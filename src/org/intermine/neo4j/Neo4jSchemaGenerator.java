package org.intermine.neo4j;

import org.neo4j.driver.v1.*;

import java.util.*;

/**
 * Generates schema of a given Neo4j database and stores it in the database itself.
 * All nodes of describing the schema are assigned a "Metagraph" label.
 * Metagraph structure described at https://gist.github.com/yasharmaster/8071e53c500081660b9e5c203b913b6d
 *
 * @author Yash Sharma
 */
public class Neo4jSchemaGenerator {

    /**
     * Creates a Metagraph which maps all the nodes and relationships of the database.
     * @param driver Neo4j Java Driver instance.
     */
    public static void generateSchema(Driver driver){
        // Create three initial nodes of the Metagraph : Root, NodeDescriptor Owner & Rel Owner.
        createInitialNodes(driver);

        // Creates Representative NodeType & RelType nodes for each relationship & node and
        // Add required relationships between them.
        mapConnectedNodes(driver);

        // Create Representative (Admin) nodes for all disconnected nodes.
        mapDisconnectedNodes(driver);
    }

    /**
     * Create three initial nodes of the Metagraph : Root, NodeDescriptor Owner & Rel Owner.
     * @param driver Neo4j Java Driver instance.
     */
    private static void createInitialNodes(Driver driver){
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                String query = "MERGE (m:Metagraph { metaType: 'NodeTypeOwner' })" +
                                "<-[:OWNS]-(o:Metagraph { metaType: 'RootOwner' })-[:OWNS]->" +
                                "(n:Metagraph { metaType: 'RelTypeOwner' })";
                tx.run(query);
                tx.success();
                tx.close();

                // Log the progress
                System.out.println("Schema Progress : Initial Nodes Created - Root NodeDescriptor, NodeTypeOwner & RelTypeOwner.");
            }
        }
    }

    /**
     * Maps all the connected nodes of the database to the schema.
     * @param driver Neo4j Java Driver instance.
     */
    private static void mapConnectedNodes(Driver driver){
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                // For all connected pair of nodes which are not in the metagraph,
                // return the set of labels & properties of the nodes
                // and the type & properties of their relationship.
                String readQuery = "MATCH (n)-[r]->(m) " +
                                    "WHERE NOT n:Metagraph AND NOT m:Metagraph " +
                                    "RETURN DISTINCT labels(n), keys(n), " +
                                    "type(r), keys(r), labels(m), keys(m)";
                StatementResult result = tx.run(readQuery);

                // For each node and relationship store NodeType and RelTpe nodes respectively,
                // And add required relationships.
                while (result.hasNext()) {
                    Record record = result.next();
                    System.out.println("------------------Record------------------");
                    System.out.println(record.toString());
                    Map<String, Object> params = getQueryParams(record);
                    String writeQuery = generateWriteQuery(record);
                    tx.run(writeQuery, params);
                }
                tx.success();
                tx.close();

                // Log the progress
                System.out.println("Schema Progress : Mapped Connected Nodes.");
            }
        }
    }

    /**
     * Maps all the disconnected nodes of the database to the schema.
     * @param driver Neo4j Java Driver instance.
     */
    private static void mapDisconnectedNodes(Driver driver){
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                // For all disconnected nodes which are not in the metagraph,
                // return the set of labels & properties of these nodes
                String readQuery = "match (n)\n" +
                        "with n\n" +
                        "optional match (n)-[r]-()\n" +
                        "with n, count(r) as c\n" +
                        "where c=0\n" +
                        "return labels(n), keys(n)";
                StatementResult result = tx.run(readQuery);

                // For each node store NodeType and add required relationships.
                while (result.hasNext()) {
                    Record record = result.next();
                    System.out.println("------------------Record------------------");
                    System.out.println(record.toString());
                    Map<String, Object> params = getQueryParams(record);
                    String writeQuery = generateDisconnectedWriteQuery(record);
                    tx.run(writeQuery, params);
                }
                tx.success();
                tx.close();

                // Log the progress
                System.out.println("Schema Progress : Mapped Disconnected Nodes.");
            }
        }
    }

    /**
     * Generates a write cypher query for mapping connected nodes based on the data provided.
     * @param record A record containing one record of the read cypher query.
     * @return A string containing the cypher query.
     */
    private static String generateWriteQuery(Record record){
        Value startNodeLabels = record.get("labels(n)");
        Value startNodeKeys = record.get("keys(n)");
        Value endNodeLabels = record.get("labels(m)");
        Value endNodeKeys = record.get("keys(m)");
        Value relType = record.get("type(r)");
        Value relKeys = record.get("keys(r)");

        String query = "MATCH (nodeOwner:Metagraph { metaType: 'NodeTypeOwner' }), " +
                        "(relOwner:Metagraph { metaType: 'RelTypeOwner' }) " +
                        "MERGE (nodeOwner)-[:OWNS]->(n:Metagraph { metaType: 'NodeType'";
        if (!startNodeLabels.isNull() && !startNodeLabels.isEmpty()){
            query = query + ", labels: $startNodeLabels";
        }
        if (!startNodeKeys.isNull() && !startNodeKeys.isEmpty()){
            query = query + ", properties: $startNodeKeys";
        }
        query = query + "}) MERGE (nodeOwner)-[:OWNS]->(m:Metagraph { metaType: 'NodeType'";
        if (!endNodeLabels.isNull() && !endNodeLabels.isEmpty()){
            query = query + ", labels: $endNodeLabels";
        }
        if (!endNodeKeys.isNull() && !endNodeKeys.isEmpty()){
            query = query + ", properties: $endNodeKeys";
        }
        query = query + "}) MERGE (relOwner)-[:OWNS]->(r:Metagraph { metaType: 'RelType'";
        if (!relType.isNull()){
            query = query + ", type: $relType";
        }
        if (!relKeys.isNull() && !relKeys.isEmpty()){
            query = query + ", properties: $relKeys";
        }
        query = query + "}) MERGE (n)<-[:StartNodeType]-(r)-[:EndNodeType]->(m)";

        //Print the generated query
        System.out.println("-------------------Query------------------");
        System.out.println(query);
        System.out.println("------------------------------------------");

        return query;
    }

    /**
     * Generates a write cypher query for mapping disconnected nodes based on the data provided.
     * @param record A record containing one record of the read cypher query.
     * @return A string containing the cypher query.
     */
    private static String generateDisconnectedWriteQuery(Record record){
        Value startNodeLabels = record.get("labels(n)");
        Value startNodeKeys = record.get("keys(n)");

        String query = "MATCH (nodeOwner:Metagraph { metaType: 'NodeTypeOwner' }) " +
                "MERGE (nodeOwner)-[:OWNS]->(n:Metagraph { metaType: 'NodeType'";
        if (!startNodeLabels.isNull() && !startNodeLabels.isEmpty()){
            query = query + ", labels: $startNodeLabels";
        }
        if (!startNodeKeys.isNull() && !startNodeKeys.isEmpty()){
            query = query + ", properties: $startNodeKeys";
        }
        query = query + "})";

        //Print the generated query
        System.out.println("-------------------Query------------------");
        System.out.println(query);
        System.out.println("------------------------------------------");

        return query;
    }

    /**
     * Takes a list of objects, converts it into a list of Strings then returns the list after sorting.
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
     * Creates a HashMap containing all the parameters to be passed to the write cypher query.
     * @param record A record containing one record of the read cypher query.
     * @return A HashMap containing all the parameters.
     */
    private static HashMap<String, Object> getQueryParams(Record record){
        Value startNodeLabels = record.get("labels(n)");
        Value startNodeKeys = record.get("keys(n)");
        Value endNodeLabels = record.get("labels(m)");
        Value endNodeKeys = record.get("keys(m)");
        Value relType = record.get("type(r)");
        Value relKeys = record.get("keys(r)");

        HashMap<String, Object> params = new HashMap<>();

        if (!startNodeLabels.isNull() && !startNodeLabels.isEmpty()){
            params.put("startNodeLabels", processList(startNodeLabels.asList()));
        }
        if (!startNodeKeys.isNull() && !startNodeKeys.isEmpty()){
            params.put("startNodeKeys", processList(startNodeKeys.asList()));
        }
        if (!endNodeLabels.isNull() && !endNodeLabels.isEmpty()){
            params.put("endNodeLabels", processList(endNodeLabels.asList()));
        }
        if (!endNodeKeys.isNull() && !endNodeKeys.isEmpty()){
            params.put("endNodeKeys", processList(endNodeKeys.asList()));
        }
        if (!relType.isNull()){
            params.put("relType", relType.asString());
        }
        if (!relKeys.isNull() && !relKeys.isEmpty()){
            params.put("relKeys", processList(relKeys.asList()));
        }

        return params;
    }

    /**
     * Generates a NodeDescriptor from a record.
     * @param record a row of from neo4j result.
     * @return A NodeDescriptor object based on the record.
     */
    private static NodeDescriptor getNodeDescriptor(Record record){
        Set<String> labels = new HashSet<>(processList(record.get("labels").asList()));
        Set<String> properties = new HashSet<>(processList(record.get("properties").asList()));

        return new NodeDescriptor(labels, properties);
    }

    /**
     * Generates a RelationshipDescriptor from a record.
     * @param record a row of from neo4j result.
     * @return A RelationshipDescriptor object based on the record.
     */
    private static RelationshipDescriptor getRelationshipDescriptor(Record record){
        String type = record.get("relType").asString();
        Value value = record.get("relProperties");
        Set<String> properties;
        if(!value.isNull() && !value.isEmpty()){
            properties = new HashSet<>(processList(value.asList()));
        }
        else{
            properties = new HashSet<>();
        }
        return new RelationshipDescriptor(type, properties);
    }

    /**
     * Adds a set to the map of set of sets.
     *
     */
    private static void addSetToMap(Map<String, Set<Set<String>>> map, String key, Set<String> set){
        if(map.containsKey(key)){
            Set<Set<String>> setOfSets = map.get(key);
            setOfSets.add(set);
            map.put(key, setOfSets);
        }
        Set<Set<String>> setOfSets = new HashSet<>();
        setOfSets.add(set);
        map.put(key, setOfSets);
    }

    /**
     * Creates and returns a Model object from the metagraph
     * @param driver Neo4j Java Driver instance.
     * @return Model A model object which represents the metagraph (schema).
     */
    public static Model getModel(Driver driver){
        Set<NodeDescriptor> nodes = new HashSet<>();
        Set<RelationshipDescriptor> relationships = new HashSet<>();

        Map<String, Set<Set<String>>> startNodes = new HashMap<>();
        Map<String, Set<Set<String>>> endNodes = new HashMap<>();

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                String query = "MATCH (n:Metagraph {metaType : 'NodeType'}) " +
                        "RETURN DISTINCT n.labels as labels, n.properties as properties";
                StatementResult result = tx.run(query);

                // For each node create a NodeDescriptor
                while (result.hasNext()) {
                    Record record = result.next();
                    nodes.add(getNodeDescriptor(record));
                }

                query = "MATCH (startNode:Metagraph)<-[:StartNodeType]-" +
                        "(n:Metagraph {metaType : 'RelType'})-[:EndNodeType]->" +
                        "(endNode:Metagraph) RETURN DISTINCT startNode.labels as startLabels, " +
                        "endNode.labels as endLabels, n.type as relType, n.properties as relProperties";
                result = tx.run(query);

                // For relationship, create a RelationshipDescriptor and store start and end nodes
                while (result.hasNext()) {
                    Record record = result.next();
                    relationships.add(getRelationshipDescriptor(record));
                    String key = record.get("relType").asString();
                    Set<String> startSet = new HashSet<>(processList(record.get("startLabels").asList()));
                    Set<String> endSet = new HashSet<>(processList(record.get("endLabels").asList()));
                    addSetToMap(startNodes, key, startSet);
                    addSetToMap(endNodes, key, endSet);
                }

                tx.success();
                tx.close();

                // Log the progress
                System.out.println("Model successfully extracted from the database.");
            }
        }
        Model model = new Model(nodes, relationships, startNodes, endNodes);
        return model;
    }
}
