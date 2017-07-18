package org.intermine.neo4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.io.IOUtils;

import org.intermine.metadata.AttributeDescriptor;
import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.CollectionDescriptor;
import org.intermine.metadata.InterMineModelParser;
import org.intermine.metadata.Model;
import org.intermine.metadata.ModelParserException;
import org.intermine.metadata.ReferenceDescriptor;
import org.intermine.metadata.SAXParser;

/**
 * Parse a Neo4j-modified InterMine metadata XML file to provide instructions for
 * loading data into a Neo4j graph.
 *
 * @author Sam Hokin
 */
public class Neo4jModelParser {

    static final String NEO4J_IGNORE_ATTRIBUTE = "neo4j-ignore";
    static final String NEO4J_RELATIONSHIP_ATTRIBUTE = "neo4j-relationship";

    // the InterMineModel represented by this XML file
    Model model;

    // classes, attributes, references and collections to ignore during Neo4j loading
    Set<String> ignoredClasses;           // e.g. "Sequence"
    Set<String> ignoredAttributes;        // e.g. "BioEntity.chadoFeatureId"
    Set<String> ignoredReferences;        // e.g. "SequenceFeature.chromosomeLocation"
    Set<String> ignoredCollections;       // e.g. "GOAnnotation.transcripts"

    // the Neo4j relationship types corresponding to classes that become relationships
    // as well as references and collections that should be renamed in the graph
    Map<String,String> relationshipTypes; // e.g. "Location" -> "LOCATED_ON" or "homologue" -> "HOMOLOGUE_OF"

    /**
     * Constructor
     */
    public Neo4jModelParser() {
        // create our ignored sets
        ignoredClasses = new LinkedHashSet<String>();
        ignoredAttributes = new LinkedHashSet<String>();
        ignoredReferences = new LinkedHashSet<String>();
        ignoredCollections = new LinkedHashSet<String>();

        // relationship types, keyed by class name for classes, or Class.name for references and collections
        relationshipTypes = new LinkedHashMap<String,String>();

        // initialization
        ignoredClasses.add("InterMineObject"); // doesn't appear in XML files but most certainly should be ignored!
    }

    /**
     * Simple main method that spits out Neo4j information extracted from a model XML file on the command line.
     * (Also serves as an example of how to use this class.)
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, SAXException, ParserConfigurationException, ModelParserException {

        // validation
        if (args.length!=1) {
            System.out.println("Usage: Neo4jModelParser <XML file>");
            System.exit(0);
        }
        String dataModelFilename = args[0];

        // process the XML file for Neo4j instructions
        Neo4jModelParser nmp = new Neo4jModelParser();
        nmp.process(new FileReader(dataModelFilename));

        // output the model with Neo4j notations
        for (ClassDescriptor classDescriptor : nmp.getModel().getClassDescriptors()) {
            boolean isIgnored = nmp.isIgnored(classDescriptor);
            boolean isRelationship = nmp.isRelationship(classDescriptor);
            if (isRelationship) {
                System.out.print("R ");
            } else if (isIgnored) {
                System.out.print("X ");
            } else {
                System.out.print("+ ");
            }
            System.out.print(classDescriptor.getSimpleName());
            if (isRelationship) {
                System.out.print(" --> "+nmp.getRelationshipType(classDescriptor));
            }
            System.out.println("");
            // show attributes
            Set<AttributeDescriptor> attrDescriptors = classDescriptor.getAttributeDescriptors();
            for (AttributeDescriptor ad : attrDescriptors) {
                if (nmp.isIgnored(ad)) System.out.print("X");
                System.out.println("\ta "+ad.getName());
            }
            // show references
            Set<ReferenceDescriptor> refDescriptors = classDescriptor.getReferenceDescriptors();
            for (ReferenceDescriptor rd : refDescriptors) {
                if (nmp.isIgnored(rd)) System.out.print("X");
                System.out.print("\tr "+rd.getName());
                if (!nmp.getRelationshipType(classDescriptor.getSimpleName(),rd.getName()).equals(rd.getName())) {
                    System.out.println(" --> "+nmp.getRelationshipType(classDescriptor.getSimpleName(),rd.getName()));
                } else {
                    System.out.println("");
                }
            }
            // show collections
            Set<CollectionDescriptor> collDescriptors = classDescriptor.getCollectionDescriptors();
            for (CollectionDescriptor cd : collDescriptors) {
                if (nmp.isIgnored(cd)) System.out.print("X");
                System.out.print("\tc "+cd.getName());
                if (!nmp.getRelationshipType(classDescriptor.getSimpleName(),cd.getName()).equals(cd.getName())) {
                    System.out.println(" --> "+nmp.getRelationshipType(classDescriptor.getSimpleName(),cd.getName()));
                } else {
                    System.out.println("");
                }
            }
        }

    }

    /**
     * Return true if the provided class is to be ignored for Neo4j node loading
     *
     * @param cd the ClassDescriptor
     * @return true if the class is to be ignored in Neo4j node loading
     */
    public boolean isIgnored(ClassDescriptor cd) {
        return ignoredClasses.contains(cd.getSimpleName());
    }

    /**
     * Return true if the provided class is meant to be a Neo4j relationship
     *
     * @param cd the ClassDescriptor
     * @return true if the class is to be a Neo4j relationship
     */
    public boolean isRelationship(ClassDescriptor cd) {
        return relationshipTypes.containsKey(cd.getSimpleName());
    }

    /**
     * Return the relationship type for the given class, if it is to be a Neo4j relationship; null otherwise.
     * @param cd the ClassDescriptor
     * @return the relationship type, or null if the given class is not meant to be a Neo4j relationship
     */
    public String getRelationshipType(ClassDescriptor cd) {
        if (relationshipTypes.containsKey(cd.getSimpleName())) return(relationshipTypes.get(cd.getSimpleName()));
        // default
        return null;
    }

    /**
     * Return the relationship type for the given reference or collection, if it is to be renamed in the Neo4j graph; refName otherwise.
     * @param className the name of the class
     * @param refName the name of the reference or collection
     * @return the relationship type, either the renamed value or refName if it is not designated to be renamed in Neo4j
     */
    public String getRelationshipType(String className, String refName) {
        String key = className+"."+refName;
        // found for this class
        if (relationshipTypes.containsKey(key)) return(relationshipTypes.get(key));
        // default
        return refName;
    }

    /**
     * Return the full map of relationship types
     *
     * @return a string-string map of classes-relationship types
     */
    public Map<String,String> getRelationshipTypes() {
        return relationshipTypes;
    }

    /**
     * Return true if the provided attribute is to be ignored for Neo4j node loading
     *
     * @param ad the AttributeDescriptor
     * @return true if the attribute is to be ignored in Neo4j node loading
     */
    public boolean isIgnored(AttributeDescriptor ad) {
        String adName = ad.getClassDescriptor().getSimpleName()+"."+ad.getName();
        return ignoredAttributes.contains(adName);
    }

    /**
     * Return true if the provided reference is to be ignored for Neo4j node loading
     *
     * @param rd the ReferenceDescriptor
     * @return true if the reference is to be ignored in Neo4j node loading
     */
    public boolean isIgnored(ReferenceDescriptor rd) {
        String rdName = rd.getClassDescriptor().getSimpleName()+"."+rd.getName();
        return ignoredReferences.contains(rdName);
    }

    /**
     * Return true if the provided collection is to be ignored for Neo4j node loading
     *
     * @param cd the CollectionDescriptor
     * @return true if the collection is to be ignored in Neo4j node loading
     */
    public boolean isIgnored(CollectionDescriptor cd) {
        String cdName = cd.getClassDescriptor().getSimpleName()+"."+cd.getName();
        return ignoredCollections.contains(cdName);
    }

    /**
     * Read source model information in InterMine-Neo4j XML format and populate various sets and maps with Neo4j loading properties.
     * Also returns the IM model, just as InterMineModelParser does.
     *
     * @param reader a file reader providing the XML file
     * @throws ModelParserException if something goes wrong with parsing the class descriptors.
     */
    public void process(Reader reader) throws IOException, SAXException, ParserConfigurationException, ModelParserException {
        // we need to copy the data so we can read it twice
        String xmlData = IOUtils.toString(reader);
        // parse the file for the InterMine Model
        model = new InterMineModelParser().process(new InputStreamReader(IOUtils.toInputStream(xmlData,"UTF-8")));
        // parse the file for Neo4j relevant stuff
        SAXParser.parse(new InputSource(new InputStreamReader(IOUtils.toInputStream(xmlData,"UTF-8"))), new ModelHandler());
        // update relationshipTypes to include references and collections in subclasses
        // have to use a standalone map to avoid concurrent updates
        LinkedHashMap<String,String> addMap = new LinkedHashMap<String,String>();
        for (String key : relationshipTypes.keySet()) {
            String[] parts = key.split("\\.");
            if (parts.length==2) {
                String className = parts[0];
                String refName = parts[1];
                String relType = relationshipTypes.get(key);
                ClassDescriptor cd = model.getClassDescriptorByName(className);
                Set<ClassDescriptor> subDescriptors = model.getAllSubs(cd);
                for (ClassDescriptor scd : subDescriptors) {
                    addMap.put(scd.getSimpleName()+"."+refName, relType);
                }
            }
        }
        relationshipTypes.putAll(addMap);
    }

    /**
     * Run process() on the data model file given in a Neo4jLoaderProperties, presumed to be in the classpath
     *
     * @param props an instantiated Neo4jLoaderProperties object
     * @throws ModelParserException if something goes wrong with parsing the class descriptors.
     */
    public void process(Neo4jLoaderProperties props) throws IOException, SAXException, ParserConfigurationException, ModelParserException {
        process(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(props.dataModelFilename)));
    }

    /**
     * Just a getter.
     */
    public Model getModel() {
        return model;
    }
    
    /**
     * Just a getter.
     */
    public Set<String> getIgnoredClasses() {
        return ignoredClasses;
    }

    /**
     * Just a getter.
     */
    public Set<String> getIgnoredAttributes() {
        return ignoredAttributes;
    }

    /**
     * Just a getter.
     */
    public Set<String> getIgnoredReferences() {
        return ignoredReferences;
    }

    /**
     * Just a getter.
     */
    public Set<String> getIgnoredCollections() {
        return ignoredCollections;
    }

    /**
     * Extension of DefaultHandler to handle IM-Neo4j metadata file
     */
    class ModelHandler extends DefaultHandler {

        // holds the current class
        SkeletonClass cls;

        /**
         * {@inheritDoc}
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attrs) {

            // added Neo4j attributes
            boolean neo4jIgnore = Boolean.valueOf(attrs.getValue(NEO4J_IGNORE_ATTRIBUTE)).booleanValue();
            String neo4jRelationship = attrs.getValue(NEO4J_RELATIONSHIP_ATTRIBUTE);
            
            if ("class".equals(qName)) {

                String name = attrs.getValue("name");
                String supers = attrs.getValue("extends");
                cls = new SkeletonClass(name, supers);
                if (neo4jIgnore) ignoredClasses.add(name);
                if (!StringUtils.isEmpty(neo4jRelationship)) {
                    ignoredClasses.add(name);
                    relationshipTypes.put(name, neo4jRelationship);
                }

            } else if ("attribute".equals(qName)) {

                String name = attrs.getValue("name");
                if (StringUtils.isEmpty(name)) throw new IllegalArgumentException("Error - `" + cls.name + "` has an attribute" + " with an empty/null name");
                if (neo4jIgnore) ignoredAttributes.add(cls.name+"."+name);

            } else if ("reference".equals(qName)) {

                String name = attrs.getValue("name");
                if (StringUtils.isEmpty(name)) throw new IllegalArgumentException("Error - `" + cls.name + "` has a reference" + " with an empty/null name");
                if (neo4jIgnore) ignoredReferences.add(cls.name+"."+name);
                if (!StringUtils.isEmpty(neo4jRelationship)) relationshipTypes.put(cls.name+"."+name, neo4jRelationship);

            } else if ("collection".equals(qName)) {

                String name = attrs.getValue("name");
                if (StringUtils.isEmpty(name)) throw new IllegalArgumentException("Error - `" + cls.name + "` has a collection" + " with an empty/null name");
                if (neo4jIgnore) ignoredCollections.add(cls.name+"."+name);
                if (!StringUtils.isEmpty(neo4jRelationship)) relationshipTypes.put(cls.name+"."+name, neo4jRelationship);

            }
        }

    }

    /**
     * Semi-constructed ClassDescriptor
     */
    static class SkeletonClass {
        String name;
        String supers;

        /**
         * Constructor.
         *
         * @param name the fully qualified name of the described class
         * @param supers a space string of fully qualified class names
         */
        SkeletonClass(String name, String supers) {
            this.name = name;
            if (this.name.contains(".")) {
                throw new IllegalArgumentException("Class " + name + " is not in the model package.");
            }
            if (supers != null) {
                String[] superNames = supers.split(" ");
                StringBuilder supersBuilder = new StringBuilder();
                boolean needComma = false;
                for (String superName : superNames) {
                    String origSuperName = superName;
                    if (!"java.lang.Object".equals(superName)) {
                        if (superName.contains(".")) {
                            throw new IllegalArgumentException("Superclass " + origSuperName + " of class " + this.name + " is not in the model package.");
                        }
                    }
                    if (needComma) {
                        supersBuilder.append(" ");
                    }
                    needComma = true;
                    supersBuilder.append(superName);
                }
                this.supers = supersBuilder.toString();
            } else {
                this.supers = null;
            }
        }
    }

}
