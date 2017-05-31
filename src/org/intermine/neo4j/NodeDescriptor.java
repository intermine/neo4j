package org.intermine.neo4j;

import java.util.HashSet;
import java.util.Set;

/**
 * Describes a NodeType node of the Neo4j metagraph.
 *
 * @author Yash Sharma
 */
public class NodeDescriptor {

    private Set<String> labels;
    private Set<String> properties;

    public NodeDescriptor(Set<String> labels, Set<String> properties) {
        this.labels = labels;
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "NodeDescriptor{" +
                "labels=" + labels +
                ", properties=" + properties +
                '}';
    }

    public NodeDescriptor() {
        this.labels = new HashSet<>();
        this.properties = new HashSet<>();
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }

    public Set<String> getProperties() {
        return properties;
    }

    public void setProperties(Set<String> properties) {
        this.properties = properties;
    }

    public void addLabel(String label){
        this.labels.add(label);
    }

    public void removeLabel(String label){
        this.labels.remove(label);
    }

    public void addProperty(String property){
        this.properties.add(property);
    }

    public void removeProperty(String property){
        this.properties.remove(property);
    }

}
