package org.intermine.neo4j.cypher;

import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.neo4j.metadata.Neo4jSchemaGenerator;
import org.neo4j.driver.v1.Driver;

import java.io.IOException;

/**
 * Stores Node label and Property name for extra value in the LOOKUP Constraint.
 *
 * @author Yash Sharma
 */
public class ExtraValueBag {
    String label;

    String property;

    public ExtraValueBag(String label, String property) {
        this.label = label;
        this.property = property;
    }

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

    public String getLabel() {
        return label;
    }

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

            // To check of extra constraint is valid, we need to access the data model.
            // To access the data model we need to access the Neo4j database.
            // It takes a lot of time, so to avoid it we are returning true by default, for now.

//            return Neo4jSchemaGenerator.getModel(driver).labelHasNeighbour(nodeLabel, neighbourLabel);
            return true;
        }
        catch (IOException e){
            System.out.println("Could not read extra value label from neojloader.properties");
            e.printStackTrace();
        }
        return false;
    }
}
