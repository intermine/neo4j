package org.intermine.neo4j;

import java.util.Map;
import java.util.Set;

/**
 * Describe a Neo4j graph model.  Gives access to NodeDescriptor & RelationshipDescriptor
 * descriptors that exist within the model.
 *
 * @author Yash Sharma
 */
public class Model {

    Set<NodeDescriptor> nodes;
    Set<RelationshipDescriptor> relationships;

    // Maps Relationship Type to the associated Sets of Node Labels
    Map<String, Set<Set<String>>> startNodes;
    Map<String, Set<Set<String>>> endNodes;

    public Model(Set<NodeDescriptor> nodes, Set<RelationshipDescriptor> relationships, Map<String, Set<Set<String>>> startNodes, Map<String, Set<Set<String>>> endNodes) {
        this.nodes = nodes;
        this.relationships = relationships;
        this.startNodes = startNodes;
        this.endNodes = endNodes;
    }

    public Model() {
        this.nodes = null;
        this.relationships = null;
        this.startNodes = null;
        this.endNodes = null;
    }

    public String toJSON(){
        // TO DO : Write this method.
        return "";
    }

    @Override
    public String toString(){
        return "Nodes :\n" + nodes.toString() + "\n\n" +
                "Relationships :\n" + relationships.toString() + "\n\n" +
                "Start Nodes :\n" + startNodes.toString() + "\n\n" +
                "End Nodes :\n" + endNodes.toString() + "\n\n";
    }
}
