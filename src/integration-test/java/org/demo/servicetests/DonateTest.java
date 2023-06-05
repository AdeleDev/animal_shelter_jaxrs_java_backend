package org.demo.servicetests;

import org.demo.ShelterApplication;
import org.demo.defaultpackage.shelterservice.model.DonateDto;
import org.demo.entity.DonateEntity;
import org.demo.exceptions.PetNotExistException;
import org.demo.repository.DonateRepository;
import org.demo.service.pets.DonateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@SpringBootTest(classes = {ShelterApplication.class})
@ActiveProfiles("integration-test")
public class DonateTest {

    @Autowired
    private DonateServiceImpl donateService;

    @Autowired
    private DonateRepository donateRepository;


    @Test
    public void testDonateCreation() {
        DonateDto donateDto = generateDonate();
        donateService.addDonate(donateDto);
        Assertions.assertTrue(StreamSupport.stream(donateRepository.findAll().spliterator(), false)
                .findAny().isPresent(), "Donate was not created");
    }

    @Test
    public void testGetAllDonations() {
        DonateDto donateDto = generateDonate();
        donateService.addDonate(donateDto);
        donateService.addDonate(donateDto);
        List<DonateEntity> donates = donateService.getAllDonations(donateDto.getPetId());
        Assertions.assertEquals(
                donates,
                donateRepository.findByPetId(donateDto.getPetId()),
                "Donates were not found");
    }

    private DonateDto generateDonate() {
        DonateDto donateDto = new DonateDto();
        donateDto.setUserId((long) LocalDateTime.now().getNano());
        donateDto.setPetId((long) LocalDateTime.now().getNano());
        donateDto.setSum(1000L);
        return donateDto;
    }

}
