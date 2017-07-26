package org.intermine.neo4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
 * Load Sequence residues as large objects in the PostgreSQL data store specified in neo4jloader.properties:
 *
 * sequence.pg.host = theHost
 * sequence.pg.port = 5432
 * sequence.pg.database = theDatabase
 * sequence.pg.user = theUser
 * sequence.pg.password = thePassword
 * sequence.pg.table = sequences
 *
 * The corresponding Pg table is expected to exist, created the following way:
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
public class Neo4jSequenceLoader {

    // force use of standard driver, be sure to execute with JAR file in classpath
    final static String PG_DRIVER = "org.postgresql.Driver";

    /**
     * @param args command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ModelParserException, SAXException, ParserConfigurationException, ClassNotFoundException, SQLException {

        // Load parameters from default properties file
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();

        // PostgreSQL setup
        String dbUrl = "jdbc:postgresql://"+props.getSequencePgHost()+":"+props.getSequencePgPort()+"/"+props.getSequencePgDatabase();
        Class.forName(PG_DRIVER);
        Connection conn = DriverManager.getConnection(dbUrl, props.getSequencePgUser(), props.getSequencePgPassword());
        Statement stmt = conn.createStatement();
        
        conn.setAutoCommit(false);
        LargeObjectManager lom = conn.unwrap(org.postgresql.jdbc.PgConnection.class).getLargeObjectAPI();
        
        System.out.println("Connected to "+dbUrl);
        System.out.println("Storing sequences in table "+props.getSequencePgTable());

        // Neo4j setup
        Driver driver = props.getGraphDatabaseDriver();

        // InterMine setup
        QueryService service = props.getQueryService();

        // load local model XML file, which contains additional info for IM->Neo4j
        Model model = props.getModel();

        // PathQuery objects used in various places
        PathQuery nodeQuery = new PathQuery(model);
        PathQuery attrQuery = new PathQuery(model);
        
        // Retrieve the IM IDs of nodes that have already been stored and store in a set
        Set<Integer> nodesAlreadyStored = new TreeSet<Integer>();
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run("MATCH (n:InterMineID) RETURN n.id ORDER BY n.id");
                while (result.hasNext()) {
                    Record record = result.next();
                    nodesAlreadyStored.add(record.get("n.id").asInt());
                }
                tx.success();
                tx.close();
            }
        }

        // Retrieve the IM IDs of all Sequence nodes
        Set<Integer> sequenceNodes = new TreeSet<Integer>();
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run("MATCH (n:Sequence) RETURN n.id ORDER BY n.id");
                while (result.hasNext()) {
                    Record record = result.next();
                    if (!record.get("n.id").isNull()) {
                        sequenceNodes.add(record.get("n.id").asInt());
                    }
                }
                tx.success();
                tx.close();
            }
        }

        // Spin through the sequence nodes, storing residues for those that are not in nodesAlreadyStored
        String nodeLabel = "Sequence";
        System.out.println("id\t\tlength\tmd5checksum");
        for (int id : sequenceNodes) {
            if (!nodesAlreadyStored.contains(id)) {

                nodeQuery.clearView();
                nodeQuery.clearConstraints();
                nodeQuery.addView("Sequence.id");
                nodeQuery.addView("Sequence.length");
                nodeQuery.addView("Sequence.md5checksum");
                nodeQuery.addView("Sequence.residues");
                nodeQuery.addConstraint(new PathConstraintAttribute("Sequence.id", ConstraintOp.EQUALS, String.valueOf(id)));
                Iterator<List<Object>> rows = service.getRowListIterator(nodeQuery);
                if (rows.hasNext()) {
                    Object[] row = rows.next().toArray();
                    int sid = Integer.parseInt(row[0].toString());
                    int length = Integer.parseInt(row[1].toString());
                    String md5checksum = row[2].toString();
                    String residues = row[3].toString();
                    System.out.print(id);
                    System.out.print("\t"+length);
                    System.out.print("\t"+md5checksum);
                    if (residues.length()>5) System.out.print("\t"+residues.substring(0,5)+"...");
                    System.out.println("");

                    // insert the large object
                    long loid = lom.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
                    LargeObject lo = lom.open(loid, LargeObjectManager.WRITE);
                    lo.write(residues.getBytes());
                    lo.close();
                    
                    // insert the row into the sequences table
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO "+props.getSequencePgTable()+" VALUES (?, ?, ?, ?)");
                    ps.setInt(1, sid);
                    ps.setInt(2, length);
                    if (md5checksum.equals("null")) {
                        ps.setNull(3, java.sql.Types.VARCHAR);
                    } else {
                        ps.setString(3, md5checksum);
                    }
                    ps.setLong(4, loid);
                    ps.executeUpdate();
                    ps.close();
                    
                    // commit the transaction since autocommit is off
                    conn.commit();

                    // MERGE this node's InterMine ID into the InterMine ID nodes for record-keeping that it's stored
                    try (Session session = driver.session()) {
                        try (Transaction tx = session.beginTransaction()) {
                            tx.run("MERGE (:InterMineID {id:"+id+"})");
                            tx.success();
                            tx.close();
                        }
                    }
                }

            }
        }
    }
    
}
