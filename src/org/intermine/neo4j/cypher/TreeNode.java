package org.intermine.neo4j.cypher;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Describes a component used in the Path Query.
 *
 * @author Yash Sharma
 */
public class TreeNode {
    // Name as in Neo4j - Node Label or Relationship Type
    private String graphicalName;
    // Name as in path query
    private String name;
    // Name generated for cypher query
    private String variableName;
    // Node or Relationship or Property
    private TreeNodeType treeNodeType;
    // Reference to its parent in the PathTree
    private TreeNode parent;
    // Children of the TreeNode
    private Map<String, TreeNode> children;

    TreeNode(String variableName, String name, TreeNodeType treeNodeType, TreeNode parent){
        Boolean DEBUG = false;

        this.variableName = variableName;
        this.name = name;
        this.treeNodeType = treeNodeType;
        this.graphicalName = OntologyConverter.convertInterMineToNeo4j(name);
        this.parent = parent;
        children = new HashMap<>();
        if(DEBUG){
            if(parent == null){
                System.out.println("Create node " + name + " as root");
            } else {
                System.out.println("Create node " + name +
                ", parent " + parent.getName() +
                ", variableName " + variableName);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getGraphicalName() {
        return graphicalName;
    }

    public TreeNode getParent() {
        return parent;
    }

    public TreeNodeType getTreeNodeType() {
        return treeNodeType;
    }

    public TreeNode getChild(String key){
        return children.get(key);
    }

    public void addChild(String key, TreeNode child){
        children.put(key, child);
    }

    public Set<String> getChildrenKeys(){
        return children.keySet();
    }

    public void setGraphicalName(String graphicalName) {
        this.graphicalName = graphicalName;
    }

    public void setTreeNodeType(TreeNodeType treeNodeType) {
        this.treeNodeType = treeNodeType;
    }

    @Override
    public String toString(){
        return graphicalName + " : " + treeNodeType.name();
    }

}
