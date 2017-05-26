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
 * Parse a Neo4j-modified InterMine metadata XML file to provide methods pertaining to loading data into a Neo4j graph.
 *
 * @author Sam Hokin
 */
public class Neo4jModelParser {

    // core IM stuff
    Model model;
    String modelName;
    String packageName;
    int version;

    // Neo4j stuff
    Set<String> ignoredClasses;

    /**
     * Constructor
     */
    public Neo4jModelParser() {
        ignoredClasses = new LinkedHashSet<String>();
        ignoredClasses.add("InterMineObject"); // doesn't appear in XML files but most certainly should be ignored!
    }

    /**
     * Simple main method that spits out Neo4j information extracted from a model XML file on the command line
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, SAXException, ParserConfigurationException, ModelParserException {
        if (args.length!=1) {
            System.out.println("Usage: Neo4jModelParser <XML file>");
            System.exit(0);
        }
        String dataModelFilename = args[0];

        // process the XML file
        Neo4jModelParser nmp = new Neo4jModelParser();
        nmp.process(dataModelFilename);

        // output
        System.out.println("file\t\t"+dataModelFilename);
        System.out.println("modelName\t"+nmp.modelName);
        System.out.println("packageName\t"+nmp.packageName);
        System.out.println("version\t\t"+nmp.version);
        System.out.println("--------------------------------");
        for (ClassDescriptor classDescriptor : nmp.model.getClassDescriptors()) {
            boolean ignored = nmp.ignoredClasses.contains(classDescriptor.getSimpleName());
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
                    System.out.println("\ta "+ad.getName());
                }
                // show references
                Set<ReferenceDescriptor> refDescriptors = classDescriptor.getReferenceDescriptors();
                for (ReferenceDescriptor rd : refDescriptors) {
                    String refClass = rd.getReferencedClassDescriptor().getSimpleName();
                    if (nmp.ignoredClasses.contains(refClass)) System.out.print("X");
                    System.out.println("\tr "+rd.getName());
                }
                // show collections
                Set<CollectionDescriptor> collDescriptors = classDescriptor.getCollectionDescriptors();
                for (CollectionDescriptor cd : collDescriptors) {
                    String collClass = cd.getReferencedClassDescriptor().getSimpleName();
                    if (nmp.ignoredClasses.contains(collClass)) System.out.print("X");
                    System.out.println("\tc "+cd.getName());
                }
            }
        }

    }

    /**
     * Read source model information in InterMine-Neo4j XML format and populate various sets and maps with Neo4j loading properties.
     * Also returns the IM model, just as InterMineModelParser does.
     *
     * @param dataModelFilename the source model XML file to parse
     * @return an InterMine Model
     * @throws ModelParserException if something goes wrong with parsing the class descriptors.
     */
    public Model process(String dataModelFilename) throws IOException, SAXException, ParserConfigurationException, ModelParserException {
        // we'll want the IM model for reference
        InterMineModelParser immp = new InterMineModelParser();
        model = immp.process(new InputStreamReader(new FileInputStream(dataModelFilename)));
        // now parse the file again, loading Neo4j relevant stuff
        ModelHandler handler = new ModelHandler();
        SAXParser.parse(new InputSource(new InputStreamReader(new FileInputStream(dataModelFilename))), handler);
        // return the IM model just as usual
        return model;
    }

    /**
     * Read source model information in InterMine XML format and create a set of ClassDescriptors.
     *
     * @param reader the source model to parse
     * @param packageName the package name that all the classes should be in
     * @return a set of ClassDescriptors
     * @throws ModelParserException if something goes wrong with parsing the class descriptors.
     */
    // @Override
    // public Set<ClassDescriptor> generateClassDescriptors(Reader reader, String packageName) throws ModelParserException {
    //     try {
    //         ModelHandler handler = new ModelHandler();
    //         handler.packageName = packageName;
    //         SAXParser.parse(new InputSource(reader), handler);
    //         return handler.classes;
    //     } catch (Exception e) {
    //         throw new ModelParserException(e);
    //     }
    // }

    /**
     * Extension of DefaultHandler to handle IM-Neo4j metadata file
     */
    class ModelHandler extends DefaultHandler {
        SkeletonClass cls;

        /**
         * {@inheritDoc}
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attrs) {
            
            if ("model".equals(qName)) {
                modelName = attrs.getValue("name");
                packageName = attrs.getValue("package");
                String versionString = attrs.getValue("version");
                if (versionString != null) {
                    version = Integer.parseInt(versionString);
                }
                
            } else if ("class".equals(qName)) {
                String name = attrs.getValue("name");
                String supers = attrs.getValue("extends");
                boolean isInterface = Boolean.valueOf(attrs.getValue("is-interface")).booleanValue();
                boolean neo4jIgnore = Boolean.valueOf(attrs.getValue("neo4j-ignore")).booleanValue();
                cls = new SkeletonClass(packageName, name, supers, isInterface, neo4jIgnore);
                
            } else if ("attribute".equals(qName)) {
                String name = attrs.getValue("name");
                String type = attrs.getValue("type");
                if (StringUtils.isEmpty(name)) {
                    throw new IllegalArgumentException("Error - `" + cls.name + "` has an attribute" + " with an empty/null name");
                }
                if (attrs.getType("type") == null) {
                    throw new IllegalArgumentException("Error - type of attribute `" + name + "` not defined for `" + cls.name + "`");
                }
                cls.attributes.add(new AttributeDescriptor(name, type));
                
            } else if ("reference".equals(qName)) {
                String name = attrs.getValue("name");
                String origType = attrs.getValue("referenced-type");
                String type = origType;
                if (StringUtils.isEmpty(name)) {
                    throw new IllegalArgumentException("Error - `" + cls.name + "` has a reference" + " with an empty/null name");
                }
                if (type == null) {
                    throw new IllegalArgumentException("Error - type of reference `" + name + "` not defined for `" + cls.name + "`");
                }
                if (type.startsWith(packageName + ".")) {
                    type = type.substring(packageName.length() + 1);
                }
                if (type.contains(".")) {
                    throw new IllegalArgumentException("Class " + origType + " in reference " + cls.name + "." + name + " is not in the model package " + packageName);
                }
                if (!"".equals(packageName)) {
                    type = packageName + "." + type;
                }
                String reverseReference = attrs.getValue("reverse-reference");
                cls.references.add(new ReferenceDescriptor(name, type, reverseReference));
                
            } else if ("collection".equals(qName)) {
                String name = attrs.getValue("name");
                String origType = attrs.getValue("referenced-type");
                String type = origType;
                if (StringUtils.isEmpty(name)) {
                    throw new IllegalArgumentException("Error - `" + cls.name + "` has a collection" + " with an empty/null name");
                }
                if (type == null) {
                    throw new IllegalArgumentException("Error - `" + name + "` collection missing a type for `" + cls.name + "`");
                }
                if (type.startsWith(packageName + ".")) {
                    type = type.substring(packageName.length() + 1);
                }
                if (type.contains(".")) {
                    throw new IllegalArgumentException("Class " + origType + " in reference " + cls.name + "." + name + " is not in the model package " + packageName);
                }
                if (!"".equals(packageName)) {
                    type = packageName + "." + type;
                }
                String reverseReference = attrs.getValue("reverse-reference");
                cls.collections.add(new CollectionDescriptor(name, type, reverseReference));
                
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if ("class".equals(qName)) {
                String[] parts = cls.name.split("\\.");
                String className = parts[parts.length-1];
                if (cls.neo4jIgnore) ignoredClasses.add(className);
            }
        }
        
    }

    /**
     * Semi-constructed ClassDescriptor
     */
    static class SkeletonClass {
        String name;
        String supers;
        boolean isInterface;
        boolean neo4jIgnore;
        Set<AttributeDescriptor> attributes = new LinkedHashSet<AttributeDescriptor>();
        Set<ReferenceDescriptor> references = new LinkedHashSet<ReferenceDescriptor>();
        Set<CollectionDescriptor> collections = new LinkedHashSet<CollectionDescriptor>();

        /**
         * Constructor.
         *
         * @param packageName the name of the model package
         * @param name the fully qualified name of the described class
         * @param supers a space string of fully qualified class names
         * @param isInterface true if describing an interface
         */
        SkeletonClass(String packageName, String name, String supers, boolean isInterface, boolean neo4jIgnore) {
            this.name = name;
            if (this.name.startsWith(packageName + ".")) {
                this.name = this.name.substring(packageName.length() + 1);
            }
            if (this.name.contains(".")) {
                throw new IllegalArgumentException("Class " + name + " is not in the model package " + packageName);
            }
            if (!"".equals(packageName)) {
                this.name = packageName + "." + this.name;
            }
            if (supers != null) {
                String[] superNames = supers.split(" ");
                StringBuilder supersBuilder = new StringBuilder();
                boolean needComma = false;
                for (String superName : superNames) {
                    String origSuperName = superName;
                    if (superName.startsWith(packageName + ".")) {
                        superName = superName.substring(packageName.length() + 1);
                    }
                    if (!"java.lang.Object".equals(superName)) {
                        if (superName.contains(".")) {
                            throw new IllegalArgumentException("Superclass " + origSuperName + " of class " + this.name + " is not in the model package " + packageName);
                        }
                        if (!"".equals(packageName)) {
                            superName = packageName + "." + superName;
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
            this.isInterface = isInterface;
            this.neo4jIgnore = neo4jIgnore;
        }
    }

}
