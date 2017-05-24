package org.intermine.neo4j;

import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

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
 * Query an InterMine model, and load a SINGLE object, referenced by its InterMine id, along with its attributes, references and collections into a Neo4j database.
 *
 * Connection properties are given in neo4jloader.properties.
 * 
 * @author Sam Hokin
 */
public class Neo4jNodeLoader {

    static final String PROPERTIES_FILE = "neo4jloader.properties";

    /**
     * @param args command line arguments
     * @throws IOException
     * @throws ModelParserException
     */
    public static void main(String[] args) throws IOException, ModelParserException {

        // args
        if (args.length!=2) {
            System.out.println("Usage: Neo4jNodeLoader <IM class name> <IM id>");
            System.exit(0);
        }
        String nodeClass = args[0];
        int id = Integer.parseInt(args[1]);

        // Load parameters from neo4jloader.properties
        Properties props = new Properties();
        props.load(new FileInputStream(PROPERTIES_FILE));
        String intermineServiceUrl = props.getProperty("intermine.service.url");
        String neo4jUrl = props.getProperty("neo4j.url");
        String neo4jUser = props.getProperty("neo4j.user");
        String neo4jPassword = props.getProperty("neo4j.password");
        String dataModelFilename = props.getProperty("data.model.file");

        // classes to ignore, usually superclasses or maybe just classes you don't want
        List<String> ignoredClasses = new LinkedList<String>();
        if (props.getProperty("ignored.classes")!=null) ignoredClasses = Arrays.asList(props.getProperty("ignored.classes").split(","));

        // references to ignore, typically reverse-reference
        List<String> ignoredReferences = new LinkedList<String>();
        if (props.getProperty("ignored.references")!=null) ignoredReferences = Arrays.asList(props.getProperty("ignored.references").trim().split(","));

        // collections to ignore, typically reverse-reference
        List<String> ignoredCollections = new LinkedList<String>();
        if (props.getProperty("ignored.collections")!=null) ignoredCollections = Arrays.asList(props.getProperty("ignored.collections").trim().split(","));
        
        // InterMine setup
        ServiceFactory factory = new ServiceFactory(intermineServiceUrl);
        QueryService service = factory.getQueryService();

        // load local model XML file, which contains additional info for IM->Neo4j
        InterMineModelParser immp = new InterMineModelParser();
        Model model = immp.process(new InputStreamReader(new FileInputStream(dataModelFilename)));

        // PathQuery objects used in various places
        PathQuery nodeQuery = new PathQuery(model);
        PathQuery refQuery = new PathQuery(model);
        PathQuery collQuery = new PathQuery(model);
        PathQuery attrQuery = new PathQuery(model);
        
        // Neo4j setup
        Driver driver = GraphDatabase.driver(neo4jUrl, AuthTokens.basic(neo4jUser, neo4jPassword));

        // Get the descriptor for this node
        ClassDescriptor nodeDescriptor = model.getClassDescriptorByName(nodeClass);
        
        // display the attributes for the desired class
        Set<AttributeDescriptor> attrDescriptors = nodeDescriptor.getAllAttributeDescriptors();
        if (attrDescriptors.size()>1) {
            Set<String> attrNames = new HashSet<String>(); // just for output
            for (AttributeDescriptor ad : attrDescriptors) {
                attrNames.add(ad.getName());
            }
            System.out.println("Attributes:"+attrNames);
        }

        // load the references, except ignored classes and references, into a map, and display
        HashMap<String,ReferenceDescriptor> refDescriptors = new HashMap<String,ReferenceDescriptor>();
        for (ReferenceDescriptor rd : nodeDescriptor.getAllReferenceDescriptors()) {
            String refName = rd.getName();
            String refClass = rd.getReferencedClassDescriptor().getSimpleName();
            if (!ignoredClasses.contains(refClass) && !ignoredReferences.contains(nodeClass+"."+refName)) refDescriptors.put(refName, rd);
        }
        if (refDescriptors.size()>0) System.out.println("References:"+refDescriptors.keySet());

        // get the collections, except ignored classes and collections, into a map, and display
        HashMap<String,CollectionDescriptor> collDescriptors = new HashMap<String,CollectionDescriptor>();
        for (CollectionDescriptor cd : nodeDescriptor.getAllCollectionDescriptors()) {
            String collName = cd.getName();
            String collClass = cd.getReferencedClassDescriptor().getSimpleName();
            if (!ignoredClasses.contains(collClass) && !ignoredCollections.contains(nodeClass+"."+collName)) collDescriptors.put(collName, cd);
        }
        if (collDescriptors.size()>0) System.out.println("Collections:"+collDescriptors.keySet());

        // query this node (which ensures that it exists)
        nodeQuery.addView(nodeClass+".id"); // every object has an IM id
        nodeQuery.addConstraint(new PathConstraintAttribute(nodeClass+".id", ConstraintOp.EQUALS, String.valueOf(id)));
        Iterator<List<Object>> rows = service.getRowListIterator(nodeQuery);
        while (rows.hasNext()) {
            Object[] row = rows.next().toArray();
            System.out.print(nodeClass+":"+id+":");

            // MERGE this node by its id
            String nodeLabel = nodeClass;
            String merge = "MERGE (n:"+nodeLabel+" {id:"+id+"})";
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    tx.run(merge);
                    tx.success();
                    tx.close();
                }
            }

            // SET this nodes attributes
            Neo4jLoader.populateIdClassAttributes(service, driver, attrQuery, id, nodeLabel, nodeDescriptor);

            // MERGE this node's references by id, class by class
            for (String refName : refDescriptors.keySet()) {
                ReferenceDescriptor rd = refDescriptors.get(refName);
                ClassDescriptor rcd = rd.getReferencedClassDescriptor();
                String refLabel = Neo4jLoader.getFullNodeLabel(rcd);
                refQuery.clearView();
                refQuery.clearConstraints();
                refQuery.addView(nodeClass+".id");
                refQuery.addView(nodeClass+"."+refName+".id");
                refQuery.addConstraint(new PathConstraintAttribute(nodeClass+".id", ConstraintOp.EQUALS, String.valueOf(id)));
                Iterator<List<Object>> rs = service.getRowListIterator(refQuery);
                while (rs.hasNext()) {
                    Object[] r = rs.next().toArray();
                    int idn = Integer.parseInt(r[0].toString());      // node id
                    if (r[1]!=null) {                                 // refs can be null!
                        int idr = Integer.parseInt(r[1].toString());  // ref id
                        if (idr!=idn) {                               // avoid loops
                            // merge this reference node by its id
                            merge = "MERGE (n:"+refLabel+" {id:"+idr+"})";
                            try (Session session = driver.session()) {
                                try (Transaction tx = session.beginTransaction()) {
                                    tx.run(merge);
                                    tx.success();
                                    tx.close();
                                }
                            }
                            // set this reference node's attributes
                            Neo4jLoader.populateIdClassAttributes(service, driver, refQuery, idr, refLabel, rcd);
                            // merge this node-->reference relationship
                            String match = "MATCH (n:"+nodeLabel+" {id:"+idn+"}),(r:"+refLabel+" {id:"+idr+"}) MERGE (n)-[:"+refName+"]->(r)";
                            try (Session session = driver.session()) {
                                try (Transaction tx = session.beginTransaction()) {
                                    tx.run(match);
                                    tx.success();
                                    tx.close();
                                }
                            }
                            System.out.print("r");
                        }
                    }
                }
            }

            // MERGE this node's collections by id, class by class
            for (String collName : collDescriptors.keySet()) {
                CollectionDescriptor cd = collDescriptors.get(collName);
                ClassDescriptor ccd = cd.getReferencedClassDescriptor();
                String collLabel = Neo4jLoader.getFullNodeLabel(ccd);
                collQuery.clearView();
                collQuery.clearConstraints();
                collQuery.addView(nodeClass+".id");
                collQuery.addView(nodeClass+"."+collName+".id");
                collQuery.addConstraint(new PathConstraintAttribute(nodeClass+".id", ConstraintOp.EQUALS, String.valueOf(id)));
                Iterator<List<Object>> rs = service.getRowListIterator(collQuery);
                int collCount = 0;
                while (rs.hasNext()) {
                    collCount++;
                    Object[] r = rs.next().toArray();
                    int idn = Integer.parseInt(r[0].toString());      // node id
                    int idc = Integer.parseInt(r[1].toString());      // collection id
                    if (idc!=idn) {                                   // avoid loops
                        // merge this collections node
                        merge = "MERGE (n:"+collLabel+" {id:"+idc+"})";
                        try (Session session = driver.session()) {
                            try (Transaction tx = session.beginTransaction()) {
                                tx.run(merge);
                                tx.success();
                                tx.close();
                            }
                        }
                        // set this collection node's attributes
                        Neo4jLoader.populateIdClassAttributes(service, driver, attrQuery, idc, collLabel, ccd);
                        // merge this node-->coll relationship
                        String match = "MATCH (n:"+nodeLabel+" {id:"+idn+"}),(c:"+collLabel+" {id:"+idc+"}) MERGE (n)-[:"+collName+"]->(c)";
                        try (Session session = driver.session()) {
                            try (Transaction tx = session.beginTransaction()) {
                                tx.run(match);
                                tx.success();
                                tx.close();
                            }
                        }
                        System.out.print("c");
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
