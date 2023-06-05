package org.demo.service.pets.api;

import org.demo.entity.DogEntity;
import org.demo.exceptions.PetNotExistException;

import java.util.List;

public interface IDogService {

    List<DogEntity> getByTailDocked(Boolean tailDocked) throws PetNotExistException;
}
