package org.demo.api;

import org.demo.defaultpackage.shelterservice.api.UsersApi;
import org.demo.defaultpackage.shelterservice.model.UserDto;
import org.demo.entity.UserEntity;
import org.demo.exceptions.UserNotExistException;
import org.demo.service.people.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.demo.mappers.UserFieldsMapper.MAPPER;

@Controller
public class UserApiImpl implements UsersApi {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserServiceImpl userService;

    public UserApiImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public Response updateUser(Long userId, @Valid UserDto userDto) {
        logger.warn("Try to update user with id: " + userId + ", new data: " + userDto);
        UserEntity user;
        try {
            user = userService.updateUser(userId, userDto);
        } catch (UserNotExistException e) {
            logger.warn("User with id " + userId + " such id does not exist");
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(user).build();
    }

    @Override
    public Response deleteUser(Long userId) {
        logger.warn("Try to delete user with id: " + userId);
        try {
            userService.deleteUser(userId);
        } catch (UserNotExistException e) {
            logger.warn("User does not exist");
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    @Override
    public Response getAllUsers() {
        List<UserDto> users = new ArrayList<>();
        userService.getAllUsers().forEach(userEntity -> users.add(MAPPER.userEntityToDto(userEntity)));
        logger.info("Return all users, amount = " + users.size());
        if (users.isEmpty()) {
            logger.warn("No users found in db");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(users).build();
    }

    @Override
    public Response getUserByEmail(String email) {
        UserEntity user;
        try {
            user = userService.getUserByEmail(email);
        } catch (UserNotExistException e) {
            logger.warn("No users with email " + email + " found in db");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        logger.info("Return user with email =" + email + " in response");
        return Response.ok().entity(MAPPER.userEntityToDto(user)).build();
    }

    @Autowired
    public void setService(UserServiceImpl userService) {
        this.userService = userService;
    }
}