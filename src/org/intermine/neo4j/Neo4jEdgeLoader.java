package org.intermine.neo4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
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
import org.intermine.metadata.ReferenceDescriptor;
import org.intermine.metadata.Model;
import org.intermine.metadata.ModelParserException;
import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.OrderDirection;
import org.intermine.pathquery.OuterJoinStatus;
import org.intermine.pathquery.PathConstraintAttribute;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Query an InterMine model and load the requested edges (relations) between the subject and
 * target objects. Requested relations are given on the command line, e.g.
 *
 * <code>Neo4jEdgeLoader Gene chromosomeLocation locatedOn</code>
 *
 * The relation can be an Intermine reference or collection and the target is a reference.
 *
 * Only nodes that have been fully loaded (i.e. their InterMine ID is in the InterMineID node)
 * are processed. This prevents partial labels from being created.
 *
 * @author Sam Hokin
 */
public class Neo4jEdgeLoader {

    /**
     * @param args command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ModelParserException {

        // get the args
        if (args.length!=3) {
            System.out.println("Usage: Neo4jEdgeLoader <SourceClass> <referenceName> <targetName>");
            System.out.println("Example: Neo4jEdgeLoader Gene chromosomeLocation locatedOn");
            System.exit(0);
        }
        String sourceClass = args[0];
        String edgeName = args[1];
        String targetName = args[2];

        // get the properties from the default file
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();

        // Neo4j setup
        Driver driver = props.getGraphDatabaseDriver();

        // InterMine setup
        Model model = props.getModel();
        QueryService service = props.getQueryService();
        PathQuery nodeQuery = new PathQuery(model);
        PathQuery refQuery = new PathQuery(model);
        PathQuery collQuery = new PathQuery(model);
        PathQuery attrQuery = new PathQuery(model);

        // load the class, attribute, reference and collection instructions for Neo4j
        Neo4jModelParser nmp = new Neo4jModelParser();
        nmp.process(props);

        // map IM classes that become edges to their Neo4j relationship type
        Map<String,String> edgeClassTypes = nmp.getRelationshipTypes();

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
        while (rows.hasNext() && (props.maxRows==0 || nodeCount<props.maxRows)) {
            nodeCount++;

            Object[] row = rows.next().toArray();
            int i = 0;
            int sid = Integer.parseInt(row[i++].toString()); // source
            int eid = Integer.parseInt(row[i++].toString()); // edge
            int tid = Integer.parseInt(row[i++].toString()); // target

            // only process edges that haven't been done for nodes that ARE fully stored on both sides
            if (thingsAlreadyStored.contains(sid) && !thingsAlreadyStored.contains(eid) && thingsAlreadyStored.contains(tid))  {

                // SET the source and target attributes (just in case, possible a stored node lacks an attribute)
                Neo4jLoader.populateIdClassAttributes(service, driver, attrQuery, sid, sourceClass, sourceDescriptor, nmp);
                Neo4jLoader.populateIdClassAttributes(service, driver, attrQuery, tid, targetClass, targetDescriptor, nmp);

                // MERGE the edge, but don't relate InterMineID nodes!
                String merge = "MATCH (s {id:"+sid+"}),(t {id:"+tid+"}) WHERE labels(s)<>'InterMineID' AND labels(t)<>'InterMineID' MERGE (s)-[:"+edgeType+" {id:"+eid+"}]->(t)";
                try (Session session = driver.session()) {
                    try (Transaction tx = session.beginTransaction()) {
                        tx.run(merge);
                        tx.success();
                        System.out.println("("+sourceClass+":"+sid+")-["+edgeType+":"+eid+"]->("+targetClass+":"+tid+")");
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
