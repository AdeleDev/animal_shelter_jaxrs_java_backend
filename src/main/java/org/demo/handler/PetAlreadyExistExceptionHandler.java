package org.demo.handler;


import org.demo.exceptions.PetAlreadyExistsException;
import org.demo.exceptions.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@Component
public class PetAlreadyExistExceptionHandler implements ExceptionMapper<PetAlreadyExistsException> {
    private final static Logger LOGGER = LoggerFactory.getLogger(PetAlreadyExistExceptionHandler.class);

    @Override
    public Response toResponse(PetAlreadyExistsException exception) {
        LOGGER.error(exception.getMessage());
        return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
    }
}