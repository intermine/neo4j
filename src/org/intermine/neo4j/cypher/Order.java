package org.intermine.neo4j.cypher;

import org.intermine.pathquery.OrderElement;

/**
 * Represents a order in cypher.
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

    public String getPropertyKey() {
        return propertyKey;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getDirection() {
        return direction;
    }

}
