package org.intermine.neo4j.metadata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Describes a Neo4j graph data model. It gives access to various
 * {@link NodeTypeDescriptor NodeType} and {@link RelTypeDescriptor RelType}
 * nodes and their relationships within the metagraph. Metagraph structure is described at
 * https://github.com/intermine/neo4j/blob/dev/metadata.md .
 * <p>
 * You can generate an instance of the Model class for a given Neo4j database by calling <code>Neo4jSchemaGenerator.getModel()</code>
 * method. An instance of Model class provides a number of methods to query the data model. These methods
 * can be used to query properties of nodes and relationships and to query how nodes are related
 * to each other.
 * </p>
 *
 * @author Yash Sharma
 */
public class Model {

    private Set<NodeTypeDescriptor> nodes;
    private Set<RelTypeDescriptor> relationships;

    // Maps Relationship Type to the associated Sets of Node Labels
    private Map<String, Set<Set<String>>> startNodes;
    private Map<String, Set<Set<String>>> endNodes;

    // Stores incoming & outgoing relationships of each NodeType
    private Map<Set<String>, Set<String>> incomingRelationships;
    private Map<Set<String>, Set<String>> outgoingRelationships;

    public Model(Set<NodeTypeDescriptor> nodes, Set<RelTypeDescriptor> relationships, Map<String, Set<Set<String>>> startNodes, Map<String, Set<Set<String>>> endNodes) {
        this.nodes = nodes;
        this.relationships = relationships;
        this.startNodes = startNodes;
        this.endNodes = endNodes;

        incomingRelationships = new HashMap<>();
        outgoingRelationships = new HashMap<>();

        for (String key : startNodes.keySet()){
            for (Set<String> set : startNodes.get(key)){
                Set<String> outRelationships;
                if(outgoingRelationships.containsKey(set)){
                    outRelationships = outgoingRelationships.get(set);
                }
                else{
                    outRelationships = new HashSet<>();
                }
                outRelationships.add(key);
                outgoingRelationships.put(set, outRelationships);
            }
        }

        for (String key : endNodes.keySet()){
            for (Set<String> set : endNodes.get(key)){
                Set<String> inRelationships;
                if(incomingRelationships.containsKey(set)){
                    inRelationships = incomingRelationships.get(set);
                }
                else{
                    inRelationships = new HashSet<>();
                }
                inRelationships.add(key);
                incomingRelationships.put(set, inRelationships);
            }
        }
    }

    public Model() {
        this.nodes = null;
        this.relationships = null;
        this.startNodes = null;
        this.endNodes = null;
        this.incomingRelationships = null;
        this.outgoingRelationships = null;
    }

    /**
    * Generates a string containing all the information in the instance of a Model class.
    */
    @Override
    public String toString(){
        return "\nNodes :\n" + nodes.toString() + "\n\n" +
                "Relationships :\n" + relationships.toString() + "\n\n" +
                "Start Nodes :\n" + startNodes.toString() + "\n\n" +
                "End Nodes :\n" + endNodes.toString() + "\n\n" +
                "Incoming Relationships :\n" + incomingRelationships.toString() + "\n\n" +
                "Outgoing Relationships :\n" + outgoingRelationships.toString() + "\n\n";
    }

    /**
    * Find out if there exists a node labelled with the given label.
    * @param label the given node label
    * @return true if there exists a node labelled with the given label, false otherwise
    */
    public boolean hasNodeLabel(String label){
        for (NodeTypeDescriptor node : nodes){
            if(node.hasLabel(label)){
                return true;
            }
        }
        return false;
    }

    /**
     * Find out if there exists a relationship of the given type.
     * @param type the given relationship type
     * @return true if there exists a relationship with the given type, false otherwise
     */
    public boolean hasRelationshipType(String type){
        for (RelTypeDescriptor relationship : relationships){
            if(relationship.hasType(type)){
                return true;
            }
        }
        return false;
    }

    /**
     * Find out if there exists a node among all the nodes labelled with the given label that
     * contains the given property.
     * @param label the given node label
     * @param property the given property
     * @return true if there exists a node among all the nodes with the given label that
     * contains the given property, false otherwise
     */
    public boolean labelHasProperty(String label, String property){
        for (NodeTypeDescriptor node : nodes){
            if(node.hasLabel(label) && node.hasProperty(property)){
                return true;
            }
        }
        return false;
    }

    /**
     * Find out if there exists a relationship among all the relationships with the given type
     * that contains the given property.
     * @param type the given relationship type
     * @param property the given property
     * @return true if there exists a relationship among all the relationships with the given type
     *         that contains the given property, false otherwise
     */
    public boolean relationshipHasProperty(String type, String property){
        for (RelTypeDescriptor relationship : relationships){
            if(relationship.hasType(type) && relationship.hasProperty(property)){
                return true;
            }
        }
        return false;
    }

    /**
     * Get all the relationships (both incoming and outgoing) associated with the given label.
     * @param label the given node label
     * @return all the relationships associated with the given label
     */
    public Set<String> getRelationships(String label){
        Set<String> relationships = new HashSet<>();
        relationships.addAll(getIncomingRelationships(label));
        relationships.addAll(getOutgoingRelationships(label));
        return relationships;
    }

    /**
     * Get the incoming relationships associated with the given label.
     * @param label the given node label
     * @return all the incoming relationships associated with the given label
     */
    public Set<String> getIncomingRelationships(String label){
        Set<String> relationships = new HashSet<>();
        for (Set<String> nodeLabels : incomingRelationships.keySet()){
            if(nodeLabels.contains(label)){
                for (String str : incomingRelationships.get(nodeLabels)){
                    relationships.add(str);
                }
            }
        }
        return relationships;
    }

    /**
     * Get the outgoing relationships associated with the given label.
     * @param label the given node label
     * @return all the outgoing relationships associated with the given label
     */
    public Set<String> getOutgoingRelationships(String label){
        Set<String> relationships = new HashSet<>();
        for (Set<String> nodeLabels : outgoingRelationships.keySet()){
            if(nodeLabels.contains(label)){
                for (String str : outgoingRelationships.get(nodeLabels)){
                    relationships.add(str);
                }
            }
        }
        return relationships;
    }

    /**
     * Get all the properties that exist among all the nodes labelled with the given label.
     * @param label the given node label
     * @return all the properties that exist among all the nodes labelled with the given label
     */
    public Set<String> getLabelProperties(String label){
        Set<String> properties = new HashSet<>();
        for (NodeTypeDescriptor node : nodes){
            if(node.hasLabel(label)){
                for (String str : node.getProperties()){
                    properties.add(str);
                }
            }
        }
        return properties;
    }

    /**
     * Get all the properties that exist among all the relationships of the given type.
     * @param type the given relationship type
     * @return all the properties that exist among all the relationships of the given type.
     */
    public Set<String> getRelationshipProperties(String type){
        Set<String> properties = new HashSet<>();
        for (RelTypeDescriptor relationship : relationships){
            if(relationship.hasType(type)){
                for (String str : relationship.getProperties()){
                    properties.add(str);
                }
                break;
            }
        }
        return properties;
    }

    /**
     * Get all possible combinations of labels that exist as a Start Node of the given relationship.
     * @param type the given relationship type
     * @return a set containing many sets, each of which is a possible combinations of labels
     *        that exist as a Start Node of the given relationship.
     */
    public Set<Set<String>> getStartLabelsOfRelationship(String type){
        return startNodes.get(type);
    }

    /**
     * Get all possible combinations of labels that exist as a End Node of the given relationship.
     * @param type the given relationship type
     * @return a set containing many sets, each of which is a possible combinations of labels
     *        that exist as a End Node of the given relationship.
     */
    public Set<Set<String>> getEndLabelsOfRelationship(String type){
        return endNodes.get(type);
    }

    /**
     * Finds out if there is a relationship (incoming OR outgoing) of the given type associated
     * with any node labelled with the given label.
     * @param label the given node label
     * @param type the given relationship type
     * @return true if there is a relationship (incoming OR outgoing) of the given type associated
     *         with any node labelled with the given label, false otherwise
     */
    public boolean hasNodeRelationship(String label, String type){
        return (hasIncomingNodeRelationship(label, type) || hasOutgoingNodeRelationship(label, type));
    }

    /**
     * Finds out if there is an incoming relationship of the given type associated
     * with any node labelled with the given label.
     * @param label the given node label
     * @param type the given relationship type
     * @return true if there is an incoming relationship of the given type associated
     *        with any node labelled with the given label, false otherwise
     */
    public boolean hasIncomingNodeRelationship(String label, String type){
        for (Set<String> nodeLabels : incomingRelationships.keySet()){
            if(nodeLabels.contains(label)){
                for (String str : incomingRelationships.get(nodeLabels)){
                    if(str.equals(type)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Finds out if there is an outgoing relationship of the given type associated
     * with any node labelled with the given label.
     * @param label the given node label
     * @param type the given relationship type
     * @return true if there is an outgoing relationship of the given type associated
     *        with any node labelled with the given label, false otherwise
     */
    public boolean hasOutgoingNodeRelationship(String label, String type){
        for (Set<String> nodeLabels : outgoingRelationships.keySet()){
            if(nodeLabels.contains(label)){
                for (String str : outgoingRelationships.get(nodeLabels)){
                    if(str.equals(type)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Finds out if two labels are connected to each other
     * @param nodeLabel the label of the node
     * @param neighbourLabel the label of the neighbouring node
     * @return true if they are neighbours, false otherwise
     */
    public boolean labelHasNeighbour(String nodeLabel, String neighbourLabel){
        System.out.println(nodeLabel + " " + neighbourLabel);
        for (RelTypeDescriptor relType : relationships ){
            String key = relType.getType();
            if ( (hasIncomingNodeRelationship(nodeLabel, key))&&(hasOutgoingNodeRelationship(neighbourLabel, key))
                    ||
                    (hasIncomingNodeRelationship(neighbourLabel, key)&&(hasOutgoingNodeRelationship(nodeLabel, key))) ){
                return true;
            }
        }
        return false;
    }

}
