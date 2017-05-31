package org.intermine.neo4j;

import java.util.List;

/**
 * Describes a NodeType node of the Neo4j metagraph.
 *
 * @author Yash Sharma
 */
public class NodeDescriptor {
    private List<String> labels;
    private List<String> properties;

    public NodeDescriptor(List<String> labels, List<String> properties) {
        this.labels = labels;
        this.properties = properties;
    }

    public NodeDescriptor() {
        this.labels = null;
        this.properties = null;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
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
