package org.intermine.neo4j.cypher;

import org.intermine.metadata.ConstraintOp;

import java.util.List;

/**
 * Describes a constraint.
 *
 * @author Yash Sharma
 */
public class Constraint {
    private List<TreeNode> treeNodes;
    private ConstraintOp operator;
    private String value;

    Constraint(List<TreeNode> treeNodes, ConstraintOp Op, String value){
        this.treeNodes = treeNodes;
        // Perhaps we would also need to convert IM operators to Neo4j operators down the line
        this.operator = Op;
        this.value = value;
    }

    public String toString(){
        return "(" + treeNodes + " " + operator + " " + value + ")";
    }

}
