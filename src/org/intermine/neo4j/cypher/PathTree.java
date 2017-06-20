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

    TreeNode root;

    PathTree(PathQuery pathQuery){
        Boolean DEBUG = false;

        // Print paths
        Set<String> paths = Helper.getAllPaths(pathQuery);

        if(DEBUG){
            System.out.println(paths);
        }

        // Root node is always null at the start.
        root = null;

        for (String path : paths){
            List<String> tokens = Helper.getTokensFromPath(path);

            // Start variable name as an empty string. Gradually add each token for each node.
            String variableName = "";

            // At start, no portion of the path is traversed.
            List<String> pathSoFar = new ArrayList<>(tokens.size());

            if(DEBUG){
                System.out.println(path + "\n");
            }

            for (String token : tokens){
                pathSoFar.add(token);

                // Add token to variable name
                variableName += (token.toLowerCase());

                if(DEBUG){
                    System.out.println(pathSoFar);
                }

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
                        if(DEBUG){
                            System.out.println("treeNode is " + treeNode.getName());
                        }
                        // Get current TreeNode's child
                        TreeNode child = treeNode.getChild(str);
                        // If child is null, add new child
                        if(child == null){
                            treeNode.addChild(str,
                                            new TreeNode(variableName, str, TreeNodeType.NODE, treeNode));
                            break;
                        }
                        treeNode = child;
                    }
                }
                variableName += "_";
            }
            if(DEBUG){
                System.out.println();
            }
        }
    }

    public void serialize(){
        System.out.print("Serializing PathTree :\n");
        if (root == null){
            return;
        }
        System.out.print(root.getName() + " ");
        for (String key : root.getChildrenKeys()){
            serializeUtil(root.getChild(key));
        }
        System.out.print(")");
    }

    private void serializeUtil(TreeNode treeNode){
        if (treeNode == null){
            return;
        }
        System.out.print(treeNode.getName() + " ");
        for (String key : treeNode.getChildrenKeys()){
            serializeUtil(treeNode.getChild(key));
        }
        System.out.print(")");
    }

    public String toCypher(){
        // TO DO : Write this method
        return "";
    }

}
