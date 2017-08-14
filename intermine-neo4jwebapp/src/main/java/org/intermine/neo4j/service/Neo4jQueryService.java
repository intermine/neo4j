package org.intermine.neo4j.service;

import org.intermine.metadata.ClassDescriptor;
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
import org.xml.sax.SAXException;

import javax.activation.UnsupportedDataTypeException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.Date;

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

    public QueryResult getQueryResult(QueryResultBean bean) throws PathException, IOException, ModelParserException, ParserConfigurationException, SAXException {
        Neo4jLoaderProperties properties = new Neo4jLoaderProperties();
        PathQuery pathQuery = properties.getQueryService()
                                        .createPathQuery(bean.getPathQuery());
        CypherQuery cypherQuery = getCypherQuery(bean.getPathQuery());

        int size = bean.getSize();
        if (size > 0) {
            cypherQuery.setResultRowsLimit(size);
        }

        int start = bean.getStart();
        if (start > 0) {
            cypherQuery.setResultRowsSkip(start);
        }

        QueryResult queryResult = new QueryResult();

        queryResult.setRootClass(pathQuery.getRootClass());
        queryResult.setModelName(pathQuery.getModel().getName());
        queryResult.setStart(bean.getStart());
        queryResult.setViews(pathQuery.getView());

        List<String> headersList = new ArrayList<>();
        Model model = new Neo4jLoaderProperties().getModel();
        for (String view : pathQuery.getView()) {
            Path path = new Path(model, view);
            headersList.add(getColumnHeader(path));
        }
        queryResult.setColumnHeaders(headersList);

        queryResult.setResults(getResultsFromNeo4j(properties.getGraphDatabaseDriver(),
                                                    cypherQuery,
                                                    pathQuery));

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        queryResult.setExecutionTime(dateFormat.format(new Date()));

        queryResult.setSuccessful(true);
        queryResult.setError(null);
        queryResult.setStatusCode(200);

        return queryResult;
    }

    private static Object getValueFromRecord(Record record, String key, String view) throws IOException, ModelParserException, PathException {
        Value value = record.get(key);
        if (value.isNull()) {
            return null;
        }
        Model model = new Neo4jLoaderProperties().getModel();
        Path path = new Path(model, view);
        switch (path.getEndType().getName()) {
            case "java.lang.Integer":
                return new Integer(value.asInt());
            case "java.lang.Double":
                return new Double(value.asDouble());
            case "java.lang.Boolean":
                return new Boolean(value.asBoolean());
            case "java.lang.Float":
                return new Float(value.asFloat());
            case "java.lang.String":
                return new String(value.asString());
            default:
                throw new UnsupportedDataTypeException("Data Type " +
                        value.getClass().getName() +
                        " Neo4jQueryService.getValueFromRecord()");
        }
    }

    public static List<List<Object>> getResultsFromNeo4j(Driver driver, CypherQuery cypherQuery, PathQuery pathQuery) throws IOException, ModelParserException, PathException {
        // execute the Cypher query and load results into a list of tab-delimited strings
        List<List<Object>> resultsList = new ArrayList<>();
        Session session = driver.session();
        Transaction tx = session.beginTransaction();
        StatementResult result = tx.run(cypherQuery.toString());
        while (result.hasNext()) {
            Record record = result.next();
            List<Object> resultList = new ArrayList<>();
            for (String view : pathQuery.getView()) {
                String variableName = cypherQuery.getVariable(view);
                Object value;
                try {
                    value = getValueFromRecord(record, variableName, view);
                    resultList.add(value);
                } catch (Exception e) {
                    resultList.add(e.toString());
                }
            }
            resultsList.add(resultList);
        }
        return resultsList;
    }

    private static String getColumnHeader(Path path) {
        String header = "";
        for (ClassDescriptor classDescriptor : path.getElementClassDescriptors()) {
            header += classDescriptor.getSimpleName() + " > ";
        }
        header += path.getLastElement();
        return header;
    }
}
