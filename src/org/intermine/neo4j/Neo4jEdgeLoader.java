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
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import org.intermine.metadata.AttributeDescriptor;
import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.CollectionDescriptor;
import org.intermine.metadata.ConstraintOp;
import org.intermine.metadata.ReferenceDescriptor;
import org.intermine.metadata.Model;
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
 * Query an InterMine model and load the requested edges (relations) between the subject and target objects.
 * Requested relations are given on the command line, e.g.
 *
 * Neo4jEdgeLoader Gene chromosomeLocation locatedOn
 *
 * The relation can be an Intermine reference or collection and the target is a reference.
 *
 * Connection and other properties are given in neo4jloader.properties.
 *
 * Only nodes that have been fully loaded (i.e. their InterMine ID is in the InterMineID node) are processed. This prevents partial labels from being created.
 * 
 * @author Sam Hokin
 */
public class Neo4jEdgeLoader {

    static final String PROPERTIES_FILE = "neo4jloader.properties";

    /**
     * @param args command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        // get the args
        if (args.length!=3) {
            System.out.println("Usage: Neo4jEdgeLoader <SourceClass> <referenceName> <targetName>");
            System.out.println("Example: Neo4jEdgeLoader Gene chromosomeLocation locatedOn");
            System.exit(0);
        }
        String sourceClass = args[0];
        String edgeName = args[1];
        String targetName = args[2];

        // Load parameters from neo4jloader.properties
        Properties props = new Properties();
        props.load(new FileInputStream(PROPERTIES_FILE));
        String intermineServiceUrl = props.getProperty("intermine.service.url");
        String neo4jUrl = props.getProperty("neo4j.url");
        String neo4jUser = props.getProperty("neo4j.user");
        String neo4jPassword = props.getProperty("neo4j.password");
        boolean verbose = Boolean.parseBoolean(props.getProperty("verbose"));
        int maxRows = Integer.parseInt(props.getProperty("max.rows"));

        // map IM classes that become edges to their Neo4j relationship type
        Map<String,String> edgeClassTypes = new HashMap<String,String>();
        String edgeClassList = props.getProperty("intermine.edge.classes");
        String edgeTypeList = props.getProperty("neo4j.edge.types");
        if (edgeClassList!=null) {
            String[] classes = edgeClassList.split(",");
            String[] types = edgeTypeList.split(",");
            for (int i=0; i<classes.length; i++) {
                edgeClassTypes.put(classes[i], types[i]);
            }
        }
        
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

        // Retrieve the IM IDs of things that have already been stored
        List<Integer> thingsAlreadyStored = new ArrayList<Integer>();
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run("MATCH (n:InterMineID) RETURN n.id");
                while (result.hasNext()) {
                    Record record = result.next();
                    thingsAlreadyStored.add(record.get("n.id").asInt());
                }
                tx.success();
                tx.close();
            }
        }

        // Get the source descriptor
        ClassDescriptor sourceDescriptor = model.getClassDescriptorByName(sourceClass);
        if (sourceDescriptor==null) {
            System.out.println("Model does not contain class: "+sourceClass);
            System.exit(1);
        }

        // Find the edge class descriptor, trying references, then collections
        String edgeClass = null;
        ClassDescriptor edgeDescriptor = null;
        for (ReferenceDescriptor rd : sourceDescriptor.getAllReferenceDescriptors()) {
            String refName = rd.getName();
            if (refName.equals(edgeName)) {
                edgeDescriptor = rd.getReferencedClassDescriptor();
                edgeClass = edgeDescriptor.getSimpleName();
            }
        }
        if (edgeClass==null) {
            for (CollectionDescriptor cd : sourceDescriptor.getAllCollectionDescriptors()) {
                String collName = cd.getName();
                if (collName.equals(edgeName)) {
                    edgeDescriptor = cd.getReferencedClassDescriptor();
                    edgeClass = edgeDescriptor.getSimpleName();
                }
            }
        }
        if (edgeClass==null) {
            System.out.println("Couldn't find "+edgeName+" in either references or collections of "+sourceClass);
            System.exit(1);
        }

        // now get the target class referenced from the edge class
        String targetClass = null;
        ClassDescriptor targetDescriptor = null;
        for (ReferenceDescriptor rd : edgeDescriptor.getAllReferenceDescriptors()) {
            String refName = rd.getName();
            if (refName.equals(targetName)) {
                targetDescriptor = rd.getReferencedClassDescriptor();
                targetClass = targetDescriptor.getSimpleName();
            }
        }
        if (targetClass==null) {
            System.out.println("Could not find reference to "+targetName+" in class "+edgeClass);
            System.exit(1);
        }

        // now we need the attributes of the edge class
        Set<AttributeDescriptor> attrDescriptors = edgeDescriptor.getAllAttributeDescriptors();
        Map<String,String> attrNamesTypes = new HashMap<String,String>();
        for (AttributeDescriptor ad : attrDescriptors) {
            attrNamesTypes.put(ad.getName(),ad.getType());
        }
        System.out.println(edgeName+":"+attrNamesTypes.keySet());
        
        // query the source:relation attributes:target
        nodeQuery.addView(sourceClass+".id"); // Neo4j source key
        nodeQuery.addView(sourceClass+"."+edgeName+".id"); // Neo4j edge key
        nodeQuery.addView(sourceClass+"."+edgeName+"."+targetName+".id"); // Neo4j target key
        for (String attrName : attrNamesTypes.keySet()) {
            if (!attrName.equals("id")) nodeQuery.addView(sourceClass+"."+edgeName+"."+attrName);
        }

        // get the edge type from either the edgeName or the map back to its class
        String edgeType = edgeName;
        if (edgeClassTypes.containsKey(edgeClass)) edgeType = edgeClassTypes.get(edgeClass);

        int nodeCount = 0;
        Iterator<List<Object>> rows = service.getRowListIterator(nodeQuery);
        while (rows.hasNext() && (maxRows==0 || nodeCount<maxRows)) {
            nodeCount++;
			
            Object[] row = rows.next().toArray();
            int i = 0;
            int sid = Integer.parseInt(row[i++].toString()); // source
            int eid = Integer.parseInt(row[i++].toString()); // edge
            int tid = Integer.parseInt(row[i++].toString()); // target

            // only process edges that haven't been done for nodes that are fully stored on both sides
            if (thingsAlreadyStored.contains(sid) && !thingsAlreadyStored.contains(eid) && thingsAlreadyStored.contains(tid))  {
                
                // MERGE the source and target (one or other may be missing in Neo4j)
                String merge = "MERGE (s:"+sourceClass+" {id:"+sid+"}) MERGE (t:"+targetClass+" {id:"+tid+"})";
                try (Session session = driver.session()) {
                    try (Transaction tx = session.beginTransaction()) {
                        tx.run(merge);
                        tx.success();
                    }
                }
                
                // SET the source and target attributes (just in case, possible a stored node lacks an attribute)
                Neo4jLoader.populateIdClassAttributes(service, driver, attrQuery, sid, sourceClass, sourceDescriptor);
                Neo4jLoader.populateIdClassAttributes(service, driver, attrQuery, tid, targetClass, targetDescriptor);
                
                // MERGE the edge
                merge = "MATCH (s:"+sourceClass+" {id:"+sid+"}),(t:"+targetClass+" {id:"+tid+"}) MERGE (s)-[:"+edgeType+" {id:"+eid+"}]->(t)";
                System.out.println(merge);
                try (Session session = driver.session()) {
                    try (Transaction tx = session.beginTransaction()) {
                        tx.run(merge);
                        tx.success();
                    }
                }
                
                // MATCH the edge and SET its properties
                String set = "MATCH p=()-[r:"+edgeType+" {id:"+eid+"}]->() ";
                boolean first = true;
                for (String attrName : attrNamesTypes.keySet()) {
                    String attrType = attrNamesTypes.get(attrName);
                    if (!attrName.equals("id")) {
                        String val = Neo4jLoader.escapeForNeo4j(row[i++].toString());
                        if (first) {
                            set += "SET ";
                            first = false;
                        } else {
                            set += ", ";
                        }
                        if (attrType.equals("java.lang.String") || attrType.equals("org.intermine.objectstore.query.ClobAccess")) {
                            set += "r."+attrName+"=\""+val+"\"";
                        } else {
                            set += "r."+attrName+"="+val;
                        }
                    }
                }
                try (Session session = driver.session()) {
                    try (Transaction tx = session.beginTransaction()) {
                        tx.run(set);
                        tx.success();
                    }
                }
                
                // MERGE this edge's InterMine ID into the InterMine ID nodes for record-keeping that it's been stored as an edge
                try (Session session = driver.session()) {
                    try (Transaction tx = session.beginTransaction()) {
                        tx.run("MERGE (:InterMineID {id:"+eid+"})");
                        tx.success();
                        tx.close();
                    }
                }

            }

        }
        
        // Close connections
        driver.close();
        
    }

}
