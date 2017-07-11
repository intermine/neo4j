package org.intermine.neo4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.intermine.metadata.AttributeDescriptor;
import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.CollectionDescriptor;
import org.intermine.metadata.ConstraintOp;
import org.intermine.metadata.InterMineModelParser;
import org.intermine.metadata.ReferenceDescriptor;
import org.intermine.metadata.Model;
import org.intermine.metadata.ModelParserException;
import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.OuterJoinStatus;
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

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Query an InterMine model, and load a SINGLE object, referenced by its InterMine id,
 * along with its attributes, references and collections into a Neo4j database.
 *
 * @author Sam Hokin
 */
public class Neo4jNodeLoader {

    /**
     * @param args command line arguments
     * @throws IOException
     * @throws ModelParserException
     */
    public static void main(String[] args) throws IOException, ModelParserException, SAXException, ParserConfigurationException {

        // args
        if (args.length!=2) {
            System.out.println("Usage: Neo4jNodeLoader <IM class name> <IM id>");
            System.exit(0);
        }
        String nodeClass = args[0];
        int id = Integer.parseInt(args[1]);

        // get the properties from the default file
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();

        // Neo4j setup
        Driver driver = props.getGraphDatabaseDriver();

        // InterMine setup
        QueryService service = props.getQueryService();

        // load the class, attribute, reference and collection instructions for Neo4j
        Neo4jModelParser nmp = new Neo4jModelParser();
        nmp.process(props);

        // load InterMine model from the local XML file
        Model model = props.getModel();

        // PathQuery objects used in various places
        PathQuery nodeQuery = new PathQuery(model);
        PathQuery refQuery = new PathQuery(model);
        PathQuery collQuery = new PathQuery(model);
        PathQuery attrQuery = new PathQuery(model);

        // Get the descriptor for this node
        ClassDescriptor nodeDescriptor = model.getClassDescriptorByName(nodeClass);

        // load the attributes that are not ignored into a map
        HashMap<String,AttributeDescriptor> attrDescriptors = new HashMap<String,AttributeDescriptor>();
        for (AttributeDescriptor ad : nodeDescriptor.getAllAttributeDescriptors()) {
            if (!nmp.isIgnored(ad)) {
                String attrName = ad.getName();
                attrDescriptors.put(attrName, ad);
            }
        }
        System.out.println("Attributes:"+attrDescriptors.keySet());

        // load the references that are not ignored into a map
        HashMap<String,ReferenceDescriptor> refDescriptors = new HashMap<String,ReferenceDescriptor>();
        for (ReferenceDescriptor rd : nodeDescriptor.getAllReferenceDescriptors()) {
            if (!nmp.isIgnored(rd)) {
                String refName = rd.getName();
                refDescriptors.put(refName, rd);
            }
        }
        System.out.println("References:"+refDescriptors.keySet());

        // get the collections that are not ignored into a map
        HashMap<String,CollectionDescriptor> collDescriptors = new HashMap<String,CollectionDescriptor>();
        for (CollectionDescriptor cd : nodeDescriptor.getAllCollectionDescriptors()) {
            if (!nmp.isIgnored(cd)) {
                String collName = cd.getName();
                collDescriptors.put(collName, cd);
            }
        }
        System.out.println("Collections:"+collDescriptors.keySet());

        // query this node (which ensures that it exists)
        nodeQuery.addView(nodeClass+".id"); // every object has an IM id
        nodeQuery.addConstraint(new PathConstraintAttribute(nodeClass+".id", ConstraintOp.EQUALS, String.valueOf(id)));
        Iterator<List<Object>> rows = service.getRowListIterator(nodeQuery);
        while (rows.hasNext()) {
            Object[] row = rows.next().toArray();
            String nodeLabel = nodeClass;
            System.out.print(nodeClass+":"+id+":");

            // MERGE this node by its id
            try (Session session = driver.session()) {
                String merge = "MERGE (n:"+nodeLabel+" {id:"+id+"})";
                try (Transaction tx = session.beginTransaction()) {
                    tx.run(merge);
                    tx.success();
                    tx.close();
                }
            }

            // SET this nodes attributes
            Neo4jLoader.populateIdClassAttributes(service, driver, attrQuery, id, nodeLabel, nodeDescriptor, nmp);

            // MERGE this node's references by id, class by class
            for (String refName : refDescriptors.keySet()) {
                ReferenceDescriptor rd = refDescriptors.get(refName);
                ClassDescriptor rcd = rd.getReferencedClassDescriptor();
                String refLabel = Neo4jLoader.getFullNodeLabel(rcd);
                String relType = nmp.getRelationshipType(nodeClass, refName);
                refQuery.clearView();
                refQuery.clearConstraints();
                refQuery.addView(nodeClass+".id");
                refQuery.addView(nodeClass+"."+refName+".id");
                refQuery.addConstraint(new PathConstraintAttribute(nodeClass+".id", ConstraintOp.EQUALS, String.valueOf(id)));
                Iterator<List<Object>> rs = service.getRowListIterator(refQuery);
                while (rs.hasNext()) {
                    try {
                        Object[] r = rs.next().toArray();
                        int idn = Integer.parseInt(r[0].toString());      // node id
                        if (r[1]!=null) {                                 // refs can be null!
                            int idr = Integer.parseInt(r[1].toString());  // ref id
                            if (idr!=idn) {                               // avoid loops
                                // merge this reference node by its id
                                try (Session session = driver.session()) {
                                    String merge = "MERGE (n:"+refLabel+" {id:"+idr+"})";
                                    try (Transaction tx = session.beginTransaction()) {
                                        tx.run(merge);
                                        tx.success();
                                        tx.close();
                                    }
                                }
                                // merge this node-->reference relationship
                                try (Session session = driver.session()) {
                                    String match = "MATCH (n:"+nodeLabel+" {id:"+idn+"}),(r:"+refLabel+" {id:"+idr+"}) MERGE (n)-[:"+relType+"]->(r)";
                                    try (Transaction tx = session.beginTransaction()) {
                                        tx.run(match);
                                        tx.success();
                                        tx.close();
                                    }
                                }
                                System.out.print("r");
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            }

            // MERGE this node's collections by id, class by class
            for (String collName : collDescriptors.keySet()) {
                CollectionDescriptor cd = collDescriptors.get(collName);
                ClassDescriptor ccd = cd.getReferencedClassDescriptor();
                String collLabel = Neo4jLoader.getFullNodeLabel(ccd);
                String collType = nmp.getRelationshipType(nodeClass, collName);
                collQuery.clearView();
                collQuery.clearConstraints();
                collQuery.addView(nodeClass+".id");
                collQuery.addView(nodeClass+"."+collName+".id");
                collQuery.addConstraint(new PathConstraintAttribute(nodeClass+".id", ConstraintOp.EQUALS, String.valueOf(id)));
                Iterator<List<Object>> rs = service.getRowListIterator(collQuery);
                int collCount = 0;
                while (rs.hasNext()) {
                    collCount++;
                    try {
                        Object[] r = rs.next().toArray();
                        int idn = Integer.parseInt(r[0].toString());      // node id
                        int idc = Integer.parseInt(r[1].toString());      // collection id
                        if (idc!=idn) {                                   // avoid loops
                            // merge this collections node
                            try (Session session = driver.session()) {
                                String merge = "MERGE (n:"+collLabel+" {id:"+idc+"})";
                                try (Transaction tx = session.beginTransaction()) {
                                    tx.run(merge);
                                    tx.success();
                                    tx.close();
                                }
                            }
                            // merge this node-->coll relationship
                            try (Session session = driver.session()) {
                                String match = "MATCH (n:"+nodeLabel+" {id:"+idn+"}),(c:"+collLabel+" {id:"+idc+"}) MERGE (n)-[:"+collType+"]->(c)";
                                try (Transaction tx = session.beginTransaction()) {
                                    tx.run(match);
                                    tx.success();
                                    tx.close();
                                }
                            }
                            System.out.print("c");
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                    }
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

            System.out.println("");

        }

        // Close connections
        driver.close();

    }

}
