# Neo4j

Project to develop Neo4j as the backing store for InterMine. Hosted at http://intermine-neo4jwebapp.herokuapp.com/.

This repository contains following two sub-projects.

- **intermine-neo4j** contains the packages for Neo4j metadata management, PathQuery to Cypher conversion and data loaders for InterMine Neo4j database.

- **intermine-neo4jwebapp** contains the web service API endpoint and servlets from which you can query InterMine Neo4j database.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. We use gradle wrapper for building the project.

### Prerequisites

To run the project you need to get the following prerequisites.
* JDK 8
* Neo4j Graph Database 3.2
* Neo4j APOC 3+

### Installing

- Place the `apoc-3.1.*.jar` file to the plugins directory inside your local neo4j database directory. For example, inside `/home/yourusername/neo4j-community-3.2.0/plugins`.

- Download the latest source code by cloning the git repository.

`git clone https://github.com/intermine/neo4j.git`

- Change the current directory to project directory.
`cd neo4j`

#### intermine-neo4j

- Change the current directory to project directory.
`cd intermine-neo4j`

- Update your Neo4j Database URL, InterMine Service URL, credentials and other information in `neo4jloader.properties` file.

- To build the project, run
`./gradlew build`

- To see the list of all available taks and their description, run
`./gradlew tasks --all`.

- To execute a particular task, run
`./gradlew <taskName>`


#### intermine-neo4jwebapp

- Change the current directory to project directory.
`cd intermine-neo4jwebapp`

- To run a local instance of the server, run
`./gradlew jettyRun`

- For Swagger documentation of the API, visit http://localhost:8080/.

- For testing PathQueryServlets, visit http://localhost:8080/servlet/.

## Documentation

The [project wiki](https://github.com/intermine/neo4j/wiki) contains detailed documentation of the code and the available gradle tasks.
