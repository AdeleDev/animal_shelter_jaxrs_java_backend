package org.demo.apitests.tests.pets;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.demo.apitests.configuration.TestHelper;
import org.demo.defaultpackage.shelterservice.model.PetDto;
import org.demo.entity.PetEntity;
import org.demo.repository.DonateRepository;
import org.demo.repository.PetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.logging.Logger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("integration-test")
@Transactional
public class PositiveTests extends TestHelper {

    private static final Logger LOGGER = Logger.getLogger(PositiveTests.class.getName());

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private DonateRepository donateRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeTransaction
    public void cleanDb() {
        LOGGER.warning("Cleaning db..");
        petRepository.deleteAll();
    }

    @AfterEach
    public void flush() {
        entityManager.flush();
    }


    @Test
    public void addPet() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .post(pathPets)
                .then()
                .statusCode(HttpStatus.SC_OK);

        Long petId = petRepository.findByName(postPet.getName()).get(0).getId();

        LOGGER.info("Send GET to created pet with id = " + petId);

        TypeRef<List<PetDto>> ref = new TypeRef<>() {
        };
        List<PetDto> getPet = RestAssured.when()
                .get(pathPetByName, postPet.getName())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .as(ref);
        Assertions.assertEquals(postPet.getName(), getPet.get(0).getName(), "Pets are different");
    }

    @Test
    public void getPets() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .post(pathPets)
                .then()
                .statusCode(HttpStatus.SC_OK);

        Long petId = petRepository.findByName(postPet.getName()).get(0).getId();

        LOGGER.info("Send GET to created pet with id = " + petId);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .get(pathPets)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void findPetByName() {
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

        Long petId = petRepository.findByName(postPet.getName()).get(0).getId();

        LOGGER.info("Send GET to created pet with id = " + petId);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .get(pathPetByName, postPet.getName())
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void findPetByType() {
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

        Long petId = petRepository.findByName(postPet.getName()).get(0).getId();

        LOGGER.info("Send GET to created pet with id = " + petId);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .get(pathPetsByType, postPet.getType())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .as(List.class);
    }

    @Test
    public void updatePet() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .post(pathPets)
                .then()
                .statusCode(HttpStatus.SC_OK);

        Long petId = petRepository.findByName(postPet.getName()).get(0).getId();

        LOGGER.info("Send PUT to created pet with id = " + petId);

        postPet.setColor("white");

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .put(pathPetById, petId)
                .then()
                .statusCode(HttpStatus.SC_OK);

        LOGGER.info("Send GET to created pet to check update");

        TypeRef<List<PetDto>> ref = new TypeRef<>() {
        };
        List<PetDto> getPet = RestAssured.when()
                .get(pathPetByName, postPet.getName())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .as(ref);
        Assertions.assertEquals("white", getPet.get(0).getColor(), "Color was not updated");
    }

    @Test
    public void deletePet() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPet)
                .when()
                .post(pathPets)
                .then()
                .statusCode(HttpStatus.SC_OK);

        Long petId = petRepository.findByName(postPet.getName()).get(0).getId();

        LOGGER.info("Send DELETE to created pet with id = " + petId);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .delete(pathPetById, petId)
                .then()
                .statusCode(HttpStatus.SC_OK);

        LOGGER.info("Send GET to created pet to check it was deleted");

        RestAssured.when()
                .get(pathPetByName, postPet.getName())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void addDonate() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postDonate)
                .when()
                .post(pathDonate, postDonate.getPetId())
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void getDonates() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postDonate)
                .when()
                .post(pathDonate, postDonate.getPetId())
                .then()
                .statusCode(HttpStatus.SC_OK);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .get(pathDonate, postDonate.getPetId())
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
