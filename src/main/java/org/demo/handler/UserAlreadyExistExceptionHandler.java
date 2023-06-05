package org.demo.handler;


import org.demo.exceptions.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@Component
public class UserAlreadyExistExceptionHandler implements ExceptionMapper<UserAlreadyExistsException> {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserAlreadyExistExceptionHandler.class);

    @Override
    public Response toResponse(UserAlreadyExistsException exception) {
        LOGGER.error(exception.getMessage());
        return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
    }
}