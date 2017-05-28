package org.intermine.neo4j;

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
public class Neo4jQueryTester {

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

        // Generate and store the schema of the database
        Neo4jSchemaGenerator.generateAndStoreSchema(driver);


        driver.close();
    }
}
