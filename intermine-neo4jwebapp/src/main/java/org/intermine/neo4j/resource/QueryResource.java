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
@Produces(MediaType.TEXT_PLAIN)
@Api(value = "Query")
public class QueryResource {

    Neo4jQueryService neo4jQueryService = new Neo4jQueryService();

    @GET
    @Path("results")
    @ApiOperation(value = "Returns results of a Path Query")
    public Response getResults(@BeanParam QueryResultBean bean) throws IOException, PathException, ModelParserException, ParserConfigurationException, SAXException {
        String pathQuery = bean.getPathQuery();
        if (pathQuery == null) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Invalid PathQuery")
                    .build();
        }
        QueryResult queryResult;
        try {
            queryResult = neo4jQueryService.getQueryResult(bean);
        }
        catch (IOException e) {
            queryResult = new QueryResult("IOException");
        }
        catch (PathException e) {
            queryResult = new QueryResult("PathException");
        }
        catch (ModelParserException e) {
            queryResult = new QueryResult("ModelParserException");
        }
        catch (ParserConfigurationException e) {
            queryResult = new QueryResult("ParserConfigurationException");
        }
        catch (SAXException e) {
            queryResult = new QueryResult("SAXException");
        }
        return Response.status(Response.Status.ACCEPTED)
                .entity(queryResult)
                .build();
    }
}
