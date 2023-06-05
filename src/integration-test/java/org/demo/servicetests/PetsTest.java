package org.demo.servicetests;

import org.demo.ShelterApplication;
import org.demo.defaultpackage.shelterservice.model.PetDto;
import org.demo.entity.CatEntity;
import org.demo.entity.DogEntity;
import org.demo.entity.PetEntity;
import org.demo.exceptions.PetAlreadyExistsException;
import org.demo.exceptions.PetNotExistException;
import org.demo.repository.CatRepository;
import org.demo.repository.DogRepository;
import org.demo.repository.PetRepository;
import org.demo.service.pets.PetServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.stream.StreamSupport;

@SpringBootTest(classes = {ShelterApplication.class})
@ActiveProfiles("integration-test")
public class PetsTest {

    @Autowired
    private PetServiceImpl petsService;

    @Autowired
    private PetRepository petsRepository;

    @Autowired
    private DogRepository dogsRepository;

    @Autowired
    private CatRepository catsRepository;

    @Test
    public void testPetsCreation() throws PetAlreadyExistsException {
        PetDto pet = generatePet();
        petsService.addPet(pet);
        Assertions.assertTrue(StreamSupport.stream(petsRepository.findAll().spliterator(), false).findAny().isPresent(), "Pet was not created");
    }

    @Test
    public void testPetsCreationAlreadyExists() {
        Assertions.assertThrows(PetAlreadyExistsException.class, () -> {
            PetDto pet = generatePet();
            petsService.addPet(pet);
            petsService.addPet(pet);
        });
    }

    @Test
    public void testPetsUpdating() throws PetNotExistException, PetAlreadyExistsException {
        PetDto pet = generatePet();
        PetEntity entity = petsService.addPet(pet);
        int newAge = 4;
        pet.setAge(newAge);
        petsService.updatePet(entity.getId(), pet);
        Assertions.assertEquals(newAge, petsRepository.findByNameAndKind(pet.getName(), pet.getKind()).getAge(), "Pet was not updated");
    }

    @Test()
    public void testPetsUpdatingNoPet() {
        Assertions.assertThrows(PetNotExistException.class, () -> {
            PetDto pet = generatePet();
            pet.setId((long) LocalDateTime.now().getNano());
            petsService.updatePet(1L, pet);
        });
    }

    @Test
    public void testPetsDeletion() throws PetAlreadyExistsException, PetNotExistException {
        PetEntity entity = petsService.addPet(generatePet());
        petsService.deletePet(entity.getId());
        Assertions.assertEquals(Collections.emptyList(), petsRepository.findByName(entity.getName()), "Pet does still exist");
    }

    @Test()
    public void testPetsDeletionNoPet() {
        Assertions.assertThrows(PetNotExistException.class, () -> {
            PetDto pet = generatePet();
            pet.setId((long) LocalDateTime.now().getNano());
            petsService.deletePet(pet.getId());
        });
    }

    @Test
    public void testGetPetByName() throws PetAlreadyExistsException, PetNotExistException {
        PetDto pet = generatePet();
        petsService.addPet(pet);
        Assertions.assertNotNull(petsService.getPetByName(pet.getName()), "Pet was not found");
    }


    @Test
    public void testGetPetByType() throws PetAlreadyExistsException, PetNotExistException {
        PetDto pet = generatePet();
        petsService.addPet(pet);
        Assertions.assertFalse(petsService.getPetByType(pet.getType().value()).isEmpty(), "Pet was not found");
    }

    //    @Test
//    public void testGetBooked() throws PetAlreadyExistsException {
//        PetEntity pet = getPet(LocalDateTime.now().getNano());
//        petsService.addPet(pet);
//        Assertions.assertFalse("Pet was not found", petsService.getBooked(pet.getBooked()).isEmpty());
//    }
//
    @Test
    public void testCatGetByTrayNeed() {
        CatEntity cat = new CatEntity();
        cat.setName("Name" + LocalDateTime.now().getNano());
        cat.setAge(10);
        cat.setColor("black");
        cat.setWeight(3);
        cat.setKind("terier");
        cat.setBooked(false);
        cat.setCastrated(true);
        cat.setGender(PetEntity.GenderEnum.MALE);
        cat.setSpecialTreatment(false);
        cat.setStory("Was found under bridge");
        cat.setVaccinated(true);
        cat.setDonate(0L);
        cat.setVersion(1);
        cat.setCreatedTimestamp(OffsetDateTime.now());
        cat.setLastModifiedTimestamp(OffsetDateTime.now());
        cat.setGoesToTray(true);
        cat.setType(PetEntity.PetEnum.CAT);

        petsRepository.save(cat);
        Assertions.assertFalse(catsRepository.findByGoesToTray(cat.getGoesToTray()).isEmpty(), "Pets were not found");
    }

    @Test
    public void testGetByTailDocked() {
        DogEntity dog = new DogEntity();
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
        dog.setDonate(0L);
        dog.setCreatedTimestamp(OffsetDateTime.now());
        dog.setLastModifiedTimestamp(OffsetDateTime.now());
        dog.setTailDocked(false);
        dog.setType(PetEntity.PetEnum.DOG);

        petsRepository.save(dog);
        Assertions.assertFalse(dogsRepository.findByTailDocked(dog.getTailDocked()).isEmpty(), "Pets were not found");
    }

    private PetDto generatePet() {
        long id = LocalDateTime.now().getNano();
        PetDto pet = new PetDto();
        pet.setName("Name" + id);
        pet.setAge(10);
        pet.setColor("black");
        pet.setWeight(3);
        pet.setType(PetDto.TypeEnum.CAT);
        pet.setKind("scottish");
        pet.setCastrated(true);
        pet.setGender(PetDto.GenderEnum.MALE);
        pet.setSpecialTreatment(false);
        pet.setStory("was found on street");
        pet.setVaccinated(true);
        return pet;
    }
}
