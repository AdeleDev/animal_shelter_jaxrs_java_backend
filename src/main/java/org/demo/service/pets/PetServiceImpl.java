package org.demo.service.pets;

import org.demo.defaultpackage.shelterservice.model.PetDto;
import org.demo.entity.PetEntity;
import org.demo.exceptions.PetAlreadyExistsException;
import org.demo.exceptions.PetNotExistException;
import org.demo.repository.PetRepository;
import org.demo.service.pets.api.IPetService;
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
public class PetServiceImpl implements IPetService {

    private final static Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);

    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    @Transactional
    public PetEntity addPet(PetDto pet) throws PetAlreadyExistsException {
        if (petRepository.existsByNameAndKind(pet.getName(), pet.getKind())) {
            throw new PetAlreadyExistsException(pet.getName(), pet.getKind());
        } else {
            PetEntity petEntity = MAPPER.petDtoToEntity(pet);
            int version = 1;
            petEntity.setVersion(version);
            petEntity.setCreatedTimestamp(OffsetDateTime.now());
            petEntity.setLastModifiedTimestamp(OffsetDateTime.now());
            petEntity.setBooked(false);
            petEntity.setDonate(0L);
            return petRepository.save(petEntity);
        }
    }

    @Override
    @Transactional
    public PetEntity updatePet(Long petId, PetDto pet) throws PetNotExistException {
        Optional<PetEntity> petEntity = petRepository.findById(petId);
        if (petEntity.isEmpty()) {
            throw new PetNotExistException();
        } else {
            PetEntity petEntityNew = MAPPER.petDtoToEntity(pet);
            petEntityNew.setId(petId);
            petEntityNew.setBooked(petEntity.get().getBooked());
            petEntityNew.setDonate(petEntity.get().getDonate());
            petEntityNew.setVersion(petEntity.get().getVersion() + 1);
            petEntityNew.setLastModifiedTimestamp(OffsetDateTime.now());
            return petRepository.save(petEntityNew);
        }

    }

    @Override
    @Transactional
    public void deletePet(Long petId) throws PetNotExistException {
        Optional<PetEntity> petEntity = petRepository.findById(petId);
        if (petEntity.isEmpty()) {
            throw new PetNotExistException();
        } else {
            petRepository.delete(petEntity.get());
        }
    }

    @Override
    public List<PetEntity> getPetByName(String name) throws PetNotExistException {
        List<PetEntity> petFound = petRepository.findByName(name);
        if (petFound.isEmpty()) {
            logger.error("No pets were found by name = " + name);
            throw new PetNotExistException();
        }
        return petFound;
    }

    @Override
    public List<PetEntity> getPetByType(String type) throws PetNotExistException {
        List<PetEntity> pets = petRepository.findByType(PetEntity.PetEnum.valueOf(type));
        if (pets.isEmpty()) {
            logger.warn("No pets were found by type = " + type);
            throw new PetNotExistException();
        }
        return pets;
    }

    @Override
    public List<PetEntity> getAllPets() throws PetNotExistException {
        List<PetEntity> pets = new ArrayList<>();
        petRepository.findAll().forEach(pets::add);
        if (pets.isEmpty()) {
            logger.warn("No pets were found in db");
            throw new PetNotExistException();
        }
        return pets;
    }
}
