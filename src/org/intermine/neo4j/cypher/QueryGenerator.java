package org.intermine.neo4j.cypher;


import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.pathquery.PathConstraint;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Generates Cypher Queries.
 *
 * @author Yash Sharma
 */
public class QueryGenerator {

    public static String pathQueryToCypher(String input) throws IOException {

        // get the properties from the default file
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();

        // create a PathQuery object using IM Service and input string
        QueryService service = props.getQueryService();
        PathQuery pathQuery = service.createPathQuery(input);

        if(!pathQuery.isValid()){
            System.out.println("Please enter a valid path query.");
            System.exit(0);
        }

        List views = pathQuery.getView();
        System.out.println("Views :\n" + views);

        Map<PathConstraint, String> constraints = pathQuery.getConstraints();
        System.out.println("Constraints :\n" + constraints);

        return "";
    }

}
