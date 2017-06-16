package org.intermine.neo4j.cypher;


import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.pathquery.PathConstraint;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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

        List<List<Component>> tokenizedViews = getTokenizedViews(pathQuery);
        System.out.println("Views :\n" + tokenizedViews);



        Map<PathConstraint, String> constraints = pathQuery.getConstraints();
        System.out.println("Constraints :\n" + constraints);

        return "";
    }

    /**
     * Tokenizes all the views of a path query
     * @param pathQuery The path query object, views of which has to be tokenized
     * @return All views after tokenization.
     */
    private static List<List<Component>> getTokenizedViews(PathQuery pathQuery){
        List<String> views = pathQuery.getView();
        List<List<Component>> tokenizedViews = new ArrayList<>();
        for (String view : views){
            StringTokenizer st = new StringTokenizer(view, ".");
            List<Component> tokenizedView = new ArrayList<>();
            while(st.hasMoreTokens()){
                tokenizedView.add(new Component(st.nextToken()));
            }
            tokenizedViews.add(tokenizedView);
        }
        return tokenizedViews;
    }
}
