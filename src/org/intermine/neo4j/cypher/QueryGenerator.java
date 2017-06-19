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

        // create a Path Query object using IM Service and input string
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

        Set<List<Component>> views = getViewsFromPathQuery(pathQuery);
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

    /**
     * Extracts constraints from the path query in the form of components
     * @param pathQuery the path query object
     * @return A set, each element of which is a constraint object which stores
     *         the path components, operator and the value
     */
    private static Set<Constraint> getConstraintsFromPathQuery(PathQuery pathQuery){
        Map<PathConstraint, String> constraintsMap = pathQuery.getConstraints();
        Set<Constraint> constraints = new HashSet<>(constraintsMap.keySet().size());
        for(PathConstraint pathConstraint : constraintsMap.keySet()){
            Constraint constraint = new Constraint(getComponents(pathConstraint.getPath()),
                                                    pathConstraint.getOp(),
                                                    PathConstraint.getValue(pathConstraint));
            constraints.add(constraint);
        }
        return constraints;
    }

    /**
     * Extracts views from the path query in the form of components
     * @param pathQuery the path query object
     * @return A list of views, each element of which is a list of path components
     */
    private static Set<List<Component>> getViewsFromPathQuery(PathQuery pathQuery){
        List<String> viewsList = pathQuery.getView();
        Set<List<Component>> tokenizedViews = new HashSet<>(viewsList.size());
        for(String view : viewsList){
            tokenizedViews.add(getComponents(view));
        }
        return tokenizedViews;
    }

}
