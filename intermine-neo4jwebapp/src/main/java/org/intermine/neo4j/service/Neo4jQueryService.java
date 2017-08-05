package org.intermine.neo4j.service;

import org.intermine.metadata.Model;
import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.neo4j.cypher.CypherQuery;
import org.intermine.neo4j.cypher.QueryGenerator;
import org.intermine.neo4j.model.QueryResult;
import org.intermine.neo4j.resource.bean.QueryResultBean;
import org.intermine.pathquery.Path;
import org.intermine.pathquery.PathException;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.value.Uncoercible;
import org.xml.sax.SAXException;

import javax.activation.UnsupportedDataTypeException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yash Sharma
 */
public class Neo4jQueryService {

    public CypherQuery getCypherQuery(String pathQueryString) throws IOException, PathException, ModelParserException, ParserConfigurationException, SAXException {
        Neo4jLoaderProperties properties = new Neo4jLoaderProperties();
        QueryService service = properties.getQueryService();
        PathQuery pathQuery = service.createPathQuery(pathQueryString);

        return QueryGenerator.pathQueryToCypher(pathQuery);
    }

    public QueryResult getQueryResult(QueryResultBean bean) throws PathException, ModelParserException, ParserConfigurationException, SAXException, IOException {
        Neo4jLoaderProperties properties = new Neo4jLoaderProperties();
        PathQuery pathQuery = properties.getQueryService()
                                        .createPathQuery(bean.getPathQuery());
        CypherQuery cypherQuery = getCypherQuery(bean.getPathQuery());

        return getResultsFromNeo4j(properties.getGraphDatabaseDriver(),
                                    cypherQuery,
                                    pathQuery);
    }

    private static String getValueFromRecord(Record record, String key, String view) throws IOException, ModelParserException, PathException {
        Value value = record.get(key);
        if (value.isNull()) {
            return null;
        }
        Model model = new Neo4jLoaderProperties().getModel();
        Path path = new Path(model, view);
        switch (path.getEndType().getName()) {
            case "java.lang.Integer":
                return String.valueOf(value.asInt());
            case "java.lang.Double":
                return String.valueOf(value.asDouble());
            case "java.lang.Boolean":
                return String.valueOf(value.asBoolean());
            case "java.lang.Float":
                return String.valueOf(value.asFloat());
            case "java.lang.String":
                return value.asString();
            default:
                throw new UnsupportedDataTypeException("Data Type not supported in Neo4jQueryService.getValueFromRecord().");
        }
    }

    public static QueryResult getResultsFromNeo4j(Driver driver, CypherQuery cypherQuery, PathQuery pathQuery) {
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
                        String value;
                        try {
                            value = getValueFromRecord(record, variableName, view);
                            resultList.add(value);
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
