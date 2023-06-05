package org.demo.apitests.tests.pets;

import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.demo.apitests.configuration.TestHelper;
import org.demo.defaultpackage.shelterservice.model.PetDto;
import org.demo.entity.PetEntity;
import org.demo.repository.PetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.logging.Logger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("integration-test")
@Transactional
public class NegativeTests extends TestHelper {

    private static final Logger LOGGER = Logger.getLogger(NegativeTests.class.getName());
    @Autowired
    private PetRepository repository;
    @Autowired
    private EntityManager entityManager;

    @BeforeTransaction
    public void cleanDb() {
        LOGGER.warning("Cleaning db..");
        repository.deleteAll();
    }

    @AfterEach
    public void flush() {
        entityManager.flush();
    }

    @Test
    public void addPetAlreadyExists() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .post(pathPets)
                .then()
                .statusCode(HttpStatus.SC_OK);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .post(pathPets)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void addPetWrongData() {
        PetDto postPet = getPet(RandomStringUtils.randomNumeric(31), PetDto.TypeEnum.DOG);
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .post(pathPets)
                .then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void getPetsNotFound() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .get(pathPets)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void findPetByNameNotFound() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .post(pathPets)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .as(PetEntity.class);

        Long petId = repository.findByName(postPet.getName()).get(0).getId();

        LOGGER.info("Send GET to created pet with id = " + petId);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .get(pathPetByName, postPet.getName() + "tmp")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void findPetByTypeNotFound() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .post(pathPets)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .as(PetEntity.class);

        Long petId = repository.findByName(postPet.getName()).get(0).getId();

        LOGGER.info("Send GET to created pet with id = " + petId);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .get(pathPetsByType, PetDto.TypeEnum.DOG)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void updatePetNotFound() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .put(pathPetById, 1L)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void updatePetWrongData() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .post(pathPets)
                .then()
                .statusCode(HttpStatus.SC_OK);

        Long petId = repository.findByName(postPet.getName()).get(0).getId();

        LOGGER.info("Send PUT to created pet with id = " + petId);

        postPet.setColor(RandomStringUtils.randomAlphabetic(21));

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .put(pathPetById, petId)
                .then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void deletePetNotFound() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .delete(pathPetById, 1L)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}