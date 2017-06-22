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

    /**
     * Finds out if the given PathConstraint is valid as per the given per the given PathTree
     * not by checking if the constrained object is represented by a Graph Property.
     *
     * @param pathConstraint The PathConstraint object
     * @param pathTree The PathTree
     * @return true if the given PathConstraint is valid as per the given per the given PathTree,
     *          false otherwise
     */
    public static boolean isConstraintValid(PathConstraint pathConstraint, PathTree pathTree){
        TreeNode treeNode = pathTree.getTreeNode(pathConstraint.getPath());
        if (treeNode.getTreeNodeType() != TreeNodeType.PROPERTY){
            return false;
        }
        return true;
    }

    /**
     * Gets the property key used in the constraint
     *
     * @return the property key
     */
    public String getPropertyKey() {
        return propertyKey;
    }

    /**
     * Gets the variable name used in the constraint
     *
     * @return the variable name
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Gets the operator used in the constraint
     *
     * @return the variable name
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Gets the value used in the constraint
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Converts a cypher constraint to its string representation
     *
     * @return the string representation of the constraint
     */
    @Override
    public String toString(){
        return getVariableName() + "." +
                getPropertyKey() + " " +
                getOperator() + " " +
                getValue();
    }

}
