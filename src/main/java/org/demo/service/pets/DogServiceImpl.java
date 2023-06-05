package org.demo.service.pets;

import org.demo.entity.DogEntity;
import org.demo.entity.PetEntity;
import org.demo.exceptions.PetNotExistException;
import org.demo.repository.DogRepository;
import org.demo.service.pets.api.IDogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DogServiceImpl extends PetEntity implements IDogService {
    private final static Logger logger = LoggerFactory.getLogger(DogServiceImpl.class);

    @Autowired
    private DogRepository repository;

    @Override
    public List<DogEntity> getByTailDocked(Boolean tailDocked) throws PetNotExistException {
        List<DogEntity> dogs = repository.findByTailDocked(tailDocked);
        if (dogs.isEmpty()) {
            logger.error("No tail-docked dogs found");
            throw new PetNotExistException();
        }
        return dogs;
    }
}
