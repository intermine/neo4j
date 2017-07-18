package org.intermine.neo4j;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.CollectionDescriptor;
import org.intermine.metadata.ConstraintOp;
import org.intermine.metadata.ReferenceDescriptor;
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

/**
 * Complete the relationships and collections for Neo4j nodes that are incomplete,
 * i.e. their id is not in InterMineID.
 *
 * @author Sam Hokin
 */
public class Neo4jCompleter {

    /**
     * @param args command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ModelParserException, SAXException, ParserConfigurationException {

        // optional comma-separated list of classes to load; overrides loading of ALL classes other than those ignored
        List<String> loadedClasses = new LinkedList<String>();
        if (args.length>0) {
            String[] parts = args[0].split(",");
            for (String part : parts) {
                loadedClasses.add(part);
            }
        }

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
        PathQuery refQuery = new PathQuery(model);
        PathQuery collQuery = new PathQuery(model);
        PathQuery attrQuery = new PathQuery(model);

        // Put the desired class descriptors into a map so we can grab them by class name if we want; alphabetical by class simple name
        Map<String,ClassDescriptor> nodeDescriptors = new TreeMap<String,ClassDescriptor>();
        for (ClassDescriptor cd : model.getClassDescriptors()) {
            String nodeClass = cd.getSimpleName();
            if ((loadedClasses.size()>0 && loadedClasses.contains(nodeClass)) ||
                (loadedClasses.size()==0)) {
                nodeDescriptors.put(nodeClass, cd);
            }
        }

        // Retrieve the IM IDs of nodes that have already been stored into a sorted set
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

        // Retrieve the IM IDs of ALL nodes along with lists of labels, we'll complete the ones that are NOT in nodesAlreadyStored
        Map<Integer,List<Object>> allNodes = new TreeMap<Integer,List<Object>>();
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run("MATCH (n) RETURN n.id,labels(n) ORDER BY n.id");
                while (result.hasNext()) {
                    Record record = result.next();
                    if (!record.get("n.id").isNull()) {
                        int id = record.get("n.id").asInt();
                        List<Object> labels = record.get("labels(n)").asList();
                        allNodes.put(id, labels);
                    }
                }
                tx.success();
                tx.close();
            }
        }

        // Spin through the nodes, completing those that are not in nodesAlreadyStored
        for (int id : allNodes.keySet()) {
            if (!nodesAlreadyStored.contains(id)) {
                List<Object> labels = allNodes.get(id);
                String nodeClass = (String) labels.get(labels.size()-1);
                String nodeLabel = nodeClass;
                if (nodeDescriptors.containsKey(nodeClass)) {
                    ClassDescriptor nodeDescriptor = nodeDescriptors.get(nodeClass);

                    // query this node (to be sure it exists) and continue
                    nodeQuery.clearView();
                    nodeQuery.clearConstraints();
                    nodeQuery.addView(nodeClass+".id"); // every object has an IM id
                    nodeQuery.addConstraint(new PathConstraintAttribute(nodeClass+".id", ConstraintOp.EQUALS, String.valueOf(id)));
                    Iterator<List<Object>> rows = service.getRowListIterator(nodeQuery);
                    while (rows.hasNext()) {
                        Object[] row = rows.next().toArray();
                        System.out.print(nodeClass+":"+id+":");

                        // SET this node's attributes
                        Neo4jLoader.populateIdClassAttributes(service, driver, attrQuery, id, nodeLabel, nodeDescriptor, nmp);

                        // load the references, except those to be ignored, into a map
                        HashMap<String,ReferenceDescriptor> refDescriptors = new HashMap<String,ReferenceDescriptor>();
                        for (ReferenceDescriptor rd : nodeDescriptor.getAllReferenceDescriptors()) {
                            if (!nmp.isIgnored(rd)) {
                                String refName = rd.getName();
                                refDescriptors.put(refName, rd);
                            }
                        }

                        // load the collections, except those to be ignored, into a map
                        HashMap<String,CollectionDescriptor> collDescriptors = new HashMap<String,CollectionDescriptor>();
                        for (CollectionDescriptor cd : nodeDescriptor.getAllCollectionDescriptors()) {
                            if (!nmp.isIgnored(cd)) {
                                String collName = cd.getName();
                                collDescriptors.put(collName, cd);
                            }
                        }

                        // MERGE this node's references by id, class by class
                        for (String refName : refDescriptors.keySet()) {
                            ReferenceDescriptor rd = refDescriptors.get(refName);
                            ClassDescriptor rcd = rd.getReferencedClassDescriptor();
                            String refLabel = Neo4jLoader.getFullNodeLabel(rcd);
                            String relType = nmp.getRelationshipType(nodeClass,refName);
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
                                        // MERGE this reference node
                                        try (Session session = driver.session()) {
                                            String merge = "MERGE (n:"+refLabel+" {id:"+idr+"})";
                                            try (Transaction tx = session.beginTransaction()) {
                                                tx.run(merge);
                                                tx.success();
                                                tx.close();
                                            }
                                        }
                                        // MERGE this node-->reference relationship
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
                                } catch (Exception e) {
                                    System.err.println(e);
                                }
                            }
                        }

                        // MERGE this node's collections by id, one at a time
                        for (String collName : collDescriptors.keySet()) {
                            CollectionDescriptor cd = collDescriptors.get(collName);
                            ClassDescriptor ccd = cd.getReferencedClassDescriptor();
                            String collLabel = Neo4jLoader.getFullNodeLabel(ccd);
                            String collType = nmp.getRelationshipType(nodeClass,collName);
                            collQuery.clearView();
                            collQuery.clearConstraints();
                            collQuery.addView(nodeClass+".id");
                            collQuery.addView(nodeClass+"."+collName+".id");
                            collQuery.addConstraint(new PathConstraintAttribute(nodeClass+".id", ConstraintOp.EQUALS, String.valueOf(id)));
                            Iterator<List<Object>> rs = service.getRowListIterator(collQuery);
                            int collCount = 0;
                            while (rs.hasNext()) {
                                try {
                                    collCount++;
                                    Object[] r = rs.next().toArray();
                                    int idn = Integer.parseInt(r[0].toString());      // node id
                                    int idc = Integer.parseInt(r[1].toString());      // collection id
                                    // MERGE this collections node
                                    try (Session session = driver.session()) {
                                        String merge = "MERGE (n:"+collLabel+" {id:"+idc+"})";
                                        try (Transaction tx = session.beginTransaction()) {
                                            tx.run(merge);
                                            tx.success();
                                            tx.close();
                                        }
                                    }
                                    // MERGE this node-->collection relationship
                                    try (Session session = driver.session()) {
                                        String match = "MATCH (n:"+nodeLabel+" {id:"+idn+"}),(c:"+collLabel+" {id:"+idc+"}) MERGE (n)-[:"+collType+"]->(c)";
                                        try (Transaction tx = session.beginTransaction()) {
                                            tx.run(match);
                                            tx.success();
                                            tx.close();
                                        }
                                    }
                                    System.out.print("c");
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
                }
            }
        }
    }

}
