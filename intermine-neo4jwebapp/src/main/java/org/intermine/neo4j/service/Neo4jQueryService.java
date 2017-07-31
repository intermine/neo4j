package org.intermine.neo4j.service;

import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.neo4j.cypher.CypherQuery;
import org.intermine.neo4j.cypher.QueryGenerator;
import org.intermine.neo4j.model.QueryResult;
import org.intermine.neo4j.resource.bean.QueryResultBean;
import org.intermine.pathquery.PathException;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;
import org.neo4j.driver.v1.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yash Sharma
 */
public class Neo4jQueryService {

    public QueryResult getQueryResult(QueryResultBean bean) throws PathException, ModelParserException, ParserConfigurationException, SAXException, IOException {
        // Currently returns only the converted cypher query.

        Neo4jLoaderProperties properties;
        properties = new Neo4jLoaderProperties();
        QueryService service = properties.getQueryService();
        PathQuery pathQuery = service.createPathQuery(bean.getPathQuery());
        CypherQuery cypherQuery;
        cypherQuery = QueryGenerator.pathQueryToCypher(pathQuery);
        return getResultsFromNeo4j(properties.getGraphDatabaseDriver(),
                                            cypherQuery,
                                            pathQuery);
    }

    private static QueryResult getResultsFromNeo4j(Driver driver, CypherQuery cypherQuery, PathQuery pathQuery) {
        // execute the Cypher query and load results into a list of tab-delimited strings
        List<List<String>> resultsList = new ArrayList<>();
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run(cypherQuery.toString());
                while (result.hasNext()) {
                    Record record = result.next();
                    List<String> resultList = new ArrayList<>();
                    for (String view : pathQuery.getView()) {
                        String variableName = cypherQuery.getVariable(view);
                        try {
                            resultList.add(String.valueOf(record.get(variableName)));
                        } catch (Exception e) {
                            resultList.add(e.toString());
                        }
                    }
                    resultsList.add(resultList);
                }
            }
        }
        List<String> headerList = new ArrayList<>();
        for (String view : pathQuery.getView()) {
            headerList.add(view);
        }
        return new QueryResult(headerList, resultsList);
    }

}
