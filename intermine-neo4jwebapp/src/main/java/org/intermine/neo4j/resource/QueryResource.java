package org.intermine.neo4j.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.intermine.neo4j.model.ColumnHeadersType;
import org.intermine.neo4j.model.FormatType;
import org.intermine.neo4j.resource.bean.QueryResultBean;

import java.net.URI;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author Yash Sharma
 */
@Path("query")
@Produces(MediaType.TEXT_PLAIN)
@Api(value = "Query")
public class QueryResource {

    @GET
    @Path("results")
    @ApiOperation(value = "Returns results of a Path Query")
    public String getResults(@BeanParam QueryResultBean queryResultBean) {
        String response = "";
        String pathQuery = queryResultBean.getPathQuery();
        int version = queryResultBean.getVersion();
        int start = queryResultBean.getStart();
        int size = queryResultBean.getSize();
        ColumnHeadersType columnHeadersType = queryResultBean.getColumnHeadersType();
        FormatType formatType = queryResultBean.getFormatType();

        if (pathQuery == null) {
            response = "Please enter a valid path query";
            return response;
        }
        response += pathQuery + "\n";
        if (version != -1) {
            response += String.valueOf(version) + "\n";
        }
        if (start != -1) {
            response += String.valueOf(start) + "\n";
        }
        if (size != -1) {
            response += String.valueOf(size) + "\n";
        }
        if (columnHeadersType != null) {
            response += columnHeadersType.name() + "\n";
        }
        if (formatType != null) {
            response += formatType.name() + "\n";
        }
        return response;
    }
}
