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
import org.intermine.webservice.client.services.QueryService;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;

/**
 * Query an InterMine model, and load it and its data into a Neo4j database, along with the relationships derived from relations and collections.
 *
 * @author Sam Hokin
 */
public class Neo4jLoader {

    /**
     * @param args command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ModelParserException, SAXException, ParserConfigurationException {

        // optional comma-separated list of classes to load; overrides loading of ALL classes (other than those ignored)
        List<String> loadedClasses = new LinkedList<String>();
        if (args.length>0) {
            String[] parts = args[0].split(",");
            for (String part : parts) {
                loadedClasses.add(part);
            }
        }

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

        // Put the desired class descriptors into a map so we can grab them by class name if we want; alphabetical by class simple name
        Map<String,ClassDescriptor> nodeDescriptors = new TreeMap<String,ClassDescriptor>();
        for (ClassDescriptor cd : model.getClassDescriptors()) {
            if (!nmp.isIgnored(cd)) {
                String nodeClass = cd.getSimpleName();
                if (loadedClasses.size()>0 && loadedClasses.contains(nodeClass)) {
                    nodeDescriptors.put(nodeClass, cd);
                } else if (loadedClasses.size()==0) {
                    nodeDescriptors.put(nodeClass, cd);
                }
            }
        }

        // Retrieve the IM IDs of nodes that have already been fully stored
        List<Integer> nodesAlreadyStored = new LinkedList<Integer>();
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

        // Loop over IM classes and load the node, properties and relations with their corresponding reference and collections nodes (with only attributes)
        for (String nodeClass : nodeDescriptors.keySet()) {

            ClassDescriptor nodeDescriptor = nodeDescriptors.get(nodeClass);

            // display the node with labels
            System.out.println("--------------------------------------------------------");
            System.out.println(getFullNodeLabel(nodeDescriptor));

            // load the attributes, except those flagged to be ignored, into a map and display
            HashMap<String,AttributeDescriptor> attrDescriptors = new HashMap<String,AttributeDescriptor>();
            for (AttributeDescriptor ad : nodeDescriptor.getAllAttributeDescriptors()) {
                String attrName = ad.getName();
                if (!nmp.isIgnored(ad)) attrDescriptors.put(attrName, ad);

            }
            System.out.println("Attributes:"+attrDescriptors.keySet());

            // load the references, except those flagged to be ignored, into a map, and display
            HashMap<String,ReferenceDescriptor> refDescriptors = new HashMap<String,ReferenceDescriptor>();
            for (ReferenceDescriptor rd : nodeDescriptor.getAllReferenceDescriptors()) {
                String refName = rd.getName();
                if (!nmp.isIgnored(rd)) refDescriptors.put(refName, rd);
            }
            System.out.println("References:"+refDescriptors.keySet());

            // get the collections, except those flagged to be ignored, into a map, and display
            HashMap<String,CollectionDescriptor> collDescriptors = new HashMap<String,CollectionDescriptor>();
            for (CollectionDescriptor cd : nodeDescriptor.getAllCollectionDescriptors()) {
                String collName = cd.getName();
                if (!nmp.isIgnored(cd)) collDescriptors.put(collName, cd);
            }

            System.out.println("Collections:"+collDescriptors.keySet());

            // query nodes of this class
            nodeQuery.clearView();
            nodeQuery.addView(nodeClass+".id"); // every object has an IM id
            int nodeCount = 0;
            Iterator<List<Object>> rows = service.getRowListIterator(nodeQuery);
            while (rows.hasNext() && (props.maxRows==0 || nodeCount<props.maxRows)) {

                Object[] row = rows.next().toArray();
                int id = Integer.parseInt(row[0].toString());

                // abort this node if it's already stored
                if (nodesAlreadyStored.contains(id)) continue;

                if (props.verbose) System.out.print(nodeClass+":"+id+":");
                nodeCount++;

                // MERGE this node by its id
                String nodeLabel = getFullNodeLabel(nodeDescriptor);
                String merge = "MERGE (n:"+nodeLabel+" {id:"+id+"})";
                try (Session session = driver.session()) {
                    try (Transaction tx = session.beginTransaction()) {
                        tx.run(merge);
                        tx.success();
                        tx.close();
                    }
                }

                // SET this nodes attributes
                populateIdClassAttributes(service, driver, attrQuery, id, nodeLabel, nodeDescriptor, nmp);

                // CREATE unique CONSTRAINT on these individual node types
                if (nodeCount==1) {
                    try (Session session = driver.session()) {
                        List<String> labels = Arrays.asList(nodeLabel.split(":"));
                        for (String label : labels) {
                            try (Transaction tx = session.beginTransaction()) {
                                tx.run("CREATE CONSTRAINT ON ("+label+":id) ASSERT "+label+".id IS UNIQUE");
                                tx.success();
                                tx.close();
                            }
                        }
                    }
                }

                // MERGE this node's references by id, class by class
                for (String refName : refDescriptors.keySet()) {
                    ReferenceDescriptor rd = refDescriptors.get(refName);
                    ClassDescriptor rcd = rd.getReferencedClassDescriptor();
                    String refLabel = getFullNodeLabel(rcd);
                    String relType = refName;
                    if (nmp.getRelationshipType(nodeClass+"."+refName)!=null) {
                        relType = nmp.getRelationshipType(nodeClass+"."+refName);
                    }
                    refQuery.clearView();
                    refQuery.clearConstraints();
                    refQuery.addView(nodeClass+".id");
                    refQuery.addView(nodeClass+"."+refName+".id");
                    refQuery.addConstraint(new PathConstraintAttribute(nodeClass+".id", ConstraintOp.EQUALS, String.valueOf(id)));
                    Iterator<List<Object>> rs = service.getRowListIterator(refQuery);
                    while (rs.hasNext()) {
                        Object[] r = rs.next().toArray();
                        int idn = Integer.parseInt(r[0].toString());      // node id
                        if (r[1]!=null) {                                 // refs can be null sometimes
                            int idr = Integer.parseInt(r[1].toString());  // ref id
                            if (idr!=idn) {                               // no loops
                                // merge this reference node
                                merge = "MERGE (n:"+refLabel+" {id:"+idr+"})";
                                try (Session session = driver.session()) {
                                    try (Transaction tx = session.beginTransaction()) {
                                        tx.run(merge);
                                        tx.success();
                                        tx.close();
                                    }
                                }
                                // merge this node-->ref relationship
                                String match = "MATCH (n:"+nodeLabel+" {id:"+idn+"}),(r:"+refLabel+" {id:"+idr+"}) MERGE (n)-[:"+relType+"]->(r)";
                                try (Session session = driver.session()) {
                                    try (Transaction tx = session.beginTransaction()) {
                                        tx.run(match);
                                        tx.success();
                                        tx.close();
                                    }
                                }
                                if (props.verbose) System.out.print("r");
                            }
                        }
                    }
                }

                // MERGE this node's collections by id, one at a time
                for (String collName : collDescriptors.keySet()) {
                    CollectionDescriptor cd = collDescriptors.get(collName);
                    ClassDescriptor ccd = cd.getReferencedClassDescriptor();
                    String collLabel = getFullNodeLabel(ccd);
                    String collType = collName;
                    if (nmp.getRelationshipType(nodeClass+"."+collName)!=null) {
                        collType = nmp.getRelationshipType(nodeClass+"."+collName);
                    }
                    collQuery.clearView();
                    collQuery.clearConstraints();
                    collQuery.addView(nodeClass+".id");
                    collQuery.addView(nodeClass+"."+collName+".id");
                    collQuery.addConstraint(new PathConstraintAttribute(nodeClass+".id", ConstraintOp.EQUALS, String.valueOf(id)));
                    Iterator<List<Object>> rs = service.getRowListIterator(collQuery);
                    int collCount = 0;
                    while (rs.hasNext() && (props.maxRows==0 || collCount<props.maxRows)) {
                        collCount++;
                        Object[] r = rs.next().toArray();
                        int idn = Integer.parseInt(r[0].toString());      // node id
                        int idc = Integer.parseInt(r[1].toString());      // collection id
                        if (idc!=idn) {                                   // no loops
                            // merge this collections node
                            merge = "MERGE (n:"+collLabel+" {id:"+idc+"})";
                            try (Session session = driver.session()) {
                                try (Transaction tx = session.beginTransaction()) {
                                    tx.run(merge);
                                    tx.success();
                                    tx.close();
                                }
                            }
                            // merge this node-->coll relationship
                            String match = "MATCH (n:"+nodeLabel+" {id:"+idn+"}),(c:"+collLabel+" {id:"+idc+"}) MERGE (n)-[:"+collType+"]->(c)";
                            try (Session session = driver.session()) {
                                try (Transaction tx = session.beginTransaction()) {
                                    tx.run(match);
                                    tx.success();
                                    tx.close();
                                }
                            }
                        }
                        if (props.verbose) System.out.print("c");
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

                if (props.verbose) System.out.println("");

            }
        }

        // Close connections
        driver.close();

    }

    /**
     * Populate a node's attributes for a given IM class, ID and instantiated Neo4jModelParser (to determine which attributes to ignore).
     *
     * @param service the InterMine QueryService
     * @param driver the Neo4j driver
     * @param attrQuery a PathQuery for querying attributes
     * @param id the InterMine ID of the specific node
     * @param nodeLabel the node label to use in the MATCH (may differ from className because of replacement)
     * @param cd the ClassDescriptor for this InterMine object
     */
    static void populateIdClassAttributes(QueryService service, Driver driver, PathQuery attrQuery, int id, String nodeLabel, ClassDescriptor cd, Neo4jModelParser nmp) {
        String className = cd.getSimpleName();
        Set<AttributeDescriptor> attrDescriptors = cd.getAllAttributeDescriptors();
        attrQuery.clearView();
        attrQuery.clearConstraints();
        int count = 0;
        for (AttributeDescriptor ad : attrDescriptors) {
            if (!nmp.isIgnored(ad)) {
                String attrName = ad.getName();
                attrQuery.addView(className+"."+attrName);
                count++;
            }
        }
        if (count>0) {
            attrQuery.addConstraint(new PathConstraintAttribute(className+".id", ConstraintOp.EQUALS, String.valueOf(id)));
            Iterator<List<Object>> rows = service.getRowListIterator(attrQuery);
            while (rows.hasNext()) {
                Object[] row = rows.next().toArray();
                // SET this nodes attributes
                String match = "MATCH (n:"+nodeLabel+" {id:"+id+"}) SET ";
                int i = 0;
                int terms = 0;
                for (AttributeDescriptor ad : attrDescriptors) {
                    if (!nmp.isIgnored(ad)) {
                        String attrName = ad.getName();
                        String attrType = ad.getType();
                        String val = escapeForNeo4j(row[i++].toString());
                        terms++;
                        if (terms>1) match += ",";
                        if (attrType.equals("java.lang.String") || attrType.equals("org.intermine.objectstore.query.ClobAccess")) {
                            match += "n."+attrName+"=\""+val+"\"";
                        } else {
                            match += "n."+attrName+"="+val;
                        }

                    }
                }
                if (terms>0) {
                    try (Session session = driver.session()) {
                        try (Transaction tx = session.beginTransaction()) {
                            tx.run(match);
                            tx.success();
                            tx.close();
                        }
                    }
                }
            }
        }
    }

    /**
     * Escape special characters for Neo4j cypher queries.
     */
    static String escapeForNeo4j(String value) {
        StringBuilder builder = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (c=='\'')
                builder.append("\\'");
            else if (c=='\"')
                builder.append("\\\"");
            else if (c=='\r')
                builder.append("\\r");
            else if (c=='\n')
                builder.append("\\n");
            else if (c=='\t')
                builder.append("\\t");
            else if (c=='\\')
                builder.append(""); //  yank leading slashes
            else if (c < 32 || c >= 127)
                builder.append(String.format("\\u%04x", (int)c));
            else
                builder.append(c);
        }
        return builder.toString();
    }

    /**
     * Form the multiple label for a node, appending its superclass names
     *
     * @param nodeDescriptor the ClassDescriptor for the desired node
     * @return a full node label
     */
    static String getFullNodeLabel(ClassDescriptor nodeDescriptor) {
        String nodeName = nodeDescriptor.getSimpleName();
        String fullNodeLabel = nodeName;
        for (ClassDescriptor superclassDescriptor : nodeDescriptor.getAllSuperDescriptors()) {
            String superclassName = superclassDescriptor.getSimpleName();
            if (!superclassName.equals(nodeName)) {
                fullNodeLabel += ":"+superclassName;
            }
        }
        return fullNodeLabel;
    }

}
