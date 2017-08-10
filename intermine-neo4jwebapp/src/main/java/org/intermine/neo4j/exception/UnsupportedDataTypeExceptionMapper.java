package org.intermine.neo4j.exception;

import org.intermine.neo4j.model.ErrorMessage;

import javax.activation.UnsupportedDataTypeException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Yash Sharma
 */
@Provider
public class UnsupportedDataTypeExceptionMapper implements ExceptionMapper<UnsupportedDataTypeException> {

    @Override
    public Response toResponse(UnsupportedDataTypeException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                500);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorMessage)
                .build();
    }
}
