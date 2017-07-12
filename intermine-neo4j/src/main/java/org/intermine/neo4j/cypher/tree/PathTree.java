package org.intermine.neo4j.cypher.tree;

import org.intermine.neo4j.cypher.Helper;
import org.intermine.neo4j.cypher.OntologyConverter;
import org.intermine.pathquery.OuterJoinStatus;
import org.intermine.pathquery.Path;
import org.intermine.pathquery.PathException;
import org.intermine.pathquery.PathQuery;

import java.util.*;

/**
 * Describes a Path Tree.
 *
 * @author Yash Sharma
 */
public class PathTree {

    private TreeNode root;

    public PathTree(PathQuery pathQuery) throws PathException {

        Set<Path> paths = new HashSet<>();
        paths.addAll(Helper.getAllPaths(pathQuery));

        // Initialize root node with null, since no node currently exists in the PathTree.
        this.root = null;

        // Create tree using all the paths
        for (Path path : paths){
            Path rootPath = path.decomposePath().get(0);

            for (Path traversedPath : path.decomposePath()){

                String variableName = Helper.getVariableNameFromPath(traversedPath);
                String graphicalName = OntologyConverter.getGraphicalName(traversedPath);

                if (traversedPath.isRootPath()) {
                    if (this.root == null) {
                        // Create the root TreeNode. It is always represents a Graph Node.
                        // Parent is null for root.
                        this.root = new TreeNode(rootPath.toString(),
                                                variableName,
                                                graphicalName,
                                                TreeNodeType.NODE,
                                                null,
                                                OuterJoinStatus.INNER);
                    }
                }
                else {
                    // First reach the Parent TreeNode for the current TreeNode
                    TreeNode parentNode = getTreeNode(traversedPath.getPrefix().toString());

                    String nodeName = traversedPath.getLastElement();

                    // If child TreeNode does not exist, create a new one.
                    // If it exists already, then this TreeNode has already been created for
                    // some previous path, so we do nothing.
                    if (parentNode.getChild(nodeName) == null) {
                        TreeNodeType treeNodeType = OntologyConverter.getTreeNodeType(traversedPath);
                        parentNode.addChild(nodeName, new TreeNode(nodeName,
                                                                variableName,
                                                                graphicalName,
                                                                treeNodeType,
                                                                parentNode,
                                                                OuterJoinStatus.INNER));
                    }
                }
            }

            setOuterJoinInPathTree(pathQuery);
        }
    }

    private void setOuterJoinInPathTree(PathQuery pathQuery){
        Map<String, OuterJoinStatus> outerJoinStatusMap = pathQuery.getOuterJoinStatus();
        for (String path : outerJoinStatusMap.keySet()){
            TreeNode treeNode = getTreeNode(path);
            if (treeNode != null && outerJoinStatusMap.get(path) == OuterJoinStatus.OUTER){
                // TO DO : Ponder whether we should set outer join for all children or just for
                // the current node.

                treeNode.setOuterJoinStatus(OuterJoinStatus.OUTER);
                //setOuterJoinInChildren(getTreeNode(path));
            }
        }
    }

    private void setOuterJoinInChildren(TreeNode treeNode){
        if (treeNode == null){
            return;
        }
        treeNode.setOuterJoinStatus(OuterJoinStatus.OUTER);
        for (String key : treeNode.getChildrenKeys()){
            setOuterJoinInChildren(treeNode.getChild(key));
        }
    }

    /**
     * Gets the root TreeNode of the PathTree
     *
     * @return the root TreeNode
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     * Gets the path of the Root Node
     *
     * @return the root TreeNode
     */
    public String getRootPath() {
        return root.getName();
    }

    /**
     * Traverses the PathTree as per the given path and returns the TreeNode found
     *
     * @param pathString the given path
     * @return the TreeNode object if it is found, null otherwise
     */
    public TreeNode getTreeNode(String pathString){
        if(root == null){
            return null;
        }
        List<String> nodes = Helper.getTokensFromPathString(pathString);
        TreeNode treeNode = root;
        for (String node: nodes){
            if (nodes.indexOf(node) == 0){
                continue;
            }
            treeNode = treeNode.getChild(node);
            if(treeNode == null){
                return null;
            }
        }
        return treeNode;
    }

    /**
     * Prints names of all the nodes of a tree serially, separated by a closing parenthesis.
     */
    public void serialize(){
        System.out.print("Serializing PathTree :\n");
        if (root == null){
            return;
        }
        System.out.print(root.getVariableName()+ ":" + root.getTreeNodeType().name() + " ");
        for (String key : root.getChildrenKeys()){
            serializeUtil(root.getChild(key));
        }
        System.out.println(")");
    }

    /**
     * Utility method which does recursion for the serialize method.
     */
    private void serializeUtil(TreeNode treeNode){
        if (treeNode == null){
            return;
        }
        System.out.print(treeNode.getName()+ ":" + treeNode.getTreeNodeType().name() + " ");
        for (String key : treeNode.getChildrenKeys()){
            serializeUtil(treeNode.getChild(key));
        }
        System.out.print(")");
    }

}
