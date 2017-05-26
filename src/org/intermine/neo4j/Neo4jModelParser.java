package org.intermine.neo4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.LinkedHashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.commons.lang.StringUtils;

import org.intermine.metadata.AttributeDescriptor;
import org.intermine.metadata.ClassDescriptor;
import org.intermine.metadata.CollectionDescriptor;
import org.intermine.metadata.InterMineModelParser;
import org.intermine.metadata.Model;
import org.intermine.metadata.ModelParserException;
import org.intermine.metadata.ReferenceDescriptor;
import org.intermine.metadata.SAXParser;

/**
 * Parse a Neo4j-modified InterMine metadata XML file to provide instructions for loading data into a Neo4j graph.
 *
 * @author Sam Hokin
 */
public class Neo4jModelParser {

    // classes, attributes, references and collections to ignore during Neo4j loading
    Set<String> ignoredClasses;     // e.g. "Sequence"
    Set<String> ignoredAttributes;  // e.g. "BioEntity.chadoFeatureId"
    Set<String> ignoredReferences;  // e.g. "SequenceFeature.chromosomeLocation"
    Set<String> ignoredCollections; // e.g. "GOAnnotation.transcripts"
    
    /**
     * Constructor
     */
    public Neo4jModelParser() {
        // create our sets
        ignoredClasses = new LinkedHashSet<String>();
        ignoredAttributes = new LinkedHashSet<String>();
        ignoredReferences = new LinkedHashSet<String>();
        ignoredCollections = new LinkedHashSet<String>();
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
        nmp.process(new InputStreamReader(new FileInputStream(dataModelFilename)));

        // get the plain IM model
        Model model = new InterMineModelParser().process(new InputStreamReader(new FileInputStream(dataModelFilename)));

        // output the model with Neo4j notations
        for (ClassDescriptor classDescriptor : model.getClassDescriptors()) {
            boolean ignored = nmp.isIgnored(classDescriptor);
            if (ignored) {
                System.out.print("X ");
            } else {
                System.out.print("+ ");
            }
            System.out.print(classDescriptor.getSimpleName());
            System.out.println("");
            if (!ignored) {
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
                    System.out.println("\tr "+rd.getName());
                }
                // show collections
                Set<CollectionDescriptor> collDescriptors = classDescriptor.getCollectionDescriptors();
                for (CollectionDescriptor cd : collDescriptors) {
                    if (nmp.isIgnored(cd)) System.out.print("X");
                    System.out.println("\tc "+cd.getName());
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
     * @return an InterMine Model
     * @throws ModelParserException if something goes wrong with parsing the class descriptors.
     */
    public void process(Reader reader) throws IOException, SAXException, ParserConfigurationException, ModelParserException {
        // parse the file for Neo4j relevant stuff
        SAXParser.parse(new InputSource(reader), new ModelHandler());
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

            if ("class".equals(qName)) {

                String name = attrs.getValue("name");
                String supers = attrs.getValue("extends");
                cls = new SkeletonClass(name, supers);
                boolean neo4jIgnore = Boolean.valueOf(attrs.getValue("neo4j-ignore")).booleanValue();
                if (neo4jIgnore) ignoredClasses.add(name);

            } else if ("attribute".equals(qName)) {

                String name = attrs.getValue("name");
                if (StringUtils.isEmpty(name)) {
                    throw new IllegalArgumentException("Error - `" + cls.name + "` has an attribute" + " with an empty/null name");
                }
                boolean neo4jIgnore = Boolean.valueOf(attrs.getValue("neo4j-ignore")).booleanValue();
                if (neo4jIgnore) ignoredAttributes.add(cls.name+"."+name);
                
            } else if ("reference".equals(qName)) {

                String name = attrs.getValue("name");
                if (StringUtils.isEmpty(name)) {
                    throw new IllegalArgumentException("Error - `" + cls.name + "` has a reference" + " with an empty/null name");
                }
                boolean neo4jIgnore = Boolean.valueOf(attrs.getValue("neo4j-ignore")).booleanValue();
                if (neo4jIgnore) ignoredReferences.add(cls.name+"."+name);
                
            } else if ("collection".equals(qName)) {

                String name = attrs.getValue("name");
                if (StringUtils.isEmpty(name)) {
                    throw new IllegalArgumentException("Error - `" + cls.name + "` has a collection" + " with an empty/null name");
                }
                boolean neo4jIgnore = Boolean.valueOf(attrs.getValue("neo4j-ignore")).booleanValue();
                if (neo4jIgnore) ignoredCollections.add(cls.name+"."+name);
                
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
