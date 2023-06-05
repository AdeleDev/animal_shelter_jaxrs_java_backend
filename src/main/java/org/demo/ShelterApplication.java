package org.demo;

import org.demo.configuration.PasswordEncoder;
import org.demo.defaultpackage.shelterservice.model.PetDto;
import org.demo.defaultpackage.shelterservice.model.UserDto;
import org.demo.entity.GroupEntity;
import org.demo.entity.UserEntity;
import org.demo.exceptions.PetAlreadyExistsException;
import org.demo.exceptions.UserAlreadyExistsException;
import org.demo.repository.GroupRepository;
import org.demo.repository.PetRepository;
import org.demo.repository.UserRepository;
import org.demo.service.people.UserServiceImpl;
import org.demo.service.pets.PetServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ShelterApplication {
    private final UserServiceImpl usersService;
    private final PetServiceImpl petService;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final static Logger logger = LoggerFactory.getLogger(ShelterApplication.class);

    public ShelterApplication(UserServiceImpl usersService, PetServiceImpl petService, GroupRepository groupRepository,
                              UserRepository userRepository, PetRepository petRepository, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.petService = petService;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(ShelterApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        try {
            defaultAdminCreation();
            defaultUsersCreation();
        } catch (UserAlreadyExistsException e) {
            logger.warn("Default users already exists: " + e);
        }
        try {
            defaultPetsCreation();
        } catch (PetAlreadyExistsException e) {
            logger.warn("Default pets already exists: " + e);
        }
    }

    private void defaultAdminCreation() throws UserAlreadyExistsException {
        UserDto admin = new UserDto();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("admin@mail.ru");
        admin.setPhoneNumber("89213333333");
        admin.setPassword(passwordEncoder.encoder().encode("admin"));
        UserEntity adminEntity = usersService.addUser(admin);
        adminEntity.setGroups(groupRepository.findByType(GroupEntity.GroupEnum.ADMIN));
        userRepository.save(adminEntity);
    }

    private void defaultUsersCreation() throws UserAlreadyExistsException {
        for (int i = 0; i < 3; i++) {
            UserDto user = new UserDto();
            user.setFirstName("user" + i);
            user.setLastName("user" + i);
            user.setEmail(user.getLastName() + "@mail.ru");
            user.setPhoneNumber("8921333333" + i);
            user.setPassword(passwordEncoder.encoder().encode("user" + i));
            usersService.addUser(user);
        }
    }

    private void defaultPetsCreation() throws PetAlreadyExistsException {
        for (int i = 1; i < 6; i++) {
            PetDto pet = new PetDto();
            pet.setName("Pet" + i);
            pet.setType(PetDto.TypeEnum.CAT);
            pet.setGender(PetDto.GenderEnum.FEMALE);
            pet.setKind("Kind" + i);
            pet.setAge(2 + i);
            pet.setWeight(i * 2);
            pet.setColor("PetColor" + i);
            pet.setVaccinated(true);
            pet.setCastrated(true);
            pet.setSpecialTreatment(false);
            pet.setStory("Story of pet " + pet.getName());

            if (i > 3) {
                pet.setType(PetDto.TypeEnum.DOG);
            }
            petService.addPet(pet);
        }
    }
}