package org.intermine.neo4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.Properties;

import org.intermine.metadata.InterMineModelParser;
import org.intermine.metadata.Model;
import org.intermine.metadata.ModelParserException;
import org.intermine.webservice.client.core.ServiceFactory;
import org.intermine.webservice.client.services.QueryService;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

/**
 * Simple storage class with methods to return various InterMine and Neo4j services.
 */
public class Neo4jLoaderProperties {

    public static final String DEFAULT_PROPERTIES_FILE = "neo4jloader.properties";

    // NOTE: leaving public to avoid writing getters and setters for now
    public String intermineServiceUrl;
    public String neo4jUrl;
    public String neo4jUser;
    public String neo4jPassword;
    public boolean verbose;
    public int maxRows;
    public String dataModelFilename;

    /**
     * Default constructor, loads properties from DEFAULT_PROPERTIES_FILE
     */
    public Neo4jLoaderProperties() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(DEFAULT_PROPERTIES_FILE));
        load(props);
    }

    /**
     * Constructor which loads properties from a given filename
     */
    public Neo4jLoaderProperties(String filename) throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(filename));
        load(props);
    }

    /**
     * Set the various properties from a Properties object
     */
    void load(Properties props) throws FileNotFoundException, IOException {
        intermineServiceUrl = props.getProperty("intermine.service.url");
        neo4jUrl = props.getProperty("neo4j.url");
        neo4jUser = props.getProperty("neo4j.user");
        neo4jPassword = props.getProperty("neo4j.password");
        verbose = Boolean.parseBoolean(props.getProperty("verbose"));
        maxRows = Integer.parseInt(props.getProperty("max.rows"));
        dataModelFilename = props.getProperty("data.model.file");
    }

    /**
     * Return a Neo4j graph database driver
     */
    public Driver getGraphDatabaseDriver() {
        return GraphDatabase.driver(neo4jUrl, AuthTokens.basic(neo4jUser, neo4jPassword));
    }

    /**
     * Return an InterMine QueryService
     */
    public QueryService getQueryService() {
        return new ServiceFactory(intermineServiceUrl).getQueryService();
    }

    /**
     * Return an InterMine Model
     */
    public Model getModel() throws FileNotFoundException, ModelParserException {
        return new InterMineModelParser().process(new InputStreamReader(new FileInputStream(dataModelFilename)));
    }

}
