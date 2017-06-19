package org.intermine.neo4j.cypher;

import static org.intermine.neo4j.cypher.OntologyConverter.convertInterMineToNeo4j;
import static org.intermine.neo4j.cypher.OntologyConverter.getGraphComponentType;

/**
 * Describes a component used in the Path Query.
 *
 * @author Yash Sharma
 */
public class Component {
    private String graphicalName;
    private ComponentType componentType;

    Component(String name){
        this.graphicalName = convertInterMineToNeo4j(name);
        componentType = getGraphComponentType(name);
    }

    public void setGraphicalName(String graphicalName) {
        this.graphicalName = graphicalName;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public String getGraphicalName() {
        return graphicalName;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public String toString(){
        return graphicalName + " : " + componentType.name();
    }

}
