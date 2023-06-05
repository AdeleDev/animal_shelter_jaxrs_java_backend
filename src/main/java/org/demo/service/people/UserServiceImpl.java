package org.demo.service.people;

import org.demo.configuration.PasswordEncoder;
import org.demo.defaultpackage.shelterservice.model.UserDto;
import org.demo.entity.GroupEntity;
import org.demo.entity.UserEntity;
import org.demo.exceptions.UserAlreadyExistsException;
import org.demo.exceptions.UserNotExistException;
import org.demo.repository.GroupRepository;
import org.demo.repository.UserRepository;
import org.demo.service.people.api.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.demo.mappers.UserFieldsMapper.MAPPER;

@Component
public class UserServiceImpl implements IUserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final GroupRepository groupsRepository;

    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, GroupRepository groupsRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.groupsRepository = groupsRepository;
        this.passwordEncoder = passwordEncoder;

    }


    @Override
    @Transactional
    public UserEntity addUser(UserDto person) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(person.getEmail())) {
            throw new UserAlreadyExistsException(person.getEmail());
        } else {
            UserEntity userEntity = MAPPER.userDtoToEntity(person);
            int version = 1;
            userEntity.setPassword(passwordEncoder.encoder().encode(userEntity.getPassword()));
            userEntity.setVersion(version);
            userEntity.setCreatedTimestamp(OffsetDateTime.now());
            userEntity.setLastModifiedTimestamp(OffsetDateTime.now());
            userEntity.setGroups(groupsRepository.findByType(GroupEntity.GroupEnum.GUEST));

            return userRepository.save(userEntity);
        }
    }

    @Override
    @Transactional
    public UserEntity updateUser(Long userId, UserDto person) throws UserNotExistException {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new UserNotExistException();
        } else {
            UserEntity userEntityNew = MAPPER.userDtoToEntity(person);
            userEntityNew.setId(userId);
            userEntityNew.setGroups(userEntity.get().getGroups());
            userEntityNew.setVersion(userEntity.get().getVersion() + 1);
            userEntityNew.setLastModifiedTimestamp(OffsetDateTime.now());

            return userRepository.save(userEntityNew);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) throws UserNotExistException {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new UserNotExistException();
        } else {
            userRepository.delete(userEntity.get());
        }
    }

    @Override
    @Transactional
    public UserEntity getUserByEmail(String email) throws UserNotExistException {
        UserEntity userFound = userRepository.findByEmail(email);
        if (userFound == null) {
            logger.error("No people were found by email = " + email);
            throw new UserNotExistException();
        }
        return userFound;
    }

    @Override
    @Transactional
    public UserEntity getUserByEMailAndPassword(String email, String password) {
        UserEntity userFound = userRepository.findByEmailAndPassword(email, password);
        if (userFound == null) {
            logger.warn("No people were found by email = " + email + " or password in wrong");
        }
        return userFound;
    }

    @Override
    @Transactional
    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        if (users.isEmpty()) {
            logger.error("No users found in db");
        }
        return users;
    }

}
