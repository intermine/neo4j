package org.intermine.neo4j.metadata;

import org.neo4j.driver.v1.*;

import java.util.*;

/**
 * Generates schema of a given Neo4j database and stores it in the database itself.
 * All nodes of describing the schema are assigned a "Metagraph" label.
 * Metagraph structure described at https://gist.github.com/yasharmaster/8071e53c500081660b9e5c203b913b6d
 *
 * @author Yash Sharma
 */
public class Neo4jSchemaGenerator {

    /**
     * Creates a Metagraph which maps all the nodes and relationships of the database.
     * @param driver Neo4j Java Driver instance.
     */
    public static void generateSchema(Driver driver){
        mapNodes(driver);
        mapRelationships(driver);
    }

    /**
     * Creates NodeType nodes for every type of node that exist in the database.
     * @param driver Neo4j Java Driver instance.
     */
    private static void mapNodes(Driver driver){
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                String query = "MATCH (n)\n" +
                                "WHERE NOT n:Metagraph\n" +
                                "WITH labels(n) as LABELS, keys(n) as KEYS\n" +
                                "MERGE (m:Metagraph {metaType: 'NodeType', labels: LABELS})\n" +
                                "SET m.properties =\n" +
                                "CASE m.properties\n" +
                                "\tWHEN NULL THEN KEYS\n" +
                                "    ELSE apoc.coll.union(m.properties, KEYS)\n" +
                                "END\n";
                tx.run(query);
                tx.success();
                tx.close();
                // Log the progress
                System.out.println("Schema Progress : All nodes mapped.");
            }
        }
    }

    /**
     * Creates RelType nodes for every type of relationship that exist in the database.
     * Also set StartNodeType & EndNodeType relationship for each such node.
     * @param driver Neo4j Java Driver instance.
     */
    private static void mapRelationships(Driver driver){
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                String query = "MATCH (n)-[r]->(m)\n" +
                                "WHERE NOT n:Metagraph AND NOT m:Metagraph\n" +
                                "WITH labels(n) as start_labels, type(r) as rel_type, keys(r) as rel_keys, labels(m) as end_labels\n" +
                                "MERGE (a:Metagraph {metaType:'NodeType', labels: start_labels})\n" +
                                "MERGE (b:Metagraph {metaType:'NodeType', labels: end_labels})\n" +
                                "MERGE (a)<-[:StartNodeType]-(rel:Metagraph {metaType: 'RelType', type:rel_type })-[:EndNodeType]->(b)\n" +
                                "SET rel.properties =\n" +
                                "CASE rel_keys\n" +
                                "\tWHEN NULL THEN []\n" +
                                "    ELSE rel_keys\n" +
                                "END\n";
                tx.run(query);
                tx.success();
                tx.close();
                // Log the progress
                System.out.println("Schema Progress : All relationships mapped.");
            }
        }
    }

    /**
     * Takes a list of objects, converts it into a list of Strings then returns the list after sorting.
     * @param listObjects A list of objects.
     * @return Sorted list of strings.
     */
    private static List<String> processList(List<Object> listObjects){
        List<String> listStrings = new ArrayList<>(listObjects.size());
        for(Object object: listObjects){
            listStrings.add(object.toString());
        }
        Collections.sort(listStrings);
        return listStrings;
    }

    /**
     * Generates a NodeDescriptor from a record.
     * @param record a row of from neo4j result.
     * @return A NodeDescriptor object based on the record.
     */
    private static NodeDescriptor getNodeDescriptor(Record record){
        Set<String> labels = new HashSet<>(processList(record.get("labels").asList()));
        Set<String> properties = new HashSet<>(processList(record.get("properties").asList()));

        return new NodeDescriptor(labels, properties);
    }

    /**
     * Generates a RelationshipDescriptor from a record.
     * @param record a row of from neo4j result.
     * @return A RelationshipDescriptor object based on the record.
     */
    private static RelationshipDescriptor getRelationshipDescriptor(Record record){
        String type = record.get("type").asString();
        Value value = record.get("properties");
        Set<String> properties;
        if(!value.isNull() && !value.isEmpty()){
            properties = new HashSet<>(processList(value.asList()));
        }
        else{
            properties = new HashSet<>();
        }

        return new RelationshipDescriptor(type, properties);
    }

    /**
     * Adds a set to the map of set of sets.
     */
    private static void addSetToMap(Map<String, Set<Set<String>>> map, String key, Set<String> set){
        if(map.containsKey(key)){
            Set<Set<String>> setOfSets = map.get(key);
            setOfSets.add(set);
            map.put(key, setOfSets);
        }
        Set<Set<String>> setOfSets = new HashSet<>();
        setOfSets.add(set);
        map.put(key, setOfSets);
    }

    /**
     * Creates and returns a Model object from the metagraph
     * @param driver Neo4j Java Driver instance.
     * @return Model A model object which represents the metagraph (schema).
     */
    public static Model getModel(Driver driver){
        Set<NodeDescriptor> nodes = new HashSet<>();
        Set<RelationshipDescriptor> relationships = new HashSet<>();

        Map<String, Set<Set<String>>> startNodes = new HashMap<>();
        Map<String, Set<Set<String>>> endNodes = new HashMap<>();

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                String query = "MATCH (n:Metagraph {metaType : 'NodeType'}) " +
                        "RETURN DISTINCT n.labels as labels, n.properties as properties";
                StatementResult result = tx.run(query);

                // For each node create a NodeDescriptor
                while (result.hasNext()) {
                    Record record = result.next();
                    nodes.add(getNodeDescriptor(record));
                }

                query = "MATCH (startNode:Metagraph)<-[:StartNodeType]-" +
                        "(n:Metagraph {metaType : 'RelType'})-[:EndNodeType]->" +
                        "(endNode:Metagraph) RETURN DISTINCT startNode.labels as startLabels, " +
                        "endNode.labels as endLabels, n.type as type, n.properties as properties";
                result = tx.run(query);

                // For relationship, create a RelationshipDescriptor and store start and end nodes
                while (result.hasNext()) {
                    Record record = result.next();
                    relationships.add(getRelationshipDescriptor(record));
                    String key = record.get("type").asString();
                    Set<String> startSet = new HashSet<>(processList(record.get("startLabels").asList()));
                    Set<String> endSet = new HashSet<>(processList(record.get("endLabels").asList()));
                    addSetToMap(startNodes, key, startSet);
                    addSetToMap(endNodes, key, endSet);
                }

                tx.success();
                tx.close();

                // Log the progress
                System.out.println("Model successfully extracted from the database.");
            }
        }
        Model model = new Model(nodes, relationships, startNodes, endNodes);
        return model;
    }
}
