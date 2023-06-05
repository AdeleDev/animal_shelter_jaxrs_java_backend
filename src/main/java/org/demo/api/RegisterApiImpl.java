package org.demo.api;

import org.demo.entity.UserEntity;
import org.demo.exceptions.UserAlreadyExistsException;
import org.demo.service.people.UserServiceImpl;
import org.demo.defaultpackage.shelterservice.api.RegisterApi;
import org.demo.defaultpackage.shelterservice.model.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.ws.rs.core.Response;

@Controller
public class RegisterApiImpl implements RegisterApi {

    private final static Logger logger = LoggerFactory.getLogger(RegisterApiImpl.class);

    private final UserServiceImpl users;

    public RegisterApiImpl(UserServiceImpl users) {
        this.users = users;
    }

    @Override
    @Transactional
    public Response createUser(@Valid UserDto userDto) {
        logger.info("Try to register user: " + userDto);
        UserEntity user;
        try {
            user = users.addUser(userDto);
        } catch (UserAlreadyExistsException e) {
            logger.warn("Request to create already existing user " + userDto.getEmail() + "was send");
            e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.ok().entity(user).build();
    }
}
