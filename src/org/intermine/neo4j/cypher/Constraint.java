package org.intermine.neo4j.cypher;

import org.intermine.metadata.ConstraintOp;

import java.util.List;

/**
 * Describes a constraint.
 *
 * @author Yash Sharma
 */
public class Constraint {
    List<Component> components;
    ConstraintOp operator;
    String value;

    Constraint(List<Component> components, ConstraintOp Op, String value){
        this.components = components;
        this.operator = Op;
        this.value = value;
    }

    public String toString(){
        return "(" + components + " " + operator + " " + value + ")";
    }

}
