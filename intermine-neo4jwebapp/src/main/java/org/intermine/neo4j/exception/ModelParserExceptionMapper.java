package org.intermine.neo4j.exception;

import org.intermine.metadata.ModelParserException;
import org.intermine.neo4j.model.ErrorMessage;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Yash Sharma
 */
@Provider
public class ModelParserExceptionMapper implements ExceptionMapper<ModelParserException> {

    @Override
    public Response toResponse(ModelParserException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                500);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorMessage)
                .build();
    }
}
