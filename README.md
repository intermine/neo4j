# Neo4j

Project to develop Neo4j as the backing store for InterMine.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

Download the latest source code by cloning the git repository.

`git clone https://github.com/intermine/neo4j.git`

### Prerequisites

Download the following prerequisites from their official websites.

* JDK 8
* Apache Tomcat 7
* Neo4j Graph Database 3.2
* Neo4j APOC 3.1+

### Installing

Place the `apoc-3.1.*.jar` file to the plugins directory inside the neo4j home directory. For example, `/home/yourusername/neo4j-community-3.2.0/plugins`.

Install the remaining prerequisites as per the instructions given in their official websites.

## Deployment


Change the current directory to project home by running ```cd path/to/project/```.

Build the project JAR using Apache Ant by running ```ant jar```.

#### org.intermine.neo4j.Neo4jLoader
Loads a mine into Neo4j using parameters in neo4jloader.properties.

```./run Neo4jLoader```

#### org.intermine.neo4j.Neo4jBatchLoader
A variation on Neo4jLoader which stores up a bunch of queries and submits them to Neo4j as a batch rather than one at a time. Written to compare timing.

```./run Neo4jBatchLoader```

#### org.intermine.neo4j.Neo4jCompleter
Completes the references and collections for nodes that have only had attributes stored as a result of being loaded as references and collections by Neo4jLoader.
This is very handy to fill out the graph without adding new nodes. neo4jloader.properties is used, especially ```loaded.classes``` to indicate which types of nodes you'd like completed.

```./run Neo4jCompleter```

#### org.intermine.neo4j.Neo4jNodeLoader
Load a single node into Neo4j identified by its class and InterMine id. Other parameters are read from neo4jloader.properties.

```./run Neo4jNodeLoader Gene 4295368```

#### org.intermine.neo4j.Neo4jEdgeLoader
Load edges into Neo4j given by source class, edge reference or collection, and target as referenced in the edge class. This converts IM objects
which store relations, like Location, into Neo4j edges with properties. Since some target names, e.g. chromosomeLocation, refer to simpler-named classes, like Location, there are two
parameters in neo4jloader.properties, ```intermine.edge.classes``` and ```neo4j.edge.types```, which map IM classes to Neo4j edge types so that, for example,
chromosomeLocation edges are given the type "location" in Neo4j.

```./run Neo4jEdgeLoader Gene chromosomeLocation locatedOn```

#### org.intermine.neo4j.InterMineModelXMLDumper
Simple utility dump an InterMine model in XML form to standard output.

```./run InterMineModelXMLDumper http://www.synbiomine.org/synbiomine/service```

#### org.intermine.neo4j.TestSchemaGenerator
Generates the schema of the Neo4j database using org.intermine.neo4j.metadata package and outputs the model to the console. Also, tests each validation method in the Model class.

```./run TestSchemaGenerator```

#### org.intermine.neo4j.Neo4jModelParser
Parses a Neo4j-annotated model XML file, which contains instructions like neo4j-include="true" in the class, attribute, reference and collection definitions to indicate that
those items should be ignored in Neo4j loading. The main method takes an XML file as a parameter and spits out the data model with "X" denoting ignored items.
Methods like isIgnored(ReferenceDescriptor) provide an easy way to see if a given reference (for example) should be ignored during loading.

#### webapps
Tomcat webapps for doing interactive demos.

#### webapps/ROOT/index.jsp
The webapp page, currently holds a textarea to submit PathQuery XML to...

#### org.intermine.neo4j.PathQueryTestServlet
A servlet which receives an XML PathQuery then submits it to: (1) a mine's web service endpoint; (2) the mine using the Java PathQuery API; (3) a Neo4j using a reverse-engineered Cypher query,
which may or may not actually work, and returns all the results along with query execution + mine-to-webapp timing. Parameters are in ```web.xml```.

#### org.intermine.neo4j.PathQueryServlet
A servlet which mimics the IM query endpoint: it receives an XML PathQuery with format=tab and then queries Neo4j using a reverse-engineered Cypher query, returning the tabular results.
