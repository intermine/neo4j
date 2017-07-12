package org.intermine.neo4j.cypher;

import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.CollectionDescriptor;
import org.intermine.metadata.ReferenceDescriptor;
import org.intermine.neo4j.Neo4jModelParser;
import org.intermine.neo4j.cypher.tree.TreeNodeType;
import org.intermine.pathquery.Path;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles InterMine to Neo4j ontology conversion.
 *
 * @author Yash Sharma
 */
public class OntologyConverter {

    /**
     * Given the path, this method finds whether the last element in the path is a Node,
     * Relationship or a Property in the InterMine Neo4j Graph
     *
     * @param path The path from the path query
     * @return TreeNodeType of the last element of the path
     */
    public static TreeNodeType getTreeNodeType(Path path) {
        if (path.endIsAttribute()) {
            System.out.println("is property");
            return TreeNodeType.PROPERTY;
        }
        else if (path.isRootPath()) {
            System.out.println("is node");
            return TreeNodeType.NODE;
        }
        else {
            System.out.println("is not property and not node");
            Neo4jModelParser modelParser = new Neo4jModelParser();
            if (path.endIsCollection()) {
                System.out.println("is collection");
                ClassDescriptor parentClassDescriptor = path.getPrefix().getEndClassDescriptor();
                CollectionDescriptor cd = parentClassDescriptor.getCollectionDescriptorByName(path.getLastElement());
                if (cd == null){
                    for (ClassDescriptor pCd: parentClassDescriptor.getSuperDescriptors()){
                        cd = pCd.getCollectionDescriptorByName(path.getLastElement());
                        if (cd != null) {
                            break;
                        }
                    }
                }
                if (modelParser.isIgnored(cd)) {
                    System.out.println("is ignored");
                    return TreeNodeType.NODE;
                }
                else {
                    System.out.println("is not ignored");
                    return TreeNodeType.RELATIONSHIP;
                }
            }
            else if (path.endIsReference()) {
                System.out.println("is reference");
                ClassDescriptor parentClassDescriptor = path.getPrefix().getEndClassDescriptor();
                ReferenceDescriptor rd = parentClassDescriptor.getReferenceDescriptorByName(path.getLastElement());
                if (rd == null){
                    for (ClassDescriptor pCd: parentClassDescriptor.getSuperDescriptors()){
                        rd = pCd.getReferenceDescriptorByName(path.getLastElement());
                        if (rd != null) {
                            break;
                        }
                    }
                }
                if (modelParser.isIgnored(rd)) {
                    System.out.println("is ignored");
                    return TreeNodeType.NODE;
                }
                else {
                    System.out.println("is not ignored");
                    return TreeNodeType.RELATIONSHIP;
                }
            }
        }
        return TreeNodeType.NODE;
    }

    /**
     * Given the path, this method returns the Neo4j name of the last element in the path
     * Usually the names of Nodes & Properties remain the same. Only the names of Relationships
     * are changed from InterMine to Neo4j.
     *
     * @param path The path from the path query
     * @return The Graphical (Neo4j) Name of the last element of the path
     */
    public static String getGraphicalName(Path path){
        if (path.isRootPath()) {
            // Root path always points to a Node class and the name of node class is
            // the same in InterMine model and in Neo4j
            return path.toString();
        }
        else if (path.endIsAttribute()) {
            // For attributes the name remains the same.
            return path.getLastElement();
        }
        else { // if (path.endIsReference() || path.endIsCollection())
            Neo4jModelParser modelParser = new Neo4jModelParser();
            // If the collection/reference is ignored, then it translates to a Neo4j Node
            // So return the name of the class (Class name remains same in InterMine and Neo4j)
            if (modelParser.isIgnored(path.getEndClassDescriptor())) {
                return path.getEndClassDescriptor().getName();
            }
            else {
                // If the collection/reference is not ignored, then it translates
                // into a Neo4j Relationship. Fetch the Relationship Name
                // from the genomic model XML file.
                String className = path.getPrefix().getEndClassDescriptor().getName();
                String refName = path.getLastElement();
                return new Neo4jModelParser().getRelationshipType(className, refName);
            }
        }
    }
}
