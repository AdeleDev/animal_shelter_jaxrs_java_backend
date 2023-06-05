package org.demo.handler;


import org.demo.exceptions.PetNotExistException;
import org.demo.exceptions.UserNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@Component
public class PetNotExistExceptionHandler implements ExceptionMapper<PetNotExistException> {
    private final static Logger LOGGER = LoggerFactory.getLogger(PetNotExistExceptionHandler.class);

    @Override
    public Response toResponse(PetNotExistException exception) {
        LOGGER.error(exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}