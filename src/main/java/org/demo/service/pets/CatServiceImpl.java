package org.demo.service.pets;

import org.demo.entity.CatEntity;
import org.demo.entity.PetEntity;
import org.demo.exceptions.PetNotExistException;
import org.demo.repository.CatRepository;
import org.demo.service.pets.api.ICatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CatServiceImpl extends PetEntity implements ICatService {
    private final static Logger logger = LoggerFactory.getLogger(CatServiceImpl.class);

    @Autowired
    private CatRepository repository;

    @Override
    public List<CatEntity> getByTrayNeed(Boolean goesToTray) throws PetNotExistException {
        List<CatEntity> cats = repository.findByGoesToTray(goesToTray);
        if (cats.isEmpty()) {
            logger.error("No pets tray learned found");
            throw new PetNotExistException();
        }
        return cats;
    }
}
