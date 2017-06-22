package org.intermine.neo4j.cypher;

import org.intermine.pathquery.PathConstraint;

/**
 * Describes a constraint in Cypher Query.
 *
 * @author Yash Sharma
 */
public class Constraint {

    private String propertyKey;

    private String variableName;

    private String operator;

    private String value;

    Constraint(PathConstraint pathConstraint, PathTree pathTree){
        TreeNode treeNode = pathTree.getTreeNode(pathConstraint.getPath());
        this.variableName = treeNode.getParent().getVariableName();
        this.propertyKey = treeNode.getGraphicalName();
        this.operator = OperatorConverter.getCypherOperator(pathConstraint.getOp());
        this.value = OperatorConverter.getCypherValue(pathConstraint.getOp(),
                                                        PathConstraint.getValue(pathConstraint));
    }

    public static boolean isConstraintValid(PathConstraint pathConstraint, PathTree pathTree){
        TreeNode treeNode = pathTree.getTreeNode(pathConstraint.getPath());
        if (treeNode.getTreeNodeType() != TreeNodeType.PROPERTY){
            return false;
        }
        return true;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString(){
        return getVariableName() + "." +
                getPropertyKey() + " " +
                getOperator() + " " +
                getValue();
    }

}
