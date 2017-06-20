package org.intermine.neo4j.cypher;

import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;

import java.io.IOException;

/**
 * Generates Cypher Queries.
 *
 * @author Yash Sharma
 */
public class QueryGenerator {

    public static String pathQueryToCypher(String input) throws IOException {

        // Get the properties from the default file
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();

        // Create a Path Query object using IM Service and the input string
        QueryService service = props.getQueryService();
        PathQuery pathQuery = service.createPathQuery(input);

        if(!pathQuery.isValid()){
            System.out.println("Please enter a valid path query.");
            System.exit(0);
        }

        // We need to call getQueryToExecute() first.  For template queries this gets a query that
        // excludes any optional constraints that have been switched off.  A normal PathQuery is
        // unchanged.
        pathQuery = pathQuery.getQueryToExecute();

        PathTree pathTree = new PathTree(pathQuery);
        pathTree.serialize();

        return pathTree.toCypher();
    }

}
