package org.intermine.neo4j.cypher;

import org.intermine.metadata.Model;
import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.pathquery.*;

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
    public static List<String> getTokensFromPathString(String path){
        StringTokenizer st = new StringTokenizer(path, ".");
        List<String> strings = new ArrayList<>();
        while(st.hasMoreTokens()){
            strings.add(st.nextToken());
        }
        return strings;
    }

    public static String getVariableNameFromPath(Path path){
        // TO DO : Use another way of creating variable names.
        // Underscore separated names may get unnecessarily large.

        return path.toString().toLowerCase().replaceAll("\\.", "_");
    }

    /**
     * Extracts all paths from the path query object
     *
     * @param pathQuery the path query object
     * @return A set containing all the paths in the path query
     */
    public static Set<Path> getAllPaths(PathQuery pathQuery) throws PathException {
        Set<Path> paths = new HashSet<>();
        Model model = null;
        try {
            model = new Neo4jLoaderProperties().getModel();
        } catch (ModelParserException e) {
            e.printStackTrace();
            System.out.println("getAllPaths() method : Could not get model from the XML file.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("getAllPaths() method : Could not get model from the XML file.");
            System.exit(0);
        }

        // Get paths from views
        for (String pathString : pathQuery.getView()) {
            paths.add(new Path(model, pathString));
        }

        // Get paths from constraints
        Set<PathConstraint> pathConstraints = pathQuery.getConstraints().keySet();
        for (PathConstraint pathConstraint : pathConstraints){
            paths.add(new Path(model, pathConstraint.getPath()));

            // Loop constraint has an additional path
            if (pathConstraint instanceof PathConstraintLoop){
                paths.add(new Path(model, PathConstraint.getValue(pathConstraint)));
            }
        }

        // Get paths from sort order
        List<OrderElement> orderElements = pathQuery.getOrderBy();
        for (OrderElement orderElement : orderElements){
            paths.add(new Path(model, orderElement.getOrderPath()));
        }

        return paths;
    }

}
