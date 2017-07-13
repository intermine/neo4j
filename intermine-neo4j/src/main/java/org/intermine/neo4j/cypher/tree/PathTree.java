package org.intermine.neo4j.cypher.tree;

import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.cypher.Helper;
import org.intermine.neo4j.cypher.OntologyConverter;
import org.intermine.pathquery.OuterJoinStatus;
import org.intermine.pathquery.Path;
import org.intermine.pathquery.PathException;
import org.intermine.pathquery.PathQuery;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 * Describes a Path Tree.
 *
 * @author Yash Sharma
 */
public class PathTree {

    private TreeNode root;

    public PathTree(PathQuery pathQuery) throws PathException, ModelParserException, ParserConfigurationException, SAXException, IOException {

        Set<Path> paths = new HashSet<>();
        paths.addAll(Helper.getAllPaths(pathQuery));

        // Initialize root node with null, since no node currently exists in the PathTree.
        this.root = null;

        // Create tree using all the paths
        for (Path path : paths){
            Path rootPath = path.decomposePath().get(0);

            for (Path traversedPath : path.decomposePath()){

                String nodeName;

                if (traversedPath.isRootPath()) {
                    if (this.root == null) {
                        nodeName = rootPath.toString();
                        // Create the root TreeNode. It always represents a Graph Node.
                        // Parent is null for the root.
                        this.root = new TreeNode(nodeName,
                                                traversedPath,
                                                null,
                                                OuterJoinStatus.INNER);
                    }
                }
                else {
                    // First reach the Parent TreeNode for the current TreeNode
                    TreeNode parentNode = getTreeNode(traversedPath.getPrefix().toString());

                    nodeName = traversedPath.getLastElement();

                    // If child TreeNode does not exist, create a new one.
                    // If it exists already, then this TreeNode has already been created for
                    // some previous path, so we do nothing.
                    if (parentNode.getChild(nodeName) == null) {
                        parentNode.addChild(nodeName, new TreeNode(nodeName,
                                                                traversedPath,
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
     * @param path the given path
     * @return the TreeNode object if it is found, null otherwise
     */
    public TreeNode getTreeNode(String path){
        if(root == null){
            return null;
        }
        List<String> nodes = Helper.getTokensFromPathString(path);
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

    public TreeNode getTreeNode(Path path){
        return getTreeNode(path.toString());
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
