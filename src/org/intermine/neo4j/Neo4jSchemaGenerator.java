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
    public static void generateAndStoreSchema(Driver driver){
        // Create three initial nodes of the Metagraph : Root, Node Owner & Rel Owner.
        createInitialNodes(driver);

        // Creates Representative NodeType & RelType nodes for each relationship & node and
        // Add required relationships between them.
        mapConnectedNodes(driver);

        // Create Representative (Admin) nodes for all disconnected nodes.
        mapDisconnectedNodes(driver);
    }

    /**
     * Create three initial nodes of the Metagraph : Root, Node Owner & Rel Owner.
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
                System.out.println("Schema Progress : Initial Nodes Created - Root Node, NodeTypeOwner & RelTypeOwner.");
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
            query = query + ", Labels: $startNodeLabels";
        }
        if (!startNodeKeys.isNull() && !startNodeKeys.isEmpty()){
            query = query + ", Properties: $startNodeKeys";
        }
        query = query + "}) MERGE (nodeOwner)-[:OWNS]->(m:Metagraph { metaType: 'NodeType'";
        if (!endNodeLabels.isNull() && !endNodeLabels.isEmpty()){
            query = query + ", Labels: $endNodeLabels";
        }
        if (!endNodeKeys.isNull() && !endNodeKeys.isEmpty()){
            query = query + ", Properties: $endNodeKeys";
        }
        query = query + "}) MERGE (relOwner)-[:OWNS]->(r:Metagraph { metaType: 'RelType'";
        if (!relType.isNull()){
            query = query + ", type: $relType";
        }
        if (!relKeys.isNull() && !relKeys.isEmpty()){
            query = query + ", Properties: $relKeys";
        }
        query = query + "}) MERGE (n)<-[:StartNodeType]-(r)-[:EndNodeType]->(m)";

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
        HashMap<String, Object> params = new HashMap<>();
        params.put("relType", record.get("type(r)").asString());
        params.put("relKeys", processList(record.get("keys(r)").asList()));
        params.put("startNodeLabels", processList(record.get("labels(n)").asList()));
        params.put("startNodeKeys", processList(record.get("keys(n)").asList()));
        params.put("endNodeLabels", processList(record.get("labels(m)").asList()));
        params.put("endNodeKeys", processList(record.get("keys(m)").asList()));

        return params;
    }

    /**
     * Maps all the disconnected nodes of the database to the schema.
     * @param driver Neo4j Java Driver instance.
     */
    private static void mapDisconnectedNodes(Driver driver){
    	// TO DO : Write this function
    }

}
