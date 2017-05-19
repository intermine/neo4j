# intermine
These are Java apps written to perform various tasks using the InterMine PathQuery API.

Compile: ```ant jar```

### org.intermine.FastaQueryClient
Prints the FASTA for the gene with the given symbol. Parameters: IM service URL and gene symbol.

```./run org.intermine.FastaQueryClient https://apps.araport.org/thalemine/service REV```

### org.intermine.ModelViewer
Prints out the full data model from a mine. Parameter: IM service URL.

```./run org.intermine.ModelViewer https://apps.araport.org/thalemine/service```

### org.intermine.neo4j.Neo4jLoader
Loads a mine into Neo4j using parameters in neo4jloader.properties.

```./run org.intermine.neo4j.Neo4jLoader```

### org.intermine.neo4j.Neo4jBatchLoader
A variation on Neo4jLoader which stores up a bunch of queries and submits them to Neo4j as a batch rather than one at a time. Written to compare timing.

```./run org.intermine.neo4j.Neo4jBatchLoader```

### org.intermine.neo4j.Neo4jCompleter
Completes the references and collections for nodes that have only had attributes stored as a result of being loaded as references and collections by Neo4jLoader.
This is very handy to fill out the graph without adding new nodes. neo4jloader.properties is used, especially ```loaded.classes``` to indicate which types of nodes you'd like completed.

```./run org.intermine.neo4j.Neo4jCompleter```

### org.intermine.neo4j.Neo4jNodeLoader
Load a single node into Neo4j identified by its class and InterMine id. Other parameters are read from neo4jloader.properties.

```./run org.intermine.neo4j.Neo4jNodeLoader Gene 4295368``` 

### org.intermine.neo4j.Neo4jEdgeLoader
Load edges into Neo4j given by source class, edge reference or collection, and target as referenced in the edge class. This converts IM objects
which store relations, like Location, into Neo4j edges with properties. Since some target names, e.g. chromosomeLocation, refer to simpler-named classes, like Location, there are two
parameters in neo4jloader.properties, ```intermine.edge.classes``` and ```neo4j.edge.types```, which map IM classes to Neo4j edge types so that, for example,
chromosomeLocation edges are given the type "location" in Neo4j.

```./run org.intermine.neo4j.Neo4jEdgeLoader Gene chromosomeLocation locatedOn```

### webapps
Tomcat webapps for doing interactive demos.

### webapps/ROOT/index.jsp
The webapp page, currently holds a textarea to submit PathQuery XML to...

### org.intermine.neo4j.PathQueryTestServlet
A servlet which receives an XML PathQuery then submits it to: (1) a mine's web service endpoint; (2) the mine using the Java PathQuery API; (3) a Neo4j using a reverse-engineered Cypher query,
which may or may not actually work, and returns all the results along with query execution + mine-to-webapp timing. Parameters are in ```web.xml```.

### org.intermine.neo4j.PathQueryServlet
A servlet which mimics the IM query endpoint: it receives an XML PathQuery with format=tab and then queries Neo4j using a reverse-engineered Cypher query, returning the tabular results.
