package org.intermine.neo4j.cypher.tree;

import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.neo4j.Neo4jModelParser;
import org.intermine.neo4j.cypher.Helper;
import org.intermine.pathquery.OuterJoinStatus;
import org.intermine.pathquery.Path;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Describes a Node of a PathTree.
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

    // Outer Join Status
    private OuterJoinStatus outerJoinStatus;

    // Path which is represented by this TreeNode
    Path path;

    TreeNode(String name,
             Path path,
             TreeNode parent,
             OuterJoinStatus outerJoinStatus) throws IOException, ModelParserException, SAXException, ParserConfigurationException {
        this.name = name;
        this.variableName = Helper.getVariableNameFromPath(path);
        this.outerJoinStatus = outerJoinStatus;
        this.children = new HashMap<>();
        this.parent = parent;
        this.path = path;

        // Set Graphical Name and TreeNodeType using Neo4jModelParser
        if (path.isRootPath()) {
            this.graphicalName = path.toString();
            this.treeNodeType = TreeNodeType.NODE;
        }
        else if (path.endIsAttribute()) {
            this.graphicalName = path.getLastElement();
            this.treeNodeType = TreeNodeType.PROPERTY;
        }
        else { // When end of the path is either a Collection or Reference
            String referencedClass = path.getLastClassDescriptor().getSimpleName();

            Neo4jModelParser modelParser = new Neo4jModelParser();
            modelParser.process(new Neo4jLoaderProperties());

            if(modelParser.isRelationship(referencedClass)) {
                String relationshipTarget = modelParser.getRelationshipTarget(referencedClass);
                this.graphicalName = modelParser.getRelationshipType(referencedClass, relationshipTarget);
                this.treeNodeType = TreeNodeType.RELATIONSHIP;
            }
            else {
                this.graphicalName = path.getEndClassDescriptor().getSimpleName();
                this.treeNodeType = TreeNodeType.NODE;
            }
        }
    }

    public Path getPath() {
        return path;
    }

    /**
     * Gets the name of the TreeNode. It is a part of a path. For example, in the path
     * Gene.chromosomes.interactions, we have three names which will be represented by TreeNodes.
     * NOTE THAT names are not unique, multiple TreeNodes may have same name as a name can
     * be repeated in a path.
     *
     * @return the name of the TreeNode
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the variable name of the TreeNode. It is unique for each node and is used to refer
     * to this node in the Cypher query.
     *
     * @return the variable name of the TreeNode
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Gets the Neo4j name of the TreeNode. For Graph Nodes it is a label, for Relationships it is
     * the Relationship Type and for Properties, it is the Property Key.
     *
     * @return the graphical name of the TreeNode
     */
    public String getGraphicalName() {
        return graphicalName;
    }

    /**
     * Gets the parent TreeNode of the given TreeNode. Null is returned for the root TreeNode
     * @return the reference to the Parent TreeNode, null if the current TreeNode is root of the PathTree
     */
    public TreeNode getParent() {
        return parent;
    }

    /**
     * Finds out where the current TreeNode represents a Graphical Node, Relationship or Property
     *
     * @return the type of the TreeNode
     */
    public TreeNodeType getTreeNodeType() {
        return treeNodeType;
    }

    /**
     * Gets the children of the TreeNode with the given name.
     * @param key the name of the child
     * @return the reference to the child
     */
    public TreeNode getChild(String key){
        return children.get(key);
    }

    /**
     * Add a child to the TreeNode with the given key.
     *
     * @param key the key for the child TreeNode
     * @param child the child TreeNode
     */
    public void addChild(String key, TreeNode child){
        children.put(key, child);
    }

    /**
     * Gets the names of all the children of the current TreeNode
     *
     * @return the set of names of all the children of the current TreeNode
     */
    public Set<String> getChildrenKeys(){
        return children.keySet();
    }

    /**
     * Sets the graphical name of the TreeNode
     *
     * @param graphicalName the graphical name
     */
    public void setGraphicalName(String graphicalName) {
        this.graphicalName = graphicalName;
    }

    /**
     * Sets the TreeNodeType of the TreeNode
     *
     * @param treeNodeType the TreeNodeType
     */
    public void setTreeNodeType(TreeNodeType treeNodeType) {
        this.treeNodeType = treeNodeType;
    }

    public OuterJoinStatus getOuterJoinStatus() {
        return outerJoinStatus;
    }

    public void setOuterJoinStatus(OuterJoinStatus outerJoinStatus) {
        this.outerJoinStatus = outerJoinStatus;
    }

    /**
     * Coverts a TreeNode to its String representation
     *
     * @return the string representation of the TreeNode
     */
    @Override
    public String toString(){
        return variableName + "(" + graphicalName + ")" + ":" + treeNodeType.name();
    }

}
