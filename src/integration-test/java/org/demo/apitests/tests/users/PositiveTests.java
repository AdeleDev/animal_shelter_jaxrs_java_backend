package org.demo.apitests.tests.users;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.demo.apitests.configuration.TestHelper;
import org.demo.defaultpackage.shelterservice.model.UserDto;
import org.demo.entity.UserEntity;
import org.demo.repository.UserRepository;
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

    UserDto postPerson = getPerson("Add", "Person");

    @Test
    public void registerUser() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .post(pathUserRegister)
                .then()
                .statusCode(HttpStatus.SC_OK);

        Long personId = repository.findByEmail(postPerson.getEmail()).getId();

        LOGGER.info("Send GET to created person with id = " + personId);

        UserDto getPerson = RestAssured.when()
                .get(pathUserByEmail, postPerson.getEmail())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .as(UserDto.class);
        Assertions.assertEquals(postPerson.getEmail(), getPerson.getEmail(), "Users are different");
    }

    @Test
    public void loginUser() {
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
                .queryParam("password", postPerson.getPassword())
                .when()
                .get(pathUserLogin)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void getUsers() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .post(pathUserRegister)
                .then()
                .statusCode(HttpStatus.SC_OK);

        List users = RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .get(pathUsers)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .as(List.class);

        int initialAmount = users.size();

        LOGGER.info("Amount of users found: " + initialAmount);

        postPerson.setEmail(postPerson.getEmail() + 2);
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .post(pathUserRegister)
                .then()
                .statusCode(HttpStatus.SC_OK);

        users = RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .get(pathUsers)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .as(List.class);
        LOGGER.info("Amount of users found: " + users.size());
        Assertions.assertEquals(initialAmount + 1, users.size(), "New user was not returned in list");
    }

    @Test
    public void findUserByEmail() {
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
                .get(pathUserByEmail, postPerson.getEmail())
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void updateUser() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .post(pathUserRegister)
                .then()
                .statusCode(HttpStatus.SC_OK);

        Long personId = repository.findByEmail(postPerson.getEmail()).getId();

        LOGGER.info("Send PUT to created person with id = " + personId);

        postPerson.setFirstName("NewName");

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .put(pathUserById, personId)
                .then()
                .statusCode(HttpStatus.SC_OK);

        LOGGER.info("Send GET to created person to check update");

        UserDto getPerson = RestAssured.when()
                .get(pathUserByEmail, postPerson.getEmail())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .as(UserDto.class);
        Assertions.assertEquals("NewName", getPerson.getFirstName(), "Name was not updated!");
    }

    @Test
    public void deleteUser() {
        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .body(postPerson)
                .when()
                .post(pathUserRegister)
                .then()
                .statusCode(HttpStatus.SC_OK);

        Long personId = repository.findByEmail(postPerson.getEmail()).getId();

        LOGGER.info("Send DELETE to created person with id = " + personId);

        RestAssured.given()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .when()
                .delete(pathUserById, personId)
                .then()
                .statusCode(HttpStatus.SC_OK);

        LOGGER.info("Send GET to created person to check it was deleted");

        RestAssured.when()
                .get(pathUserByEmail, postPerson.getEmail())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
