package org.demo.api;

import org.demo.defaultpackage.shelterservice.api.LoginApi;
import org.demo.entity.UserEntity;
import org.demo.exceptions.UserNotExistException;
import org.demo.service.people.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

import static org.demo.mappers.UserFieldsMapper.MAPPER;

@Controller
public class LoginApiImpl implements LoginApi {

    private final static Logger logger = LoggerFactory.getLogger(LoginApiImpl.class);

    private final UserServiceImpl users;
    private final PasswordEncoder passwordEncoder;

    public LoginApiImpl(UserServiceImpl users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Response userLogin(@NotNull String email, @NotNull String password) {
        logger.info("User with email = " + email + " and password = " + password + " tries to enter portal"); //todo delete pass

        try {
            UserEntity user = users.getUserByEmail(email);
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                logger.info("Return user with email =" + email + " in response");
                return Response.ok().entity(MAPPER.userEntityToDto(user)).build();
            } else {
                logger.warn("Password is incorrect");
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (UserNotExistException e) {
            logger.warn("No users found in db");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}


