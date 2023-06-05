package org.demo.servicetests;

import org.demo.ShelterApplication;
import org.demo.defaultpackage.shelterservice.model.UserDto;
import org.demo.entity.UserEntity;
import org.demo.exceptions.UserAlreadyExistsException;
import org.demo.exceptions.UserNotExistException;
import org.demo.repository.DonateRepository;
import org.demo.repository.GroupRepository;
import org.demo.repository.UserRepository;
import org.demo.service.people.UserServiceImpl;
import org.demo.service.pets.DonateServiceImpl;
import org.demo.service.pets.PetServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@SpringBootTest(classes = {ShelterApplication.class})
@ActiveProfiles("integration-test")
public class UsersTest {

    @Autowired
    private UserServiceImpl usersService;

    @Autowired
    private PetServiceImpl petsService;

    @Autowired
    private DonateServiceImpl donateService;

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private GroupRepository groupsRepository;

    @Autowired
    private DonateRepository donateRepository;

    @Test
    public void testUsersCreation() throws UserAlreadyExistsException {
        UserDto person = generatePerson();
        usersService.addUser(person);
        Assertions.assertNotNull(usersRepository.findByEmail(person.getEmail()), "User was not created");
    }

    @Test()
    public void testUsersCreationAlreadyExists() {
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            UserDto person = generatePerson();
            usersService.addUser(person);
            usersService.addUser(person);
        });

    }

    @Test
    public void testUsersUpdating() throws UserNotExistException, UserAlreadyExistsException {
        UserDto person = generatePerson();
        UserEntity entity = usersService.addUser(person);
        String newLastname = "newLast.ru";
        person.setLastName(newLastname);
        usersService.updateUser(entity.getId(), person);
        Assertions.assertEquals(newLastname, usersRepository.findByEmail(person.getEmail()).getLastName(), "User was not updated");

    }

    @Test()
    public void testUsersUpdatingNoPerson() {
        Assertions.assertThrows(UserNotExistException.class, () -> {
            UserDto person = new UserDto();
            person.setId((long) LocalDateTime.now().getNano());
            person.setEmail("newDelete@mail.ru");
            person.setFirstName("newName");
            person.setLastName("newName");
            usersService.updateUser(1L, person);
        });
    }

    @Test
    public void testUsersDeletion() throws UserAlreadyExistsException, UserNotExistException {
        UserEntity person = usersService.addUser(generatePerson());
        usersService.deleteUser(person.getId());
        Assertions.assertNull(usersRepository.findByEmail(person.getEmail()), "User does still exist");
    }

    @Test()
    public void testUsersDeletionNoPerson() {
        Assertions.assertThrows(UserNotExistException.class, () -> {
            UserDto person = generatePerson();
            person.setId((long) LocalDateTime.now().getNano());
            usersService.deleteUser(person.getId());
        });
    }

    @Test
    public void testGetUserByEmail() throws UserAlreadyExistsException, UserNotExistException {
        UserDto person = generatePerson();
        usersService.addUser(person);
        Assertions.assertNotNull(usersService.getUserByEmail(person.getEmail()), "User was not found");
    }

//    @Test
//    public void testDonate() throws UserAlreadyExistsException, PetAlreadyExistsException {
//        UserDto person = generatePerson();
//
//        DogEntity dog = new DogEntity();
//        dog.setName("Name" + LocalDateTime.now().getNano());
//        dog.setAge(10);
//        dog.setColor("black");
//        dog.setWeight(3);
//        dog.setKind("terier");
//        dog.setBooked(false);
//        dog.setCastrated(true);
//        dog.setGender(PetEntity.GenderEnum.MALE);
//        dog.setSpecialTreatment(false);
//        dog.setStory("Was found on street");
//        dog.setVaccinated(true);
//        dog.setVersion(1);
//        dog.setCreatedTimestamp(OffsetDateTime.now());
//        dog.setLastModifiedTimestamp(OffsetDateTime.now());
//        dog.setTailDocked(false);
//        dog.setType(PetEntity.PetEnum.DOG);
//
//        UserEntity userEntity = usersService.addUser(person);
//        PetEntity petEntity = petsService.addPet(dog);
//        DonateEntity donateEntity = new DonateEntity();
//        donateEntity.setUserId(userEntity.getId());
//        donateEntity.setPetId(petEntity.getId());
//        donateEntity.setSum(1000);
//
////        donateService.addDonate(MAPP);
//        Assertions.assertFalse(
//                donateRepository.findByPetId(dog.getId()).isEmpty(),
//                "Donate was not found by pet's name");
//        Assertions.assertFalse(
//                donateRepository.findByUserId(person.getId()).isEmpty(),
//                "Donate was not found by user's lastName");
//
//    }

    UserDto generatePerson() {
        UserDto person = new UserDto();
        long id = LocalDateTime.now().getNano();
        person.setFirstName("Firstname" + id);
        person.setLastName("Lastname" + id);
        person.setEmail("email" + id + "@mail.ru");
        person.setPhoneNumber("89213333333");
        person.setPassword("1234");
        return person;
    }
}
