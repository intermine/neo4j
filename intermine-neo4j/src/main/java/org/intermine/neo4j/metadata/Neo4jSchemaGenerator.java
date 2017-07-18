package org.intermine.neo4j.metadata;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.ClientException;

import java.util.*;

/**
 * A Java class which can be used to perform various operations on the schema/metagraph
 * of the given Neo4j graph database.
 * <p>
 * Metagraph structure described at https://github.com/intermine/neo4j/blob/dev/metadata.md
 *</p>
 *
 * @author Yash Sharma
 */
public class Neo4jSchemaGenerator {

    /**
     * Creates a Metagraph which represents all the nodes and relationships in the database.
     * @param driver Neo4j Java Driver instance.
     */
    public static void generateSchema(Driver driver){
        mapNodes(driver);
        addConstraintsForNodes(driver);
        mapRelationships(driver);
        addConstraintsForRelationships(driver);
        if (schemaExists(driver)){
            System.out.println("Schema successfully created.");
        }
    }

    /**
     * Creates NodeType nodes for every type of node that exist in the database.
     * @param driver Neo4j Java Driver instance.
     */
    private static void mapNodes(Driver driver){
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                // For each node in the database that does not belong to the metagraph and has
                // at least one label, check if its corresponding NodeType node is present.
                // If not, then add a new one. Determine the Union of the properties set of the NodeType
                // node & all the properties of the current node and store it in the NodeType node.
                String query = "MATCH (n)\n" +
                                "WHERE NOT n:Metagraph AND size(labels(n))>0\n" +
                                "WITH labels(n) as LABELS, keys(n) as KEYS\n" +
                                "MERGE (m:Metagraph:NodeType {labels: LABELS})\n" +
                                "SET m.properties =\n" +
                                "CASE m.properties\n" +
                                "\tWHEN NULL THEN KEYS\n" +
                                "\tELSE apoc.coll.union(m.properties, KEYS)\n" +
                                "END\n";
                tx.run(query);
                tx.success();
                tx.close();
            }
            catch (ClientException e){
                System.out.println("Schema Generator requires APOC 3.1+ to run properly. Please add the apoc jar to neo4jhome/plugins directory and restart the Neo4j database instance.\n");
                System.exit(0);
            }
        }
    }

    /**
     * Creates RelType nodes for every type of relationship that exist in the database.
     * Also set StartNodeType & EndNodeType relationship for each such node.
     * @param driver Neo4j Java Driver instance.
     */
    private static void mapRelationships(Driver driver){
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                // For each connected pair of nodes and the corresponding relationship in the database
                // that do not belong to the metagraph, check if the corresponding NodeType & RelType
                // nodes are present. If not, then add a new ones. Add StartNodeType & EndNodeType
                // relations. Determine the Union of the properties set of the RelType node & all the
                // properties of the current relationship and store it in the RelType node.
                String query = "MATCH (n)-[r]->(m)\n" +                                "WHERE NOT n:Metagraph AND NOT m:Metagraph\n" +
                                "WITH labels(n) as start_labels, type(r) as rel_type, keys(r) as rel_keys, labels(m) as end_labels\n" +
                                "MERGE (a:Metagraph:NodeType {labels: start_labels})\n" +
                                "MERGE (b:Metagraph:NodeType {labels: end_labels})\n" +
                                "MERGE (rel:Metagraph:RelType {type:rel_type})\n" +
                                "MERGE (a)<-[:StartNodeType]-(rel)-[:EndNodeType]->(b)\n" +
                                "SET rel.properties =\n" +
                                "CASE \n" +
                                "\tWHEN rel.properties IS NULL AND rel_keys IS NULL THEN []\n" +
                                "\tWHEN rel.properties IS NULL AND rel_keys IS NOT NULL THEN rel_keys\n" +
                                "\tWHEN rel.properties IS NOT NULL AND rel_keys IS NULL THEN rel.properties\n" +
                                "\tWHEN rel.properties IS NOT NULL AND rel_keys IS NOT NULL THEN " +
                                "apoc.coll.union(rel.properties, rel_keys)\n" +
                                "END\n";
                tx.run(query);
                tx.success();
                tx.close();
            }
            catch (ClientException e){
                System.out.println("Schema Generator requires APOC 3.1+ to run properly. Please add the apoc jar to neo4jhome/plugins directory and restart the Neo4j database instance.\n");
                System.exit(0);
            }
        }
    }

    /**
     * Add Uniqueness constraint for NodeType nodes.
     * @param driver Neo4j Java Driver instance.
     */
    private static void addConstraintsForNodes(Driver driver){
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                // For each set of labels, there must exist only one NodeType node
                // in the metagraph.
                String query = "CREATE CONSTRAINT ON (a:NodeType) ASSERT " +
                                "a.labels IS UNIQUE";
                tx.run(query);
                tx.success();
                tx.close();
            }
        }
    }

    /**
     * Add Uniqueness constraint for RelType nodes.
     * @param driver Neo4j Java Driver instance.
     */
    private static void addConstraintsForRelationships(Driver driver){
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                // For each type there must exist only one RelType node
                // in the metagraph.
                String query = "CREATE CONSTRAINT ON (a:RelType) ASSERT " +
                "a.type IS UNIQUE";
                tx.run(query);
                tx.success();
                tx.close();
            }
        }
    }

    /**
     * Finds out if schema (metagraph) exists in the Neo4j database.
     * @param driver Neo4j Java Driver instance.
     * @return true if schema (metagraph) exists in the Neo4j database, false otherwise
     */
    public static boolean schemaExists(Driver driver){
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                // Find out total number of nodes in the metagraph.
                String query = "MATCH (n:Metagraph) RETURN COUNT(n) AS count";
                StatementResult result = tx.run(query);
                Record record = result.next();
                int count = record.get("count").asInt();
                tx.success();
                tx.close();
                if(count > 0){
                    return true;
                }
                return false;
            }
        }
    }

    /**
     * A utility function that takes a list of objects as input, converts it into
     * a list of String objects then returns the list after sorting.
     * @param listObjects A list of objects.
     * @return Sorted list of strings.
     */
    private static List<String> processList(List<Object> listObjects){
        List<String> listStrings = new ArrayList<>(listObjects.size());
        for(Object object: listObjects){
            listStrings.add(object.toString());
        }
        Collections.sort(listStrings);
        return listStrings;
    }

    /**
     * Generates a NodeTypeDescriptor from a record.
     * @param record a row of from neo4j result.
     * @return A NodeTypeDescriptor object based on the record.
     */
    private static NodeTypeDescriptor getNodeDescriptor(Record record){
        Value labelsValue = record.get("labels");
        Set<String> labels;
        if(!labelsValue.isNull() && !labelsValue.isEmpty()){
            labels = new HashSet<>(processList(labelsValue.asList()));
        }
        else{
            labels = new HashSet<>();
        }

        Value propertiesValue = record.get("properties");
        Set<String> properties;
        if(!propertiesValue.isNull() && !propertiesValue.isEmpty()){
            properties = new HashSet<>(processList(propertiesValue.asList()));
        }
        else{
            properties = new HashSet<>();
        }

        return new NodeTypeDescriptor(labels, properties);
    }

    /**
     * Generates a RelTypeDescriptor from a record.
     * @param record a row of from neo4j result.
     * @return A RelTypeDescriptor object based on the record.
     */
    private static RelTypeDescriptor getRelationshipDescriptor(Record record){
        String type = record.get("type").asString();
        Value value = record.get("properties");
        Set<String> properties;
        if(!value.isNull() && !value.isEmpty()){
            properties = new HashSet<>(processList(value.asList()));
        }
        else{
            properties = new HashSet<>();
        }

        return new RelTypeDescriptor(type, properties);
    }

    /**
     * A utility function which adds a set to the map of set of sets.
     */
    private static void addSetToMap(Map<String, Set<Set<String>>> map, String key, Set<String> set){
        if(map.containsKey(key)){
            Set<Set<String>> setOfSets = map.get(key);
            setOfSets.add(set);
            map.put(key, setOfSets);
            return;
        }
        Set<Set<String>> setOfSets = new HashSet<>();
        setOfSets.add(set);
        map.put(key, setOfSets);
    }

    /**
     * Destroys the existing schema/metagraph from the database.
     * @param driver Neo4j Java Driver instance.
     */
    public static void destroySchema(Driver driver){
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                // Delete each node of the metagraph.
                String query = "MATCH (n:Metagraph) DETACH DELETE n";
                tx.run(query);
                tx.success();
                tx.close();
            }
        }
    }

    /**
     * Creates and returns a {@link Model Model} object which represents the metagraph/schema
     * of the database.
     * @param driver Neo4j Java Driver instance.
     * @return A model object which represents the metagraph (schema).
     */
    public static Model getModel(Driver driver){
        if(!schemaExists(driver)){
            generateSchema(driver);
        }

        Set<NodeTypeDescriptor> nodes = new HashSet<>();
        Set<RelTypeDescriptor> relationships = new HashSet<>();

        Map<String, Set<Set<String>>> startNodes = new HashMap<>();
        Map<String, Set<Set<String>>> endNodes = new HashMap<>();

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                // Find details of all the NodeType nodes
                String query = "MATCH (n:Metagraph:NodeType) " +
                        "RETURN DISTINCT n.labels as labels, n.properties as properties";
                StatementResult result = tx.run(query);

                // For each NodeType create a NodeTypeDescriptor
                while (result.hasNext()) {
                    Record record = result.next();
                    nodes.add(getNodeDescriptor(record));
                }

                // Find details of all the RelType nodes and their Start & End NodeType nodes
                query = "MATCH (startNode:Metagraph:NodeType)<-[:StartNodeType]-" +
                        "(n:Metagraph:RelType)-[:EndNodeType]->" +
                        "(endNode:Metagraph:NodeType) RETURN DISTINCT startNode.labels as startLabels, " +
                        "endNode.labels as endLabels, n.type as type, n.properties as properties";
                result = tx.run(query);

                // For relationship, create a RelTypeDescriptor and
                // store the labels of Start & End nodes.
                while (result.hasNext()) {
                    Record record = result.next();
                    relationships.add(getRelationshipDescriptor(record));
                    String key = record.get("type").asString();
                    Set<String> startSet = new HashSet<>(processList(record.get("startLabels").asList()));
                    Set<String> endSet = new HashSet<>(processList(record.get("endLabels").asList()));
                    addSetToMap(startNodes, key, startSet);
                    addSetToMap(endNodes, key, endSet);
                }

                tx.success();
                tx.close();
            }
        }
        return new Model(nodes, relationships, startNodes, endNodes);
    }
}
