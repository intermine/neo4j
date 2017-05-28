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
        mapDisconnectedNodes(driver);
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

    }

    private static void mapDisconnectedNodes(Driver driver){

    }
}
