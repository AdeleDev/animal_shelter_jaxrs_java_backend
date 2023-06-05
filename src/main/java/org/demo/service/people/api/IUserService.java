package org.demo.service.people.api;

import org.demo.entity.UserEntity;
import org.demo.exceptions.UserAlreadyExistsException;
import org.demo.exceptions.UserNotExistException;
import org.demo.defaultpackage.shelterservice.model.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUserService {

    UserEntity addUser(UserDto person) throws UserAlreadyExistsException;

    UserEntity updateUser(Long userId, UserDto person) throws UserNotExistException;

    void deleteUser(Long personId) throws UserNotExistException;

    UserEntity getUserByEmail(String email) throws UserNotExistException;

    UserEntity getUserByEMailAndPassword(String email, String password);

    List<UserEntity> getAllUsers() throws UserNotExistException;
}
