package org.intermine.neo4j.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Handles logic of "test/*" api endpoints.
 *
 * @author Yash Sharma
 */
@Path("test")
@Produces(MediaType.TEXT_PLAIN)
@Api(value = "Test")
public class TestResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Endpoint for testing")
    public Response getResponse(@QueryParam("name") String name) throws Exception {
        if (name == null) {
            name = "Anon";
        }
        return Response.ok("Hey " + name + "!")
                .build();
    }
}
