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
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author Yash Sharma
 */
public class Neo4jQueryService {

    public QueryResult getQueryResult(QueryResultBean bean) throws IOException, PathException, ModelParserException, ParserConfigurationException, SAXException {
        Neo4jLoaderProperties properties = new Neo4jLoaderProperties();
        QueryService service = properties.getQueryService();
        PathQuery pathQuery = service.createPathQuery(bean.getPathQuery());
        CypherQuery cypherQuery = QueryGenerator.pathQueryToCypher(pathQuery);

        return new QueryResult(cypherQuery.toString());
    }
}
