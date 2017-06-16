package org.intermine.neo4j.cypher;

import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.pathquery.PathConstraint;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;

import java.io.IOException;
import java.util.*;

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

        List<List<Component>> views = getViewsFromPathQuery(pathQuery);
        System.out.println("Views :\n" + views);

        Set<Constraint> constraints = getConstraintsFromPathQuery(pathQuery);
        System.out.println("Constraints :\n" + constraints);
        return "";
    }

    /**
     * Get Components after tokenization of a string
     * @param string A string which needs to be tokenized
     * @return List of all components inside the string
     */
    private static List<Component> getComponents(String string){
            StringTokenizer st = new StringTokenizer(string, ".");
            List<Component> components = new ArrayList<>();
            while(st.hasMoreTokens()){
                components.add(new Component(st.nextToken()));
            }
            return components;
    }

    private static Set<Constraint> getConstraintsFromPathQuery(PathQuery pathQuery){
        Set<Constraint> constraints = new HashSet<>();
        for(PathConstraint pathConstraint : pathQuery.getConstraints().keySet()){
            Constraint constraint = new Constraint(getComponents(pathConstraint.getPath()),
                                                    pathConstraint.getOp(),
                                                    PathConstraint.getValue(pathConstraint));
            constraints.add(constraint);
        }
        return constraints;
    }

    private static List<List<Component>> getViewsFromPathQuery(PathQuery pathQuery){
        List<List<Component>> tokenizedViews = new ArrayList<>();
        for(String view : pathQuery.getView()){
            tokenizedViews.add(getComponents(view));
        }
        return tokenizedViews;
    }

}
