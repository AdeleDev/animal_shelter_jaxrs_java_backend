package org.demo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * This class is custom handler of validation exception
 */
//@org.apache.cxf.annotations.Provider(OutFaultInterceptor)
@Component
public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
    private final static Logger logger = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    private final static String RESPONSE_MESSAGE = "Validation exception - ";

    public Response toResponse(ConstraintViolationException exception) {
        logger.error(RESPONSE_MESSAGE + exception.getMessage());
        return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity(exception.getMessage()).build();
    }
}
