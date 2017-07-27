package org.intermine.neo4j.service;

import org.intermine.neo4j.Neo4jLoaderProperties;
import org.intermine.neo4j.cypher.CypherQuery;
import org.intermine.neo4j.cypher.QueryGenerator;
import org.intermine.neo4j.model.QueryResult;
import org.intermine.neo4j.resource.bean.QueryResultBean;
import org.intermine.pathquery.PathQuery;
import org.intermine.webservice.client.services.QueryService;

/**
 * @author Yash Sharma
 */
public class Neo4jQueryService {

    public QueryResult getQueryResult(QueryResultBean bean)  {
        // Currently returns only the converted cypher query.

        Neo4jLoaderProperties properties;
        try {
            properties = new Neo4jLoaderProperties();
        } catch (Exception e) {
            return new QueryResult(e.getStackTrace().toString());
        }
        QueryService service = properties.getQueryService();
        PathQuery pathQuery = service.createPathQuery(bean.getPathQuery());
        CypherQuery cypherQuery;
        try {
            cypherQuery = QueryGenerator.pathQueryToCypher(pathQuery);
        } catch (Exception e) {
            return new QueryResult(e.getStackTrace().toString());
        }
        String response = cypherQuery.toString();
        return new QueryResult(response);
    }
}
