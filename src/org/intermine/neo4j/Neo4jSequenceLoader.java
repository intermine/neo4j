package org.intermine.neo4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.intermine.metadata.AttributeDescriptor;
import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.CollectionDescriptor;
import org.intermine.metadata.ConstraintOp;
import org.intermine.metadata.ReferenceDescriptor;
import org.intermine.metadata.Model;
import org.intermine.metadata.ModelParserException;
import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.PathConstraintAttribute;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.core.ServiceFactory;
import org.intermine.webservice.client.services.QueryService;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.util.Pair;

/**
 * Load Sequence residues if they are shorter than the specified maximum length.
 * 
 * @author Sam Hokin
 */
public class Neo4jSequenceLoader {

    /**
     * @param args command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ModelParserException, SAXException, ParserConfigurationException {

        // Load parameters from default properties file
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();

        // Neo4j setup
        Driver driver = props.getGraphDatabaseDriver();

        // load the class, attribute, reference and collection instructions for Neo4j
        Neo4jModelParser nmp = new Neo4jModelParser();
        nmp.process(props);

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

        // Spin through the nodes, loading residues for those that are not in nodesAlreadyStored and have short enough residues
        String nodeLabel = "Sequence";
        for (int id : sequenceNodes) {
            if (!nodesAlreadyStored.contains(id)) {
                // query this node to check its length
                nodeQuery.clearView();
                nodeQuery.clearConstraints();
                nodeQuery.addView("Sequence.id");
                nodeQuery.addView("Sequence.length");
                nodeQuery.addConstraint(new PathConstraintAttribute("Sequence.id", ConstraintOp.EQUALS, String.valueOf(id)));
                Iterator<List<Object>> rows = service.getRowListIterator(nodeQuery);
                boolean loadResidues = false;
                if (rows.hasNext()) {
                    Object[] row = rows.next().toArray();
                    if (row[1].toString()!=null) {
                        int length = Integer.parseInt(row[1].toString());
                        loadResidues = (length<props.getMaxSequenceLength());
                    }
                }
                nodeQuery.clearView();
                nodeQuery.clearConstraints();
                nodeQuery.addView("Sequence.id");
                nodeQuery.addView("Sequence.length");
                nodeQuery.addView("Sequence.md5checksum");
                if (loadResidues) nodeQuery.addView("Sequence.residues");
                nodeQuery.addConstraint(new PathConstraintAttribute("Sequence.id", ConstraintOp.EQUALS, String.valueOf(id)));
                rows = service.getRowListIterator(nodeQuery);
                if (rows.hasNext()) {
                    Object[] row = rows.next().toArray();
                    int length = Integer.parseInt(row[1].toString());
                    String cypher = "MATCH (n:Sequence {id:"+id+"}) SET n.length="+length;
                    if (!row[2].toString().equals("null")) {
                        int md5checksum = Integer.parseInt(row[2].toString());
                        cypher += ",n.md5checksum="+md5checksum;
                    }
                    if (loadResidues && !row[3].toString().equals("null")) {
                        String residues = row[3].toString();
                        cypher += ",n.residues='"+residues+"'";
                    }
                    try (Session session = driver.session()) {
                        try (Transaction tx = session.beginTransaction()) {
                            tx.run(cypher);
                            tx.success();
                            tx.close();
                            System.out.println("Sequence:"+id+" length="+length);
                        }
                    }
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
