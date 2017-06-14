import org.intermine.neo4j.Neo4jLoaderProperties;
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

    public static void main(String[] args) throws IOException {

        // get the properties from the default file
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();

        // Neo4j setup
        Driver driver = props.getGraphDatabaseDriver();

        // Destroy existing schema
        Neo4jSchemaGenerator.destroySchema(driver);

        // Create schema and get model
        Model model = Neo4jSchemaGenerator.getModel(driver);
        System.out.println(model);

        // Test various model methods
        if(true){
            System.out.println(model.getRelationships("Gene"));
            System.out.println(model.getIncomingRelationships("Gene"));
            System.out.println(model.getOutgoingRelationships("Gene"));
            System.out.println(model.hasNodeRelationship("Gene", "location"));
            System.out.println(model.hasIncomingNodeRelationship("Gene", "location"));
            System.out.println(model.hasOutgoingNodeRelationship("Gene", "location"));
        }

        driver.close();
    }
}
