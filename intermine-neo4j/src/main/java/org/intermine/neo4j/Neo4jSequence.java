package org.intermine.neo4j;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.intermine.metadata.ConstraintOp;
import org.intermine.metadata.Model;
import org.intermine.metadata.ModelParserException;
import org.intermine.pathquery.PathConstraintAttribute;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;

import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

/**
 * A class referencing a sequence record from a PostgreSQL data store along with methods to retrieve subsequences, etc.
 * Data params are given in the neo4jloader.properties file:
 *
 * sequence.pg.host = theHost
 * sequence.pg.port = 5432
 * sequence.pg.database = theDatabase
 * sequence.pg.user = theUser
 * sequence.pg.password = thePassword
 * sequence.pg.table = sequences
 *
 * The corresponding Pg table is expected to exist, created with:
 *
 * CREATE TABLE sequences (
 *   id          int    PRIMARY KEY,
 *   length      int    NOT NULL,
 *   md5checksum text,
 *   residues    oid    NOT NULL
 * );
 *
 * @author Sam Hokin
 */
public class Neo4jSequence {

    // force use of standard driver, be sure to execute with JAR file in classpath
    final static String PG_DRIVER = "org.postgresql.Driver";

    String sequencePgTable;
    String dbURL;
    
    int id;
    int start;
    int end;
    int length;
    String md5checksum;
    long loid;
    String residues;

    /**
     * Loads the full sequence for a given InterMine id.
     *
     * @param id the InterMine id of the sequence
     */
    public Neo4jSequence(int id) throws ClassNotFoundException, FileNotFoundException, IOException, SQLException {
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();
        dbURL = "jdbc:postgresql://"+props.getSequencePgHost()+":"+props.getSequencePgPort()+"/"+props.getSequencePgDatabase();
        sequencePgTable = props.getSequencePgTable();
        
        Class.forName(PG_DRIVER);
        Connection conn = DriverManager.getConnection(dbURL, props.getSequencePgUser(), props.getSequencePgPassword());
        conn.setAutoCommit(false);

        populateSequenceInfo(conn, id);
        start = 1;
        end = length;
        populateResidues(conn);
    }

    /**
     * Loads the subsequence for a given InterMine id, start and end position.
     *
     * @param id the InterMine id of the sequence
     * @param start the start position of the subsequence of interest, first position is at 1
     * @param end the end position of the subsequence of interest
     */
    public Neo4jSequence(int id, int start, int end) throws ClassNotFoundException, FileNotFoundException, IOException, SQLException {
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();
        dbURL = "jdbc:postgresql://"+props.getSequencePgHost()+":"+props.getSequencePgPort()+"/"+props.getSequencePgDatabase();
        sequencePgTable = props.getSequencePgTable();
        
        Class.forName(PG_DRIVER);
        Connection conn = DriverManager.getConnection(dbURL, props.getSequencePgUser(), props.getSequencePgPassword());
        conn.setAutoCommit(false);

        populateSequenceInfo(conn, id);
        this.start = start;
        this.end = end;
        populateResidues(conn);
    }

    /**
     * Populate the sequence info.
     *
     * @param conn the instantiated DB Connection
     * @param id the sequence InterMine id
     */
    void populateSequenceInfo(Connection conn, int id) throws SQLException {
        // outside for the try/catch
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT id,length,md5checksum,residues FROM "+sequencePgTable+"  WHERE id=?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                this.id = rs.getInt(1);
                this.length = rs.getInt(2);
                this.md5checksum = rs.getString(3);
                this.loid = rs.getLong(4);
            }
        } finally {
            if (rs!=null) rs.close();
            if (ps!=null) ps.close();
        }
    }

    /**
     * Populate the residues string given conn, start, end.
     *
     * @param conn the instantiated DB Connection
     */
    void populateResidues(Connection conn) throws SQLException, UnsupportedEncodingException {
        try {
            // initiate the large object
            LargeObjectManager lom = conn.unwrap(org.postgresql.jdbc.PgConnection.class).getLargeObjectAPI();
            LargeObject obj = lom.open(loid, LargeObjectManager.READ);
            
            // check that our sequence values agree
            if (length!=obj.size()) {
                throw new RuntimeException("ERROR: sequence is supposed to have length="+length+" but length of sequence object is "+obj.size());
            }
            
            // check that we're within the sequence range
            if (start<1 || start>obj.size()) {
                throw new RuntimeException("ERROR: start position "+start+" is out of range on the given sequence, which spans 1 to "+length);
            }
            if (end<1 || end>obj.size()) {
                throw new RuntimeException("ERROR: end position "+end+" is out of range on the given sequence, which spans 1 to "+length);
            }
            if (start>=end) {
                throw new RuntimeException("ERROR: start position >= end position.");
            }
            
            // get the residues
            byte buf[];
            int bytes = end - start + 1;
            buf = new byte[bytes];
            obj.seek(start-1);
            obj.read(buf, 0, bytes);
            
            // instance var
            residues = new String(buf, "UTF-8");
        } finally {
            if (conn!=null) conn.commit();
        }
    }

    /**
     * @param args command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {

        if (args.length!=1 && args.length!=3) {
            System.out.println("Usage Neo4jSequence <InterMine id> [start end]");
            System.exit(0);
        }

        int id = Integer.parseInt(args[0]);

        Neo4jSequence seq;
        if (args.length==1) {
            seq = new Neo4jSequence(id);
        } else {
            int start = Integer.parseInt(args[1]);
            int end = Integer.parseInt(args[2]);
            seq = new Neo4jSequence(id, start, end);
        }

        System.out.println(">"+seq.getID()+" length="+seq.getLength()+" start="+seq.getStart()+" end="+seq.getEnd()+" md5checksum="+seq.getMD5Checksum()+" loid="+seq.getLOID());
        System.out.println(seq.getResidues());

    }

    /**
     * Getters
     */
    public String getDbURL() {
        return dbURL;
    }
    public String getSequencePgTable() {
        return sequencePgTable;
    }
    public int getID() {
        return id;
    }
    public int getLength() {
        return length;
    }
    public int getStart() {
        return start;
    }
    public int getEnd() {
        return end;
    }
    public String getMD5Checksum() {
        return md5checksum;
    }
    public long getLOID() {
        return loid;
    }
    public String getResidues() {
        return residues;
    }
        
}
