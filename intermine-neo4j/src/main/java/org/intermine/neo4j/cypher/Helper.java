package org.intermine.neo4j.cypher;

import apoc.coll.Coll;
import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.pathquery.OrderElement;
import org.intermine.pathquery.PathConstraint;
import org.intermine.pathquery.PathConstraintLoop;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.core.ContentType;
import org.intermine.webservice.client.core.Request;
import org.intermine.webservice.client.lists.ItemList;
import org.intermine.webservice.client.results.Item;
import org.intermine.webservice.client.services.ListService;
import org.intermine.webservice.client.services.QueryService;
import org.intermine.webservice.client.util.HttpConnection;

import java.io.IOException;
import java.util.*;


/**
 * Stores various helper methods.
 *
 * @author Yash Sharma
 */
public class Helper {

    /**
     * Prints all the contraints of a Path Query to the console
     *
     * @param pathQuery the given path query
     */
    public static void printConstraints(PathQuery pathQuery){
        for (PathConstraint pathConstraint : pathQuery.getConstraints().keySet()){
            System.out.println("Path : " + pathConstraint.getPath());
            System.out.println("Operator : " + pathConstraint.getOp());
            System.out.println("Value : " + PathConstraint.getValue(pathConstraint));
            System.out.println("Values : " + PathConstraint.getValues(pathConstraint));
            System.out.println("Extra value : " + PathConstraint.getExtraValue(pathConstraint));
        }
    }

    /**
     * Add quotes to a string. For example, "Gene" becomes "'Gene'"
     *
     * @param string    the given string
     * @return string   after adding quotes
     */
    public static String quoted(String string){
        return "'" + string + "'";
    }

    /**
     * Adds quotes to all the strings stored in the collection.
     * For example, ["Gene", "Protein"] become ["'Gene'", "'Protein'"]
     *
     * @param collection The given collection of strings
     * @return           A set containing strings after adding quotes.
     */
    public static Set<String> quoted(Collection<String> collection){
        Set<String> quotedSet = new HashSet<>();
        for (String val: collection){
            quotedSet.add(Helper.quoted(val));
        }
        return quotedSet;
    }

    /**
     * Finds out if the given string represents a number or not
     *
     * @param str The given string
     * @return true if the given string represents a number, false otherwise
     */
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    /**
     * Get all tokens from a path
     *
     * @param path A path which contains many tokens separated by dots
     * @return List of all tokens of the path
     */
    public static List<String> getTokensFromPath(String path){
        StringTokenizer st = new StringTokenizer(path, ".");
        List<String> strings = new ArrayList<>();
        while(st.hasMoreTokens()){
            strings.add(st.nextToken());
        }
        return strings;
    }

    /**
     * Extracts all paths from the path query object
     *
     * @param pathQuery the path query object
     * @return A set containing all the paths in the path query
     */
    public static Set<String> getAllPaths(PathQuery pathQuery){
        Set<String> paths = new HashSet<>();

        // Get paths from views
        paths.addAll(pathQuery.getView());

        // Get paths from constraints
        Set<PathConstraint> pathConstraints = pathQuery.getConstraints().keySet();
        for (PathConstraint pathConstraint : pathConstraints){
            paths.add(pathConstraint.getPath());

            // Loop constraint has an additional path
            if (pathConstraint instanceof PathConstraintLoop){
                paths.add(PathConstraint.getValue(pathConstraint));
            }
        }

        // Get paths from sort order
        List<OrderElement> orderElements = pathQuery.getOrderBy();
        for (OrderElement orderElement : orderElements){
            paths.add(orderElement.getOrderPath());
        }

        return paths;
    }

}
