package org.demo.tests;

import org.demo.configuration.PasswordEncoder;
import org.demo.defaultpackage.shelterservice.model.PetDto;
import org.demo.entity.PetEntity;
import org.demo.exceptions.PetAlreadyExistsException;
import org.demo.exceptions.PetNotExistException;
import org.demo.repository.PetRepository;
import org.demo.service.pets.PetServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.demo.mappers.UserFieldsMapper.MAPPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {PetServiceImpl.class, PasswordEncoder.class})
public class PetsTest {

    @MockBean
    private PetRepository petRepository;

    @Autowired
    private PetServiceImpl petService;

    private PetDto dog;

    @BeforeEach
    public void setUp() {
        long id = LocalDateTime.now().getNano();

        dog = new PetDto();
        dog.setId(id);
        dog.setName("Name" + LocalDateTime.now().getNano());
        dog.setAge(10);
        dog.setColor("black");
        dog.setWeight(3);
        dog.setKind("terier");
        dog.setCastrated(true);
        dog.setSpecialTreatment(false);
        dog.setStory("Was found on street");
        dog.setVaccinated(true);
        dog.setType(PetDto.TypeEnum.CAT);
    }

    @Test
    public void testPetsCreation() throws PetAlreadyExistsException {
        PetEntity petEntity = MAPPER.petDtoToEntity(dog);
        petEntity.setVersion(1);
        petEntity.setBooked(false);
        when(petRepository.findById(petEntity.getId())).thenReturn(Optional.of(petEntity));
        when(petRepository.save(petEntity)).thenReturn(petEntity);
        PetEntity result = petService.addPet(dog);
        verify(petRepository, times(1)).save(petEntity);
        Assertions.assertEquals(petEntity, result);
    }

    @Test
    public void testPetsCreationAlreadyExists() {
        PetEntity petEntity = MAPPER.petDtoToEntity(dog);
        when(petRepository.existsByNameAndKind(petEntity.getName(), petEntity.getKind())).thenReturn(true);
        assertThrows(PetAlreadyExistsException.class, () -> petService.addPet(dog));
        verify(petRepository, times(0)).save(petEntity);
    }

    @Test
    public void testPetsUpdating() throws PetNotExistException {
        PetEntity petEntity = MAPPER.petDtoToEntity(dog);
        petEntity.setVersion(1);
        petEntity.setBooked(false);
        when(petRepository.findById(petEntity.getId())).thenReturn(Optional.of(petEntity));
        when(petRepository.save(petEntity)).thenReturn(petEntity);
        PetEntity result = petService.updatePet(petEntity.getId(), dog);
        verify(petRepository, times(1)).save(petEntity);
        Assertions.assertEquals(petEntity, result);
    }

    @Test
    public void testPetsUpdatingNoPerson() {
        PetEntity petEntity = MAPPER.petDtoToEntity(dog);
        when(petRepository.findById(petEntity.getId())).thenReturn(Optional.empty());
        when(petRepository.save(petEntity)).thenReturn(petEntity);
        assertThrows(PetNotExistException.class, () -> petService.updatePet(petEntity.getId(), dog));
        verify(petRepository, times(0)).save(petEntity);
    }

    @Test
    public void testPetsDeletion() throws PetNotExistException {
        PetEntity petEntity = MAPPER.petDtoToEntity(dog);
        when(petRepository.findById(petEntity.getId())).thenReturn(Optional.of(petEntity));
        petService.deletePet(petEntity.getId());
        verify(petRepository, times(1)).delete(petEntity);
    }

    @Test
    public void testPetsDeletionNoPerson() {
        PetEntity petEntity = MAPPER.petDtoToEntity(dog);
        when(petRepository.findById(dog.getId())).thenReturn(Optional.empty());
        assertThrows(PetNotExistException.class, () -> petService.updatePet(petEntity.getId(), dog));
        verify(petRepository, times(0)).delete(petEntity);

    }

    @Test
    public void testGetPetByName() throws PetNotExistException {
        PetEntity petEntity = MAPPER.petDtoToEntity(dog);
        when(petRepository.findByName(petEntity.getName())).thenReturn(Collections.singletonList(petEntity));
        List<PetEntity> result = petService.getPetByName(petEntity.getName());
        verify(petRepository, times(1)).findByName(petEntity.getName());
        assertEquals(petEntity, result.get(0));
    }

    @Test
    public void testGetPetByNameNotFound() {
        PetEntity petEntity = MAPPER.petDtoToEntity(dog);
        when(petRepository.findByName(petEntity.getName())).thenReturn(Collections.emptyList());
        assertThrows(PetNotExistException.class, () -> petService.getPetByType(dog.getType().value()));
        verify(petRepository, times(1)).findByType(petEntity.getType());
    }

    @Test
    public void testGetPetTypeNotFound() {
        PetEntity petEntity = MAPPER.petDtoToEntity(dog);
        when(petRepository.findByType(petEntity.getType())).thenReturn(Collections.emptyList());
        assertThrows(PetNotExistException.class, () -> petService.getPetByType(dog.getType().value()));
        verify(petRepository, times(1)).findByType(petEntity.getType());
    }

}
