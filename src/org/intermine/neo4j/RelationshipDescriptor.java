package org.intermine.neo4j;

import java.util.List;

/**
 * Describes a RelType node of the Neo4j metagraph.
 *
 * @author Yash Sharma
 */
public class RelationshipDescriptor {
    private String type;
    private List<String> properties;

    public RelationshipDescriptor(String type, List<String> properties) {
        this.type = type;
        this.properties = properties;
    }

    public RelationshipDescriptor() {
        this.type = null;
        this.properties = null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public void addProperty(String property){
        this.properties.add(property);
    }

    public void removeProperty(String property){
        this.properties.remove(property);
    }
}
