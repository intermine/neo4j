package org.intermine.neo4j.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.model.QueryResult;
import org.intermine.neo4j.resource.bean.QueryResultBean;
import org.intermine.neo4j.service.Neo4jQueryService;
import org.intermine.pathquery.PathException;
import org.xml.sax.SAXException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author Yash Sharma
 */
@Path("query")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "Query Results")
public class QueryResource {

    Neo4jQueryService neo4jQueryService = new Neo4jQueryService();

    @GET
    @Path("results")
    @ApiOperation(value = "Get results of a Path Query",
                notes = "API is currently in development. So, response might be unexpected.")
    public QueryResult getResults(@BeanParam QueryResultBean bean) throws Exception {
        String pathQuery = bean.getPathQuery();
        if (pathQuery == null) {
            throw new Exception("Invalid Path Query");
        }
        return neo4jQueryService.getQueryResult(bean);
    }
}
