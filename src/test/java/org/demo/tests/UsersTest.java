package org.demo.tests;

import org.demo.configuration.PasswordEncoder;
import org.demo.defaultpackage.shelterservice.model.UserDto;
import org.demo.entity.DogEntity;
import org.demo.entity.PetEntity;
import org.demo.entity.UserEntity;
import org.demo.exceptions.UserAlreadyExistsException;
import org.demo.exceptions.UserNotExistException;
import org.demo.repository.GroupRepository;
import org.demo.repository.PetRepository;
import org.demo.repository.UserRepository;
import org.demo.service.people.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.demo.mappers.UserFieldsMapper.MAPPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {UserServiceImpl.class, PasswordEncoder.class})
public class UsersTest {

    @Captor
    ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PetRepository petRepository;

    @MockBean
    private GroupRepository groupRepository;
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDto person;

    @BeforeEach
    public void setUp() {
        long id = LocalDateTime.now().getNano();

        person = new UserDto();
        person.setFirstName("Firstname" + id);
        person.setLastName("Lastname" + id);
        person.setEmail("email" + id + "@mail.ru");
        person.setPhoneNumber("89213333333");
        person.setPassword("1234");

        DogEntity dog = new DogEntity();
        dog.setId(id);
        dog.setName("Name" + LocalDateTime.now().getNano());
        dog.setAge(10);
        dog.setColor("black");
        dog.setWeight(3);
        dog.setKind("terier");
        dog.setBooked(false);
        dog.setCastrated(true);
        dog.setGender(PetEntity.GenderEnum.MALE);
        dog.setSpecialTreatment(false);
        dog.setStory("Was found on street");
        dog.setVaccinated(true);
        dog.setVersion(1);
        dog.setCreatedTimestamp(OffsetDateTime.now());
        dog.setLastModifiedTimestamp(OffsetDateTime.now());
        dog.setTailDocked(false);
        dog.setType(PetEntity.PetEnum.DOG);
    }

    @Test
    public void testUsersCreation() throws UserAlreadyExistsException {
        UserEntity userEntity = MAPPER.userDtoToEntity(person);
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        userService.addUser(person);
        verify(userRepository, times(1)).save(userEntityArgumentCaptor.capture());
        UserEntity userEntityCaptured = userEntityArgumentCaptor.getValue();
        Assertions.assertTrue(passwordEncoder.encoder().matches(userEntity.getPassword(), userEntityCaptured.getPassword()));
        userEntity.setPassword(userEntityCaptured.getPassword());
        Assertions.assertEquals(userEntityCaptured, userEntity);
    }

    @Test
    public void testUsersCreationAlreadyExists() {
        UserEntity userEntity = MAPPER.userDtoToEntity(person);
        when(userRepository.existsByEmail(person.getEmail())).thenReturn(true);
        assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(person));
        verify(userRepository, times(0)).save(userEntity);
    }

    @Test
    public void testUsersUpdating() throws UserNotExistException {
        UserEntity userEntity = MAPPER.userDtoToEntity(person);
        userEntity.setVersion(1);
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        UserEntity result = userService.updateUser(userEntity.getId(), person);
        verify(userRepository, times(1)).save(userEntity);
        Assertions.assertEquals(userEntity, result);
    }

    @Test
    public void testUsersUpdatingNoPerson() {
        UserEntity userEntity = MAPPER.userDtoToEntity(person);
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.empty());
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        assertThrows(UserNotExistException.class, () -> userService.updateUser(userEntity.getId(), person));
        verify(userRepository, times(0)).save(userEntity);
    }

    @Test
    public void testUsersDeletion() throws UserNotExistException {
        UserEntity userEntity = MAPPER.userDtoToEntity(person);
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        userService.deleteUser(userEntity.getId());
        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    public void testUsersDeletionNoPerson() {
        UserEntity userEntity = MAPPER.userDtoToEntity(person);
        when(userRepository.findById(person.getId())).thenReturn(Optional.empty());
        assertThrows(UserNotExistException.class, () -> userService.updateUser(userEntity.getId(), person));
        verify(userRepository, times(0)).delete(userEntity);

    }

    @Test
    public void testGetUserByEmail() throws UserNotExistException {
        UserEntity userEntity = MAPPER.userDtoToEntity(person);
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(userEntity);
        UserEntity result = userService.getUserByEmail(userEntity.getEmail());
        verify(userRepository, times(1)).findByEmail(userEntity.getEmail());
        assertEquals(userEntity, result);
    }

    @Test
    public void testGetUserByEmailAndPassword() {
        UserEntity userEntity = MAPPER.userDtoToEntity(person);
        when(userRepository.findByEmailAndPassword(userEntity.getEmail(), userEntity.getPassword())).thenReturn(
                userEntity);
        UserEntity result = userService.getUserByEMailAndPassword(userEntity.getEmail(), userEntity.getPassword());
        verify(userRepository, times(1)).findByEmailAndPassword(userEntity.getEmail(), userEntity.getPassword());
        assertEquals(userEntity, result);
    }

}
