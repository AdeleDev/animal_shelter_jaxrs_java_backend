package org.demo.service.pets;

import org.demo.defaultpackage.shelterservice.model.DonateDto;
import org.demo.entity.DonateEntity;
import org.demo.entity.PetEntity;
import org.demo.exceptions.PetAlreadyExistsException;
import org.demo.repository.DonateRepository;
import org.demo.service.pets.api.IDonate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

import static org.demo.mappers.UserFieldsMapper.MAPPER;
@Component
public class DonateServiceImpl implements IDonate {

    private final static Logger logger = LoggerFactory.getLogger(DonateServiceImpl.class);

    private final DonateRepository donateRepository;

    public DonateServiceImpl(DonateRepository donateRepository) {
        this.donateRepository = donateRepository;
    }


    @Override
    public DonateEntity addDonate(DonateDto donateDto) {
            return donateRepository.save(MAPPER.donateDtoToEntity(donateDto));
    }

    @Override
    public List<DonateEntity> getAllDonations(Long petId) {
        return donateRepository.findByPetId(petId);
    }
}
