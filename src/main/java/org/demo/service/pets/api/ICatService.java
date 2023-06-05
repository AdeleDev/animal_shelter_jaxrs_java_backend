package org.demo.service.pets.api;

import org.demo.entity.CatEntity;
import org.demo.exceptions.PetNotExistException;

import java.util.List;

public interface ICatService {
    List<CatEntity> getByTrayNeed(Boolean goesToTray) throws PetNotExistException;
}
