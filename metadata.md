# Metadata in Neo4j

Description of the Metagraph which is used to store the schema for the Neo4j database.

## Introduction

[InterMine](http://intermine.org/), as a data warehousing system, stores biological data which is loaded from various data sources. It is imperative to ensure that the loaded data conforms to the existing schema/model so as to maintain the data integrity.

Presently the data model in InterMine is stored in an external [XML file](https://github.com/intermine/intermine/blob/dev/bio/core/core.xml). For each entity, its attributes, references and collections are stored in this XML file. For example, a part of the file which has the BioEntity model looks like this.

```XML
<class name="BioEntity" is-interface="true">
    <attribute name="primaryIdentifier" type="java.lang.String"/>
    <attribute name="secondaryIdentifier" type="java.lang.String"/>
    <attribute name="symbol" type="java.lang.String"/>
    <attribute name="name" type="java.lang.String"/>
    <reference name="organism" referenced-type="Organism"/>
    <collection name="locatedFeatures" referenced-type="Location" reverse-reference="locatedOn"/>
    <collection name="locations" referenced-type="Location" reverse-reference="feature" />
    <collection name="ontologyAnnotations" referenced-type="OntologyAnnotation" reverse-reference="subject"/>
    <collection name="synonyms" referenced-type="Synonym" reverse-reference="subject"/>
    <collection name="dataSets" referenced-type="DataSet" reverse-reference="bioEntities"/>
    <collection name="publications" referenced-type="Publication" reverse-reference="bioEntities"/>
    <collection name="crossReferences" referenced-type="CrossReference" reverse-reference="subject"/>
</class>
```

While developing Neo4j prototype of InterMine, to reduce the dependency on external files, it was decided to store the schema in the database itself. Neo4j being a graph database stores data in the form of Nodes and directed Relationships. This brought up two issues:
1. The data model must to be in the form of a graph itself so that it can be stored in the Neo4j database.
2. The data model should represent all the existing Nodes in the database and the Relationships among them.

Since the data model is a graph and it stores information about the InterMine graph, it can be called a metagraph.

## Metagraph Structure

Each node in the metagraph is assigned `:Metagraph` label. All the metagraph nodes are further classified into two types, NodeType and RelType. As the name suggests, each NodeType node represents a specific type of nodes and each RelType node represents a specific type of relationship in the IM graph. So each node has either `:NodeType` or `:RelType` label depending on which entity it represents.

#### NodeType 

A metagraph node labelled `:NodeType` contains following two properties.
1. **labels** - A list containing the labels of the nodes that are represented by the `:NodeType` node. For example, ["Gene","SequenceFeature","BioEntity"].
2. **keys** - A list containing the keys of all the properties exist amongst the nodes that are represented by the `:NodeType` node. For example, ["primaryIdentifier", "secondaryIdentifier", "symbol"].

Each `:NodeType` node is uniquely identified by its labels property. So, for all the nodes in the IM graph which are labelled `:Gene`, `:SequenceFeature`, `:BioEntity`, there exists one `:NodeType` node in the metagraph which has its labels property set as ["Gene","SequenceFeature","BioEntity"].

#### RelType 

A metagraph node labelled `:RelType` contains following two properties.
1. **type** - A string denoting the `type` of the relationships that are represented by the `:RelType` node. For example, "HOMOLOGUE_OF".
2. **keys** - A list containing the keys of all the properties exist amongst the relationships that are represented by the `:RelType` node. For example, ["DataSet"].

Each `:RelType` node is uniquely identified by its type property. So, for all the relationships of type HOMOLOGUE_OF in the IM graph, there exists one `:RelType` node in the metagraph which has its type property set as "HOMOLOGUE_OF".

#### Relationships in MetaGraph

Metagraph should not only contain information about the properties of various entities in the IM graph but it should also contain store how different types of nodes are connected to each other. To represent this information, we make use of Neo4j relationships.

We know that each `:RelType` node represents a type of relationships that exist in the IM graph. Now, we create two outgoing relationships/edges from each `:RelType` node - `:StartNodeType` and `:EndNodeType`. These edges end on a `:NodeType` node.

Thus the metagraph path `(a:RelType)-[:StartNodeType]->(b:NodeType)` shows that the relationships represented by node `a` starts from the nodes represented by the node `b`. Same case follows for `:EndNodeType`.

## Generating MetaGraph

#### Representing Nodes

The following Cypher query, creates `:NodeType` nodes for all the nodes that exist in the IM graph.

```
MATCH (n)
WHERE NOT n:Metagraph AND size(labels(n))>0
WITH labels(n) as LABELS, keys(n) as KEYS
MERGE (m:Metagraph:NodeType {labels: LABELS})
SET m.properties =
CASE m.properties
	WHEN NULL THEN KEYS
    ELSE apoc.coll.union(m.properties, KEYS)
END
```

#### Representing Relationships

The following Cypher query, creates `:RelType` nodes for all the relationships that exist in the IM graph. It also connects them to the `:NodeType` nodes with `:StartNodeType` and `:EndNodeType` relationships.

```
MATCH (n)-[r]->(m)
WHERE NOT n:Metagraph AND NOT m:Metagraph
WITH labels(n) as start_labels, type(r) as rel_type, keys(r) as rel_keys, labels(m) as end_labels
MERGE (a:Metagraph:NodeType {labels: start_labels})
MERGE (b:Metagraph:NodeType {labels: end_labels})
MERGE (rel:Metagraph:RelType {type:rel_type})
MERGE (a)<-[:StartNodeType]-(rel)-[:EndNodeType]->(b)
SET rel.properties =
CASE
    WHEN rel.properties IS NULL AND rel_keys IS NULL THEN []
    WHEN rel.properties IS NULL AND rel_keys IS NOT NULL THEN rel_keys
    WHEN rel.properties IS NOT NULL AND rel_keys IS NULL THEN rel.properties
    WHEN rel.properties IS NOT NULL AND rel_keys IS NOT NULL THEN apoc.coll.union(rel.properties, rel_keys)
END
```