package org.intermine.neo4j.cypher;

import org.intermine.metadata.ConstraintOp;

import java.util.List;

/**
 * Describes a constraint.
 *
 * @author Yash Sharma
 */
public class Constraint {
    private List<Component> components;
    private ConstraintOp operator;
    private String value;

    Constraint(List<Component> components, ConstraintOp Op, String value){
        this.components = components;
        // Perhaps we would also need to convert IM operators to Neo4j operators
        this.operator = Op;
        this.value = value;
    }

    public String toString(){
        return "(" + components + " " + operator + " " + value + ")";
    }

}
