package org.demo.tests;

import org.demo.defaultpackage.shelterservice.model.DonateDto;
import org.demo.entity.DonateEntity;
import org.demo.exceptions.PetNotExistException;
import org.demo.repository.DonateRepository;
import org.demo.repository.PetRepository;
import org.demo.repository.UserRepository;
import org.demo.service.pets.DonateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.demo.mappers.UserFieldsMapper.MAPPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {DonateServiceImpl.class})
public class DonationsTest {

    @MockBean
    private DonateRepository donateRepository;
    @MockBean
    private PetRepository petRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private DonateServiceImpl donateService;

    private DonateDto donateDto;

    @BeforeEach
    public void setUp() {
        donateDto = new DonateDto();
        donateDto.setUserId(1L);
        donateDto.setPetId(1L);
        donateDto.setSum(100L);
    }

    @Test
    public void testAddDonate() {
        DonateEntity donateEntity = MAPPER.donateDtoToEntity(donateDto);
        donateEntity.setVersion(1);

        when(donateRepository.save(donateEntity)).thenReturn(donateEntity);
        DonateEntity result = donateService.addDonate(donateDto);
        verify(donateRepository, times(1)).save(donateEntity);
        Assertions.assertEquals(donateEntity, result);
    }

    @Test
    public void testGetDonate() {
        DonateEntity donateEntity = new DonateEntity();
        donateEntity.setUserId(1L);
        donateEntity.setPetId(1L);
        donateEntity.setSum(100L);

        DonateEntity donateEntity2 = new DonateEntity();
        donateEntity.setUserId(2L);
        donateEntity.setPetId(1L);
        donateEntity.setSum(100L);

        List<DonateEntity> donateEntities = new ArrayList<>();
        donateEntities.add(donateEntity);
        donateEntities.add(donateEntity2);
        when(donateRepository.findByPetId(donateEntity.getPetId())).thenReturn(donateEntities);

        List<DonateEntity> result = donateService.getAllDonations(donateEntity.getPetId());
        verify(donateRepository, times(1)).findByPetId(donateEntity.getPetId());
        assertEquals(donateEntities, result);
    }


}
