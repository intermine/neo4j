package org.intermine.neo4j.cypher;

import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.neo4j.Neo4jModelParser;
import org.intermine.neo4j.cypher.constraint.Constraint;
import org.intermine.neo4j.cypher.tree.PathTree;
import org.intermine.neo4j.cypher.tree.TreeNode;
import org.intermine.neo4j.cypher.tree.TreeNodeType;
import org.intermine.pathquery.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * This class can be used to convert a Path Query to its equivalent Cypher Query.
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
            throw new IllegalArgumentException("Path Query is invalid.");
        }

        PathTree pathTree = new PathTree(pathQuery);

        // Initialize an empty Cypher Query object. Remaining part of this method adds various
        // clauses and other data to the Cypher Query.
        CypherQuery cypherQuery = new CypherQuery();

        // This creates the Match clause and also the Optional Match clause (if required)
        createMatchClause(cypherQuery, pathTree.getRoot());

        createWhereClause(cypherQuery, pathTree, pathQuery);
        createReturnClause(cypherQuery, pathTree, pathQuery);

        // Add all Order objects to Cypher
        addOrdersToCypher(cypherQuery, pathTree, pathQuery);

        // Add all the views and their corresponding variable names to the Cypher Query.
        // These variable names will be used to extract proper values from the
        // Neo4j result records.
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
     * Generates Order objects from the Path Query and adds them to the Cypher Query.
     * These will be used to create the Order By clause within the CypherQuery class.
     *
     * @param cypherQuery     the Cypher Query object
     * @param pathTree  the given PathTree
     * @param pathQuery the given PathQuery
     */
    private static void addOrdersToCypher(CypherQuery cypherQuery, PathTree pathTree, PathQuery pathQuery) {
        List<OrderElement> orderElements = pathQuery.getOrderBy();
        for (OrderElement orderElement : orderElements) {
            Order order = new Order(orderElement, pathTree);
            cypherQuery.addOrder(order);
        }
    }

    /**
     * This method adds three underscores as prefix & suffix to the constraint codes of the
     * constraint logic. This prevents unwanted replacements of the characters which are
     * same as codes but are actually not codes.
     * For example, "A and B" will get converted to "___A___ and ___B___".
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

    /**
     * Adds three underscores as prefix and as suffix to the given Constraint Code string.
     * @param string the Constraint Code string
     * @return the modified constraint code as string
     */
    private static String modifiedConstraintCode(String string) {
        return "___" + string + "___";
    }

    /**
     * Creates WHERE clause of the Cypher Query using a PathQuery and PathTree
     *
     * @param cypherQuery     the Cypher Query object
     * @param pathTree  the given PathTree
     * @param pathQuery the given PathQuery
     */
    private static void createWhereClause(CypherQuery cypherQuery, PathTree pathTree, PathQuery pathQuery) {
        if (pathQuery.getConstraintCodes().isEmpty()) {
            return;
        }
        // We replace each constraint code in the constraint logic with its corresponding
        // constraint string. While replacing the constraint codes, it might be possible that
        // characters other than constraint codes also get replaced. To prevent unwanted
        // replacement, we add underscores to each constraint code to get the modified
        // constraint code first.
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
     * Creates MATCH clause of the Cypher Query using the given PathTree
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
        else {
            String match = null;
            if (treeNode.getTreeNodeType() == TreeNodeType.NODE) {

                Neo4jModelParser modelParser = new Neo4jModelParser();
                modelParser.process(new Neo4jLoaderProperties());
                TreeNode parentTreeNode = treeNode.getParent();

                if (parentTreeNode.getTreeNodeType() == TreeNodeType.NODE) {
                    // If current TreeNode is a NODE and its parent is also a NODE, then use
                    // ModelParser.getRelationshipType to fetch the Relationship Type from the
                    // XML data model file.
                    String className = parentTreeNode.getPath().getEndClassDescriptor().getSimpleName();
                    String refName = treeNode.getName();

                    String relationshipType = modelParser.getRelationshipType(className, refName);
                    match = "(" +
                            parentTreeNode.getVariableName() +
                            ")-[:" +
                            relationshipType +
                            "]-(" +
                            treeNode.getVariableName() +
                            " :" +
                            treeNode.getGraphicalName() +
                            ")";
                }
                else if (parentTreeNode.getTreeNodeType() == TreeNodeType.RELATIONSHIP) {
                    // If current TreeNode is a NODE and its parent is a RELATIONSHIP, then fetch
                    // the grand parent from the PathTree. Use relationship described by the parent
                    // TreeNode between the grand parent and the current TreeNode.
                    match = "(" +
                            parentTreeNode.getParent().getVariableName() +
                            ")-[" +
                            parentTreeNode.getVariableName() +
                            ":" +
                            parentTreeNode.getGraphicalName() +
                            "]-(" +
                            treeNode.getVariableName() +
                            " :" +
                            treeNode.getGraphicalName() +
                            ")";
                }
            }
            else if (treeNode.getTreeNodeType() == TreeNodeType.RELATIONSHIP) {
                // If the current TreeNode is a RELATIONSHIP and it has no children, then
                // use this relationship between the NODE represented by the parent TreeNode
                // and an empty node. For example, (m:Gene)-[r:LOCATED_ON]-().
                if (treeNode.getChildrenKeys().isEmpty()) {
                    TreeNode parentTreeNode = treeNode.getParent();
                    match = "(" +
                            parentTreeNode.getVariableName() +
                            ")-[" +
                            treeNode.getVariableName() +
                            ":" +
                            treeNode.getGraphicalName() +
                            "]-()";
                }
            }
            if (match != null) {
                // If string is not null then any of the above 3 cases has occurred,
                // so we must add it to MATCH or OPTIONAL MATCH clause as required.
                if (treeNode.getOuterJoinStatus() == OuterJoinStatus.INNER) {
                    cypherQuery.addToMatch(match);
                }
                else {
                    cypherQuery.addToOptionalMatch(match);
                }
            }
        }

        // Recursively add all the children TreeNodes to the Match/Optional Match clause
        for (String key : treeNode.getChildrenKeys()) {
            createMatchClause(cypherQuery, treeNode.getChild(key));
        }
    }

    /**
     * Creates RETURN clause of the Cypher Query using the Path Query and Path Tree
     *
     * @param cypherQuery     the Cypher Query object
     * @param pathTree  the given PathTree
     * @param pathQuery the given PathQuery
     */
    private static void createReturnClause(CypherQuery cypherQuery, PathTree pathTree, PathQuery pathQuery) {
        for (String path : pathQuery.getView()) {
            TreeNode treeNode = pathTree.getTreeNode(path);
            if (treeNode != null && treeNode.getTreeNodeType() == TreeNodeType.PROPERTY) {
                // Add to return clause ONLY IF the TreeNode represents a property !!
                // Nodes & Relationships cannot be returned.
                cypherQuery.addToReturn(treeNode.getParent().getVariableName()
                                        + "."
                                        + treeNode.getGraphicalName());
            }
        }
    }

}
