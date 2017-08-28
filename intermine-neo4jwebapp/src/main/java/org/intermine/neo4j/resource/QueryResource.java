package org.intermine.neo4j.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.intermine.neo4j.cypher.CypherQuery;
import org.intermine.neo4j.resource.bean.QueryCypherBean;
import org.intermine.neo4j.resource.bean.QueryResultBean;
import org.intermine.neo4j.service.Neo4jQueryService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Handles logic of "query/*" api endpoints.
 *
 * @author Yash Sharma
 */
@Path("query")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
@Api(value = "Query")
public class QueryResource {

    Neo4jQueryService neo4jQueryService = new Neo4jQueryService();

    @GET
    @Path("results")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Query InterMine Neo4j Graph with a Path Query.",
                notes = "API is currently in development. Report any issues at https://github.com/intermine/neo4j/issues.")
    public Response getResults(@BeanParam QueryResultBean bean) throws Exception {
        String pathQuery = bean.getPathQuery();
        if (pathQuery == null) {
            throw new BadRequestException("Invalid Path Query.");
        }
        if (bean.getSize() < 0) {
            throw new BadRequestException("Invalid size parameter: " + String.valueOf(bean.getSize()));
        }
        if (bean.getStart() < 0) {
            throw new BadRequestException("Invalid start parameter: " + String.valueOf(bean.getStart()));
        }
        return Response.ok(neo4jQueryService.getQueryResult(bean).toJSON().toString())
                .build();
    }

    @GET
    @Path("cypher")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Convert a PathQuery to Cypher.",
            notes = "API is currently in development. Report any issues at https://github.com/intermine/neo4j/issues.")
    public String getCypher(@BeanParam QueryCypherBean bean) throws Exception {
        if (bean.getPathQuery() == null) {
            throw new BadRequestException("Invalid Path Query");
        }
        CypherQuery cypherQuery = neo4jQueryService.getCypherQuery(bean.getPathQuery());
        if (bean.getSize() > 0) {
            cypherQuery.setResultRowsLimit(bean.getSize());
        }
        if (bean.getStart() > 0) {
            cypherQuery.setResultRowsSkip(bean.getStart());
        }
        return cypherQuery.toString();
    }
}
