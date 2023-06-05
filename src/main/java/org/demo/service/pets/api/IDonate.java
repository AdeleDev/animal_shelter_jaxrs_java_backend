package org.demo.service.pets.api;

import org.demo.defaultpackage.shelterservice.model.DonateDto;
import org.demo.entity.DonateEntity;
import org.demo.exceptions.PetNotExistException;

import java.util.List;

public interface IDonate {

    DonateEntity addDonate(DonateDto donateDto) throws PetNotExistException;

    List<DonateEntity> getAllDonations(Long petId) throws PetNotExistException;
}
