package org.intermine.neo4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Properties;

import org.intermine.metadata.AttributeDescriptor;
import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.CollectionDescriptor;
import org.intermine.metadata.ConstraintOp;
import org.intermine.metadata.ReferenceDescriptor;
import org.intermine.metadata.Model;
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

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Complete the relationships and collections for Neo4j nodes that are incomplete, i.e. their id is not in InterMineID.
 *
 * Connection and other properties are given in neo4jloader.properties.
 * 
 * @author Sam Hokin
 */
public class Neo4jCompleter {

    static final String PROPERTIES_FILE = "neo4jloader.properties";

    /**
     * @param args command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        // Load parameters from neo4jloader.properties
        Properties props = new Properties();
        props.load(new FileInputStream(PROPERTIES_FILE));
        String intermineServiceUrl = props.getProperty("intermine.service.url");
        String neo4jUrl = props.getProperty("neo4j.url");
        String neo4jUser = props.getProperty("neo4j.user");
        String neo4jPassword = props.getProperty("neo4j.password");
        boolean verbose = Boolean.parseBoolean(props.getProperty("verbose"));

        // classes to ignore, usually superclasses or maybe just classes you don't want
        List<String> ignoredClasses = new ArrayList<String>();
        if (props.getProperty("ignored.classes")!=null) ignoredClasses = Arrays.asList(props.getProperty("ignored.classes").trim().split(","));

        // classes to load, overrides loading all classes other than those ignored
        List<String> loadedClasses = new ArrayList<String>();
        if (props.getProperty("loaded.classes")!=null && props.getProperty("loaded.classes").trim().length()>0) loadedClasses = Arrays.asList(props.getProperty("loaded.classes").trim().split(","));
        
        // references to ignore, typically reverse-reference
        List<String> ignoredReferences = new ArrayList<String>();
        if (props.getProperty("ignored.references")!=null) ignoredReferences = Arrays.asList(props.getProperty("ignored.references").trim().split(","));

        // collections to ignore, typically reverse-reference
        List<String> ignoredCollections = new ArrayList<String>();
        if (props.getProperty("ignored.collections")!=null) ignoredCollections = Arrays.asList(props.getProperty("ignored.collections").trim().split(","));

        // InterMine setup
        ServiceFactory factory = new ServiceFactory(intermineServiceUrl);
        Model model = factory.getModel();
        QueryService service = factory.getQueryService();
        PathQuery nodeQuery = new PathQuery(model);
        PathQuery refQuery = new PathQuery(model);
        PathQuery collQuery = new PathQuery(model);
        PathQuery attrQuery = new PathQuery(model);
        
        // Neo4j setup
        Driver driver = GraphDatabase.driver(neo4jUrl, AuthTokens.basic(neo4jUser, neo4jPassword));

        // Put the desired class descriptors into a map so we can grab them by class name if we want; alphabetical by class simple name
        Map<String,ClassDescriptor> nodeDescriptors = new TreeMap<String,ClassDescriptor>();
        for (ClassDescriptor cd : model.getClassDescriptors()) {
            String nodeClass = cd.getSimpleName();
            if (loadedClasses.size()>0 && loadedClasses.contains(nodeClass)) {
                nodeDescriptors.put(nodeClass, cd);
            } else if (loadedClasses.size()==0 && !ignoredClasses.contains(nodeClass)) {
                nodeDescriptors.put(nodeClass, cd);
            }
        }
        
        // Retrieve the IM IDs of nodes that have already been stored
        List<Integer> nodesAlreadyStored = new ArrayList<Integer>();
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run("MATCH (n:InterMineID) RETURN n.id");
                while (result.hasNext()) {
                    Record record = result.next();
                    nodesAlreadyStored.add(record.get("n.id").asInt());
                }
                tx.success();
                tx.close();
            }
        }

        // Retrieve the IM IDs of ALL nodes along with lists of labels, we'll complete the ones that are NOT in nodesAlreadyStored
        Map<Integer,List<Object>> allNodes = new HashMap<Integer,List<Object>>();
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run("MATCH (n) RETURN n.id,labels(n)");
                while (result.hasNext()) {
                    Record record = result.next();
                    int id = record.get("n.id").asInt();
                    List<Object> labels = record.get("labels(n)").asList();
                    allNodes.put(id, labels);
                }
                tx.success();
                tx.close();
            }
        }

        // Store the IM IDs of nodes that have had their attributes stored, augmented within storing nodes below
        List<Integer> nodesWithAttributesStored = new ArrayList<Integer>(allNodes.keySet());

        // Spin through the nodes, completing those that are not in nodesAlreadyStored
        for (int id : allNodes.keySet()) {
            if (!nodesAlreadyStored.contains(id)) {
                List<Object> labels = allNodes.get(id);
                String nodeClass = (String) labels.get(labels.size()-1);
                if (nodeDescriptors.containsKey(nodeClass)) {
                    ClassDescriptor nodeDescriptor = nodeDescriptors.get(nodeClass);
                    
                    // load the references, except ignored classes, into a map, and display
                    HashMap<String,ReferenceDescriptor> refDescriptors = new HashMap<String,ReferenceDescriptor>();
                    for (ReferenceDescriptor rd : nodeDescriptor.getAllReferenceDescriptors()) {
                        String refName = rd.getName();
                        String refClass = rd.getReferencedClassDescriptor().getSimpleName();
                        if (!ignoredClasses.contains(refClass)) refDescriptors.put(refName, rd);
                    }
                    
                    // get the collections, except ignored classes, into a map, and display
                    HashMap<String,CollectionDescriptor> collDescriptors = new HashMap<String,CollectionDescriptor>();
                    for (CollectionDescriptor cd : nodeDescriptor.getAllCollectionDescriptors()) {
                        String collName = cd.getName();
                        String collClass = cd.getReferencedClassDescriptor().getSimpleName();
                        if (!ignoredClasses.contains(collClass)) collDescriptors.put(collName, cd);
                    }

                    // query this node (to be sure it exists) and continue
                    nodeQuery.clearView();
                    nodeQuery.clearConstraints();
                    nodeQuery.addView(nodeClass+".id"); // every object has an IM id
                    nodeQuery.addConstraint(new PathConstraintAttribute(nodeClass+".id", ConstraintOp.EQUALS, String.valueOf(id)));
                    Iterator<List<Object>> rows = service.getRowListIterator(nodeQuery);
                    while (rows.hasNext()) {
                        Object[] row = rows.next().toArray();
                        System.out.print(nodeClass+":"+id+":");

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
                                    // merge this reference node
                                    String merge = "MERGE (n:"+refLabel+" {id:"+idr+"})";
                                    try (Session session = driver.session()) {
                                        try (Transaction tx = session.beginTransaction()) {
                                            tx.run(merge);
                                            tx.success();
                                            tx.close();
                                        }
                                    }
                                    // set this reference node's attributes
                                    Neo4jLoader.populateIdClassAttributes(service, driver, refQuery, idr, refLabel, rcd);
                                    // merge this node-->ref relationship
                                    String match = "MATCH (n:"+nodeClass+" {id:"+idn+"}),(r:"+refLabel+" {id:"+idr+"}) MERGE (n)-[:"+refName+"]->(r)";
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

                        // MERGE this node's collections by id, one at a time
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
                                // merge this collections node
                                String merge = "MERGE (n:"+collLabel+" {id:"+idc+"})";
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
                                String match = "MATCH (n:"+nodeClass+" {id:"+idn+"}),(c:"+collLabel+" {id:"+idc+"}) MERGE (n)-[:"+collName+"]->(c)";
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
                }
            }
        }
    }

}
