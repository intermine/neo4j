package org.intermine.neo4j.cypher.constraint;

import org.intermine.neo4j.Neo4jLoaderProperties;
import org.neo4j.driver.v1.Driver;

import java.io.IOException;

/**
 * Stores Node label and Property name for extra value in the LOOKUP Constraint.
 *
 * @author Yash Sharma
 */
public class ExtraValueBag {

    // The label of the node whose properties are matched by the extra value.
    // Generally it is Organism.
    String label;

    // The property which is matched by the extra value.
    // For Organism it is its shortName.
    String property;

    public ExtraValueBag(String label, String property) {
        this.label = label;
        this.property = property;
    }

    /**
     * Reads extra value label & property from neo4jloader.properties, creates an ExtraValueBag
     * object and returns it.
     *
     * @return The ExtraValueBag object
     */
    protected static ExtraValueBag getExtraValueBag()  {
        try {
            Neo4jLoaderProperties neo4jLoaderProperties = new Neo4jLoaderProperties();
            return new ExtraValueBag(neo4jLoaderProperties.getExtraValueLabel(),
                                    neo4jLoaderProperties.getExtraValuePropertyName());

        }
        catch (IOException e){
            System.out.println("Could not read extra value label from neojloader.properties");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the label of the extra value bag
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the property matched by the extra value.
     *
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * Finds out if the Node with disambiguating value is the neighbor of the node on which
     * constraint is applied
     *
     * @param nodeLabel label of the node on which the constraint is applied
     * @return true if the Node with disambiguating value is the neighbor of the node, false otherwise
     * @throws IOException
     */
    protected static boolean isExtraConstraint(String nodeLabel)  {
        try {
            Neo4jLoaderProperties neo4jLoaderProperties = new Neo4jLoaderProperties();
            String neighbourLabel = neo4jLoaderProperties.getExtraValueLabel();
            Driver driver = neo4jLoaderProperties.getGraphDatabaseDriver();

            // TO DO : Restore this return statement when testing with proper IM Neo4j Graph.

            // return Neo4jSchemaGenerator.getModel(driver).labelHasNeighbour(nodeLabel, neighbourLabel);
            return true;
        }
        catch (IOException e){
            System.out.println("Could not read extra value label from neojloader.properties");
            e.printStackTrace();
        }
        return false;
    }
}
