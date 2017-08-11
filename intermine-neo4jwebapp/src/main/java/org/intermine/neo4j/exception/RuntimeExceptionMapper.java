package org.intermine.neo4j.exception;

import org.intermine.neo4j.model.ErrorMessage;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Yash Sharma
 */
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                400);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .build();
    }
}
