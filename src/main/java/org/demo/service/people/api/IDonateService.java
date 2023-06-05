package org.demo.service.people.api;

import org.demo.entity.DonateEntity;
import org.demo.defaultpackage.shelterservice.model.DonateDto;
import org.demo.exceptions.PetNotExistException;

public interface IDonateService {

    DonateEntity addDonate(DonateDto donateDto) throws PetNotExistException;

    long getAllDonate(long petId) throws PetNotExistException;

}
