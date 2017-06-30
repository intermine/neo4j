package org.intermine.neo4j.cypher;

import org.intermine.neo4j.cypher.tree.TreeNodeType;

/**
 * Handles InterMine to Neo4j ontology conversion.
 *
 * @author Yash Sharma
 */
public class OntologyConverter {

    /**
     * Converts an InterMine name to corresponding Neo4j name.
     *
     * @param name the InterMine name
     * @return the Neo4j name
     */
    public static String convertInterMineToNeo4j(String name){
        String graphName = name;
        // TO DO: Write Conversion from InterMine Namespace to Neo4j Namespace
        // For nodes, return label
        // For relationships, return type
        // For properties, return property name
        return graphName;
    }

    /**
     * Tells whether the component is a node, relationship or a property.
     *
     * @param name name of the component
     * @return type of the component
     */
    public static TreeNodeType getGraphComponentType(String name){
        // TO DO: Cover all cases (probably using an external resource)
        switch (name){
            // Everything is a Graph Node for now
            default:
                return TreeNodeType.NODE;
        }
    }
}
