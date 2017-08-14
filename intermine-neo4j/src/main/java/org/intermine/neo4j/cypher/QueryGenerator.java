package org.intermine.neo4j.cypher;

import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.neo4j.Neo4jModelParser;
import org.intermine.neo4j.cypher.constraint.Constraint;
import org.intermine.neo4j.cypher.tree.PathTree;
import org.intermine.neo4j.cypher.tree.TreeNode;
import org.intermine.neo4j.cypher.tree.TreeNodeType;
import org.intermine.pathquery.*;
import org.intermine.webservice.client.services.QueryService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Generates Cypher Queries.
 *
 * @author Yash Sharma
 */
public class QueryGenerator {

    /**
     * Converts a given PathQuery in XML into the corresponding cypher query which can
     * be used to query a Neo4j database.
     *
     * @param pathQuery the given Path Query
     * @return the cypher query
     * @throws IOException
     */
    public static CypherQuery pathQueryToCypher(PathQuery pathQuery) throws IOException, PathException, ModelParserException, SAXException, ParserConfigurationException {

        // We need to call getQueryToExecute() first. For template queries this gets a query that
        // excludes any optional constraints that have been switched off.  A normal PathQuery is
        // unchanged.
        pathQuery = pathQuery.getQueryToExecute();

        if (!pathQuery.isValid()) {
            System.out.println("Please enter a valid path query.");
            System.exit(0);
        }

        PathTree pathTree = new PathTree(pathQuery);
        CypherQuery cypherQuery = new CypherQuery();

        // This creates the Match clause and also the Optional Match (if required)
        createMatchClause(cypherQuery, pathTree.getRoot());

        createWhereClause(cypherQuery, pathTree, pathQuery);
        createReturnClause(cypherQuery, pathTree, pathQuery);
        createOrderByClause(cypherQuery, pathTree, pathQuery);

        for (String view : pathQuery.getView()) {
            TreeNode viewNode = pathTree.getTreeNode(view);
            String parentVariable = viewNode.getParent().getVariableName();
            String attributeName = viewNode.getGraphicalName();
            String variableName = parentVariable + "." + attributeName;

            cypherQuery.addVariable(view, variableName);
        }

        return cypherQuery;
    }

    /**
     * Creates Order By clause using a PathQuery and its PathTree representation
     *
     * @param cypherQuery     the Cypher Query object
     * @param pathTree  the given PathTree
     * @param pathQuery the given PathQuery
     */
    private static void createOrderByClause(CypherQuery cypherQuery, PathTree pathTree, PathQuery pathQuery) {
        List<OrderElement> orderElements = pathQuery.getOrderBy();
        for (OrderElement orderElement : orderElements) {
            Order order = new Order(orderElement, pathTree);
            cypherQuery.addToOrderBy(order.toString());
        }
    }

    /**
     * This method adds three underscores as prefix & suffix to the constraint codes of the
     * constraint logic. This prevents unwanted replacements of the characters which are
     * same as codes but are actually not codes.
     *
     * @param pathQuery The given path query
     * @return modified constraint logic string
     */
    private static String getModifiedConstraintLogic(PathQuery pathQuery) {
        String logic = pathQuery.getConstraintLogic();
        for (String code : pathQuery.getConstraintCodes()) {
            logic = logic.replaceAll(code, modifiedConstraintCode(code));
        }
        return logic;
    }

    public static String modifiedConstraintCode(String string) {
        return "___" + string + "___";
    }

    /**
     * Creates WHERE clause using a PathQuery and its PathTree representation
     *
     * @param cypherQuery     the Cypher Query object
     * @param pathTree  the given PathTree
     * @param pathQuery the given PathQuery
     */
    private static void createWhereClause(CypherQuery cypherQuery, PathTree pathTree, PathQuery pathQuery) {
        if (pathQuery.getConstraintCodes().isEmpty()) {
            return;
        }
        String whereClause = "WHERE " + getModifiedConstraintLogic(pathQuery);
        whereClause = whereClause.toUpperCase();
        for (String constraintCode : pathQuery.getConstraintCodes()) {
            PathConstraint pathConstraint = pathQuery.getConstraintForCode(constraintCode);
            Constraint constraint = new Constraint(pathConstraint, pathTree);
            whereClause = whereClause.replaceAll(modifiedConstraintCode(constraintCode),
            constraint.toString());
        }
        cypherQuery.setWhereClause(whereClause);
    }

    /**
     * Creates MATCH clause using a PathTree
     *
     * @param cypherQuery    the Cypher Query object
     * @param treeNode the root node of the PathTree
     */
    private static void createMatchClause(CypherQuery cypherQuery, TreeNode treeNode) throws IOException, ModelParserException, SAXException, ParserConfigurationException {
        if (treeNode == null) {
            throw new IllegalArgumentException("Root node of PathTree cannot be null.");
        }
        else if (treeNode.getTreeNodeType() == TreeNodeType.PROPERTY) {
            // Properties don't need to be matched in the Match statement of cypher.
            return;
        }
        else if (treeNode.getParent() == null) {
            // Root TreeNode is always a Graph Node
            cypherQuery.addToMatch("(" + treeNode.getVariableName() +
                             " :" + treeNode.getGraphicalName() + ")");
        }
        else if (treeNode.getTreeNodeType() == TreeNodeType.NODE) {

            Neo4jModelParser modelParser = new Neo4jModelParser();
            modelParser.process(new Neo4jLoaderProperties());

            TreeNode parentTreeNode = treeNode.getParent();
            String className = parentTreeNode.getPath().getEndClassDescriptor().getSimpleName();
            String refName = treeNode.getName();

            String relationshipType = modelParser.getRelationshipType(className, refName);

            String match = "(" +
                            parentTreeNode.getVariableName() +
                            ")-[:" +
                            relationshipType +
                            "]-(" +
                            treeNode.getVariableName() +
                            " :" +
                            treeNode.getGraphicalName() +
                            ")";

            if (treeNode.getOuterJoinStatus() == OuterJoinStatus.INNER) {
                cypherQuery.addToMatch(match);
            }
            else {
                cypherQuery.addToOptionalMatch(match);
            }

        }
        // If current TreeNode represents a Graphical Relationship, then Do nothing.
        // We will match this relationship when recursion reaches its children.

        // Add all children to Match clause
        for (String key : treeNode.getChildrenKeys()) {
            createMatchClause(cypherQuery, treeNode.getChild(key));
        }
    }

    /**
     * Creates RETURN clause using a PathTree
     *
     * @param cypherQuery     the Cypher Query object
     * @param pathTree  the given PathTree
     * @param pathQuery the given PathQuery
     */
    private static void createReturnClause(CypherQuery cypherQuery, PathTree pathTree, PathQuery pathQuery) {
        for (String path : pathQuery.getView()) {
            TreeNode treeNode = pathTree.getTreeNode(path);
            if (treeNode != null && treeNode.getTreeNodeType() == TreeNodeType.PROPERTY) {
                // Return ONLY IF a property is queried !!
                // Nodes & Relationships cannot be returned.
                cypherQuery.addToReturn(treeNode.getParent().getVariableName() + "." +
                treeNode.getGraphicalName());
            }
        }
    }

}
