import org.intermine.neo4j.metadata.Model;
import org.intermine.neo4j.metadata.Neo4jSchemaGenerator;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Connects to a Neo4j database, generates and stores the schema
 * and validates some sample cypher queries against it.
 *
 * @author Yash Sharma
 */
public class TestSchemaGenerator {

    static final String PROPERTIES_FILE = "neo4jloader.properties";

    public static void main(String[] args) throws IOException {
        // Load parameters from neo4jloader.properties
        Properties props = new Properties();
        props.load(new FileInputStream(PROPERTIES_FILE));
        String neo4jUrl = props.getProperty("neo4j.url");
        String neo4jUser = props.getProperty("neo4j.user");
        String neo4jPassword = props.getProperty("neo4j.password");

        // Neo4j setup
        Driver driver = GraphDatabase.driver(neo4jUrl, AuthTokens.basic(neo4jUser, neo4jPassword));

        // Destroy existing schema
        Neo4jSchemaGenerator.destroySchema(driver);

        // Create schema and get model
        Model model = Neo4jSchemaGenerator.getModel(driver);
        System.out.println(model);

        // Test various model methods
        System.out.println(model.hasRelationshipType("ACTED_IN"));
        System.out.println(model.hasNodeLabel("Person"));

        System.out.println(model.getLabelProperties("Person"));
        System.out.println(model.getRelationshipProperties("ACTED_IN"));

        System.out.println(model.getIncomingRelationships("Person"));
        System.out.println(model.getOutgoingRelationships("Person"));

        System.out.println(model.labelHasProperty("Person", "name"));
        System.out.println(model.relationshipHasProperty("Person", "roles"));

        driver.close();
    }
}
