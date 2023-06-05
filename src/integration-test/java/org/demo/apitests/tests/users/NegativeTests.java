package org.demo.apitests.tests.users;

import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.demo.apitests.configuration.TestHelper;
import org.demo.defaultpackage.shelterservice.model.UserDto;
import org.demo.entity.UserEntity;
import org.demo.repository.UserRepository;
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
    UserDto postPerson = getPerson(RandomStringUtils.randomNumeric(10), "PersonNegative");
    @Autowired
    private UserRepository repository;
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
    public void registerUserAlreadyExists() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .post(pathUserRegister)
                .then()
                .statusCode(HttpStatus.SC_OK);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .post(pathUserRegister)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void registerUserWrongData() {
        UserDto postPerson = getPerson(RandomStringUtils.randomNumeric(31), "Person");
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .post(pathUserRegister)
                .then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void loginUserWrongEmail() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .post(pathUserRegister)
                .then()
                .statusCode(HttpStatus.SC_OK);

        Long personId = repository.findByEmail(postPerson.getEmail()).getId();

        LOGGER.info("Send GET to created person with id = " + personId);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .queryParam("email", postPerson.getEmail() + "tmp")
                .queryParam("password", postPerson.getPassword())
                .when()
                .get(pathUserLogin)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void loginUserWrongPass() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .post(pathUserRegister)
                .then()
                .statusCode(HttpStatus.SC_OK);

        Long personId = repository.findByEmail(postPerson.getEmail()).getId();

        LOGGER.info("Send GET to created person with id = " + personId);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .queryParam("email", postPerson.getEmail())
                .queryParam("password", postPerson.getPassword() + "tmp")
                .when()
                .get(pathUserLogin)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void getUsersNotFound() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .get(pathUsers)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void findUserByEmailNotFound() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .post(pathUserRegister)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .as(UserEntity.class);
        Long personId = repository.findByEmail(postPerson.getEmail()).getId();

        LOGGER.info("Send GET to created person with id = " + personId);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .get(pathUserByEmail, postPerson.getEmail() + "tmp")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void updateUserNotFound() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .put(pathUserById, 1L)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void updateUserWrongData() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .post(pathUserRegister)
                .then()
                .statusCode(HttpStatus.SC_OK);

        Long personId = repository.findByEmail(postPerson.getEmail()).getId();

        LOGGER.info("Send PUT to created person with id = " + personId);

        postPerson.setFirstName(RandomStringUtils.randomAlphabetic(31));

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .put(pathUserById, personId)
                .then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void deleteUserNotFound() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .delete(pathUserById, 1L)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}