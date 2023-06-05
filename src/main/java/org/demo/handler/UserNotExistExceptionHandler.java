package org.demo.handler;


import org.demo.exceptions.UserAlreadyExistsException;
import org.demo.exceptions.UserNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@Component
public class UserNotExistExceptionHandler implements ExceptionMapper<UserNotExistException> {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserNotExistExceptionHandler.class);

    @Override
    public Response toResponse(UserNotExistException exception) {
        LOGGER.error(exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}