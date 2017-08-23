package org.intermine.neo4j.cypher;

import org.intermine.neo4j.cypher.tree.PathTree;
import org.intermine.neo4j.cypher.tree.TreeNode;
import org.intermine.pathquery.OrderElement;

/**
 * Represents an order in the cypher query. It is used for generating the ORDER BY clause.
 * Each Order object is written in the form - variableName.propertyKey DIRECTION.
 * For example, "gene.primaryIdentifier DESC".
 *
 * @author Yash Sharma
 */
public class Order {

    private String propertyKey;

    private String variableName;

    private String direction;

    Order(OrderElement orderElement, PathTree pathTree){
        TreeNode treeNode = pathTree.getTreeNode(orderElement.getOrderPath());
        propertyKey = treeNode.getGraphicalName();
        // Store variableName of parent TreeNode
        variableName = treeNode.getParent().getVariableName();
        direction = orderElement.getDirection().toString();
    }

    @Override
    public String toString() {
        return variableName + "." +
                propertyKey + " " +
                direction;
    }

    /**
     * Returns the property key of the Order object.
     *
     * @return The property key as String.
     */
    public String getPropertyKey() {
        return propertyKey;
    }

    /**
     * Returns the variable name of the Order object.
     *
     * @return The variable name as String.
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Returns the direction of the Order object. eg. ASC, DESC.
     *
     * @return The direction as String.
     */
    public String getDirection() {
        return direction;
    }

}
