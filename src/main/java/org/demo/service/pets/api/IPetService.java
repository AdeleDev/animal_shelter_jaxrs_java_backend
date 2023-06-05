package org.demo.service.pets.api;

import org.demo.defaultpackage.shelterservice.model.PetDto;
import org.demo.entity.PetEntity;
import org.demo.exceptions.PetAlreadyExistsException;
import org.demo.exceptions.PetNotExistException;

import java.util.List;

public interface IPetService {
    PetEntity addPet(PetDto pet) throws PetAlreadyExistsException;

    PetEntity updatePet(Long id,PetDto pet) throws PetNotExistException;

    void deletePet(Long petId) throws PetNotExistException;

    List<PetEntity> getPetByName(String name) throws PetNotExistException;

    List<PetEntity> getPetByType(String type) throws PetNotExistException;

    List<PetEntity> getAllPets() throws PetNotExistException;
}
