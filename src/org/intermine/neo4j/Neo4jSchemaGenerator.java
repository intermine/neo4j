package org.intermine.neo4j;

import org.neo4j.driver.v1.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates schema of a given Neo4j database and stores it in the database itself.
 * All nodes of describing the schema are assigned a "Metagraph" label.
 * Metagraph structure described at https://gist.github.com/yasharmaster/8071e53c500081660b9e5c203b913b6d
 *
 * @author Yash Sharma
 */
public class Neo4jSchemaGenerator {

    public static void generateAndStoreSchema(Driver driver){
        // Create three initial nodes of the Metagraph : Root, Node Owner & Rel Owner
        createInitialNodes(driver);

        // Creates Representative NodeType & RelType nodes for each relationship & node and
        // Add required relationships between them.
        mapConnectedNodes(driver);

        // Create Representative (Admin) nodes for all disconnected nodes
//        mapDisconnectedNodes(driver);
    }

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
                System.out.println("Initial Nodes Created - Root Node, NodeTypeOwner & RelTypeOwner.");
            }
        }
    }

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
                    Map<String, Object> params = getQueryParams(record);
                    String writeQuery = getQuery();
                    tx.run(writeQuery, params);
                }
                tx.success();
                tx.close();
                // Log the progress
                System.out.println("Mapped Connected Nodes.");
            }
        }
    }

    private static String getQuery(){
        String query = "MATCH (nodeOwner:Metagraph { metaType: 'NodeTypeOwner' }), " +
                        "(relOwner:Metagraph { metaType: 'RelTypeOwner' }) " +
                        "MERGE (nodeOwner)-[:OWNS]->(n:Metagraph { metaType: 'NodeType', " +
                        "labels: '$startNodeLabels', properties: '$startNodeProperties'}) " +
                        "MERGE (nodeOwner)-[:OWNS]->(m:Metagraph { metaType: 'NodeType', " +
                        "labels: '$endNodeLabels', properties: '$endNodeProperties'}) " +
                        "MERGE (relOwner)-[:OWNS]->(r:Metagraph { metaType: 'RelType', " +
                        "type: '$relType', properties: '$relProperties'}) " +
                        "MERGE (n)<-[:StartNodeType]-(r)-[:EndNodeType]->(m)";
        return query;
    }

    private static HashMap<String, Object> getQueryParams(Record record){
        HashMap<String, Object> params = new HashMap<>();
        params.put("relType", record.get("type(r)"));
        params.put("relProperties", record.get("key(r)"));
        params.put("startNodeLabels", record.get("labels(n)"));
        params.put("startNodeProperties", record.get("key(n)"));
        params.put("endNodeLabels", record.get("labels(m)"));
        params.put("endNodeProperties", record.get("key(m)"));
        return params;
    }

    private static void mapDisconnectedNodes(Driver driver){
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run("MATCH (n)-[r]-(m) " +
                                                "RETURN DISTINCT labels(n),type(r),labels(m)");
                while (result.hasNext()) {
                    Record record = result.next();
                    System.out.println(record.get("r").asList());
                }
                tx.success();
                tx.close();
            }
        }
    }
}
