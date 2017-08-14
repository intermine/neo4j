package org.intermine.neo4j.exception;

import org.intermine.neo4j.model.ErrorMessage;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author Yash Sharma
 */
@Provider
public class ParserConfigurationExceptionMapper implements ExceptionMapper<ParserConfigurationException> {

    @Override
    public Response toResponse(ParserConfigurationException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                500);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorMessage)
                .build();
    }
}
