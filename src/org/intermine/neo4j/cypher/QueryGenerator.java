package org.intermine.neo4j.cypher;

import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.pathquery.PathConstraint;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;

import java.io.IOException;

/**
 * Generates Cypher Queries.
 *
 * @author Yash Sharma
 */
public class QueryGenerator {

    public static String pathQueryToCypher(String input) throws IOException {

        // Get the properties from the default file
        Neo4jLoaderProperties props = new Neo4jLoaderProperties();

        // Create a Path Query object using IM Service and the input string
        QueryService service = props.getQueryService();
        PathQuery pathQuery = service.createPathQuery(input);

        if(!pathQuery.isValid()){
            System.out.println("Please enter a valid path query.");
            System.exit(0);
        }

        // We need to call getQueryToExecute() first.  For template queries this gets a query that
        // excludes any optional constraints that have been switched off.  A normal PathQuery is
        // unchanged.
        pathQuery = pathQuery.getQueryToExecute();

        PathTree pathTree = new PathTree(pathQuery);
        Query query = new Query();

        createMatchClause(query, pathTree.getRoot());
        createReturnClause(query, pathTree, pathQuery);
        createWhereClause(query, pathTree, pathQuery);
        return query.toString();
    }

    private static void createWhereClause(Query query, PathTree pathTree, PathQuery pathQuery){
        String whereClause = "WHERE " + pathQuery.getConstraintLogic();
        for (String constraintCode : pathQuery.getConstraintCodes()){
            PathConstraint pathConstraint = pathQuery.getConstraintForCode(constraintCode);
            if(Constraint.isConstraintValid(pathConstraint, pathTree)){
                System.out.println("Invalid constraint.");
                System.exit(0);
            }
            Constraint constraint = new Constraint(pathConstraint, pathTree);
            whereClause = whereClause.replaceAll(constraintCode, constraint.toString());
        }
        query.setWhereClause(whereClause);
    }

    private static void createMatchClause(Query query, TreeNode treeNode){
        if (treeNode == null){
            return;
        }
        else if (treeNode.getParent() == null) {
            // Root TreeNode is always a Graph Node
            query.addToMatch("(" + treeNode.getVariableName() +
                            " :" + treeNode.getGraphicalName() + ")");
        } else if(treeNode.getTreeNodeType() == TreeNodeType.NODE) {
            if(treeNode.getParent().getTreeNodeType() == TreeNodeType.NODE){
                // If current TreeNode is a Graph Node and its parent is also a Graph Node,
                // then add a dummy relationship.
                query.addToMatch("(" + treeNode.getParent().getVariableName() + ")" +
                                "-[]-(" + treeNode.getVariableName() +
                                " :" + treeNode.getGraphicalName() + ")");
            } else if(treeNode.getParent().getTreeNodeType() == TreeNodeType.RELATIONSHIP) {
                // If current TreeNode is a Graph Node and its parent is a Graph Relationship,
                // then match an actual relationship of the current node with its grand parent node.
                query.addToMatch("("+ treeNode.getParent().getParent().getVariableName() + ")" +
                                "-[" + treeNode.getParent().getVariableName() +
                                ":" + treeNode.getParent().getGraphicalName() + "]" +
                                "-(" + treeNode.getVariableName() +
                                " :" + treeNode.getGraphicalName() + ")");
            }
        }
        // If current TreeNode represents a Graphical Relationship, then Do nothing.
        // We will match this relationship when recursion reaches its children.

        // Add all children to Match clause
        for (String key : treeNode.getChildrenKeys()){
            createMatchClause(query, treeNode.getChild(key));
        }
    }

    private static void createReturnClause(Query query, PathTree pathTree, PathQuery pathQuery){
        for (String path : pathQuery.getView()){
            TreeNode treeNode = pathTree.getTreeNode(path);
            if (treeNode.getTreeNodeType() == TreeNodeType.PROPERTY){
                // Return only if a property is queried !!
                query.addToReturn(treeNode.getParent().getVariableName() + "." +
                                    treeNode.getGraphicalName());
            }
        }
    }

}
