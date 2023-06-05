package org.demo.api;

import org.demo.defaultpackage.shelterservice.api.PetsApi;
import org.demo.defaultpackage.shelterservice.model.DonateDto;
import org.demo.defaultpackage.shelterservice.model.PetDto;
import org.demo.entity.DonateEntity;
import org.demo.entity.PetEntity;
import org.demo.exceptions.PetAlreadyExistsException;
import org.demo.exceptions.PetNotExistException;
import org.demo.service.pets.DonateServiceImpl;
import org.demo.service.pets.PetServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.demo.mappers.UserFieldsMapper.MAPPER;

@Controller
public class PetApiImpl implements PetsApi {
    private final static Logger logger = LoggerFactory.getLogger(PetApiImpl.class);

    private final PetServiceImpl petService;

    private final DonateServiceImpl donateService;

    public PetApiImpl(PetServiceImpl pets, DonateServiceImpl donateService) {
        this.petService = pets;
        this.donateService = donateService;
    }

    @Override
    public Response addDonate(Long petId, DonateDto donateDto) {
        logger.warn("Try to add pet's donation: " + donateDto);
        DonateEntity donateEntity = donateService.addDonate(donateDto);
        return Response.ok().entity(donateEntity).build();
    }

    @Override
    public Response getAllDonate(Long petId) {
        logger.warn("Get all donations for id " + petId);
        return Response.ok().entity(donateService.getAllDonations(petId)).build();
    }

    @Override
    public Response createPet(PetDto petDto) {
        logger.warn("Try to register pet: " + petDto);
        PetEntity petEntity;
        try {
            petEntity = petService.addPet(petDto);
        } catch (PetAlreadyExistsException e) {
            logger.warn("Request to create already existing pet " + petDto.getName() + "was send");
            e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.ok().entity(petEntity).build();
    }

    @Override
    public Response updatePet(Long petId, PetDto petDto) {
        logger.warn("Try to update pet with id: " + petId + ", new data: " + petDto);

        PetEntity petEntity = MAPPER.petDtoToEntity(petDto);
        try {
            petEntity.setId(petId);
            petEntity = petService.updatePet(petId, petDto);
        } catch (PetNotExistException e) {
            logger.warn("Pet does not exist");
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(petEntity).build();
    }

    @Override
    public Response deletePet(Long petId) {
        logger.warn("Try to delete pet with id: " + petId);
        try {
            petService.deletePet(petId);
        } catch (PetNotExistException e) {
            logger.warn("Pet does not exist");
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    @Override
    public Response getAllPets() {
        try {
            List<PetDto> finalPetList = new ArrayList<>();
            petService.getAllPets().forEach(petsEntity -> finalPetList.add(MAPPER.petEntityToDto(petsEntity)));
            logger.info("Return all pets in response");
            return Response.ok().entity(finalPetList).build();
        } catch (PetNotExistException e) {
            logger.warn("Return all Pets in response");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    @Override
    public Response getPetByName(String name) {
        try {
            List<PetDto> finalPetList = new ArrayList<>();
            petService.getPetByName(name).forEach(petsEntity -> finalPetList.add(MAPPER.petEntityToDto(petsEntity)));
            logger.info("Return all pets with name =" + name + " in response");
            return Response.ok().entity(finalPetList).build();
        } catch (PetNotExistException e) {
            logger.warn("No pets with name " + name + " found in db");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    @Override
    public Response getPetsByType(String type) {
        try {
            List<PetDto> finalPetList = new ArrayList<>();
            petService.getPetByType(type)
                    .forEach(petsEntity -> finalPetList.add(MAPPER.petEntityToDto(petsEntity)));
            logger.info("Return all pets with type =" + type + " in response");
            return Response.ok().entity(finalPetList).build();
        } catch (PetNotExistException e) {
            logger.warn("No pets with type " + type + " found in db");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
