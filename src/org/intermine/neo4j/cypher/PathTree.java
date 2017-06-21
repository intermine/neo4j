package org.intermine.neo4j.cypher;

import org.intermine.pathquery.PathQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Describes a Path Tree.
 *
 * @author Yash Sharma
 */
public class PathTree {

    private TreeNode root;

    PathTree(PathQuery pathQuery){
        Boolean DEBUG = false;

        Set<String> paths = Helper.getAllPaths(pathQuery);

        if(DEBUG){
            System.out.println(paths);
        }

        // Initialize root node with null, since no node currently exists in the PathTree.
        root = null;

        // Create tree using all the paths
        for (String path : paths){
            List<String> tokens = Helper.getTokensFromPath(path);

            // Start variable name as an empty string. Gradually add each token for each node.
            String variableName = "";

            // List used to keep track of what portion of the path we have traversed.
            // At start, no portion of the path is traversed. So it is an empty list.
            List<String> pathSoFar = new ArrayList<>(tokens.size());

            if(DEBUG){
                System.out.println(path + "\n");
            }

            // We will create a TreeNode for each token of the Path.
            for (String token : tokens){
                // Since we have traversed till this token, add it to pathSoFar.
                pathSoFar.add(token);

                // Add token to variable name
                variableName += (token.toLowerCase());

                // TO DO - Set TreeNodeType of TreeNode using methods from Ontology Converter
                if(this.root == null){
                    // Create the root TreeNode. It is always represents a Graph Node.
                    // Parent is null for root.
                    this.root = new TreeNode(variableName, token, TreeNodeType.NODE, null);
                }
                else{
                    // Traverse through the tree to reach the required leaf node
                    TreeNode treeNode = root;
                    for (String str : pathSoFar){
                        if (pathSoFar.indexOf(str) == 0){
                            // Skip the root
                            continue;
                        }
                        // Get current TreeNode's child
                        TreeNode child = treeNode.getChild(str);

                        TreeNodeType treeNodeType;
                        if(tokens == pathSoFar){
                            treeNodeType = TreeNodeType.PROPERTY;
                        } else {
                            treeNodeType = OntologyConverter.getGraphComponentType(str);
                        }

                        // If child does not exist, create a new one.
                        // If it exists already, then this path prefix was also found in a previous path,
                        // so tokens for that have already been created. In this case we can simply do nothing
                        // and wait till we encounter a token for which TreeNode is not yet created.
                        if(child == null){
                            treeNode.addChild(str,
                                            new TreeNode(variableName, str, treeNodeType, treeNode));
                            break;
                        }
                        treeNode = child;
                    }
                }
                // Add an underscore in the variable name for the next child.
                variableName += "_";
            }
            if(DEBUG){
                System.out.println();
            }
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    /**
     * Prints names of all the nodes of a tree serially, separated by a closing parenthesis.
     */
    public void serialize(){
        System.out.print("Serializing PathTree :\n");
        if (root == null){
            return;
        }
        System.out.print(root.getName()+ ":" + root.getTreeNodeType().name() + " ");
        for (String key : root.getChildrenKeys()){
            serializeUtil(root.getChild(key));
        }
        System.out.print(")");
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
