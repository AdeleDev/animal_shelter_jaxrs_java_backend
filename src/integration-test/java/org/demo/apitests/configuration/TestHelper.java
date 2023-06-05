package org.demo.apitests.configuration;

import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.SSLConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.demo.configuration.JacksonConfiguration;
import org.demo.defaultpackage.shelterservice.model.DonateDto;
import org.demo.defaultpackage.shelterservice.model.PetDto;
import org.demo.defaultpackage.shelterservice.model.UserDto;
import org.junit.jupiter.api.BeforeAll;

import java.lang.reflect.Type;
import java.time.LocalDateTime;


public class TestHelper {

    protected final String pathUserLogin = "/login";
    protected final String pathUserRegister = "/register";

    protected final String pathUsers = "/users";
    protected final String pathUserByEmail = "/users/findByEmail/{email}";
    protected final String pathUserById = " users/{userId}";
    protected final String pathPets = "/pets";
    protected final String pathPetById = "/pets/{petId}";
    protected final String pathDonate = "/pets/donate/{petId}";
    protected final String pathPetByName = "/pets/findByName/{name}";
    protected final String pathPetsByType = "/pets/findByType/{type}";
    protected PetDto postPet = getPet(RandomStringUtils.randomAlphanumeric(10), PetDto.TypeEnum.CAT);
    protected DonateDto postDonate = getDonate();


    public static UserDto getPerson(String firstname, String lastname) {
        UserDto person = new UserDto();
        person.setFirstName(firstname + RandomStringUtils.randomNumeric(10));
        person.setLastName(lastname);
        person.setEmail(firstname + "_" + lastname + RandomStringUtils.randomAlphanumeric(7) + "@mail.ru");
        person.setPhoneNumber(RandomStringUtils.randomNumeric(7));
        person.setPassword(RandomStringUtils.randomAlphanumeric(6));
        person.setGroup(UserDto.GroupEnum.GUEST);

        return person;
    }

    public static PetDto getPet(String name, PetDto.TypeEnum type) {
        PetDto pet = new PetDto();
        pet.setAge(1);
        pet.setGender(PetDto.GenderEnum.MALE);
        pet.setName(name);
        pet.setType(type);
        pet.setKind("Kind");
        pet.weight(1);
        pet.setColor("black");
        pet.setVaccinated(true);
        pet.setCastrated(true);
        pet.setSpecialTreatment(true);
        pet.setStory("Story");
        return pet;
    }

    public static DonateDto getDonate() {
        DonateDto donateDto = new DonateDto();
        donateDto.setPetId((long) LocalDateTime.now().getNano());
        donateDto.setUserId((long) LocalDateTime.now().getNano());
        donateDto.setSum(500L);
        return donateDto;
    }

    @BeforeAll
    static void setRestUrl() {
        RestAssured.port = Integer.parseInt(TestProperties.getProperty(TestProperties.PropertyName.SERVICE_PORT));
        RestAssured.baseURI = TestProperties.getProperty(TestProperties.PropertyName.SERVICE_URL);
        RestAssured.basePath = TestProperties.getProperty(TestProperties.PropertyName.SERVICE_BASE_PATH);
        RestAssured.config()
                .sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation())
                .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                        .jackson2ObjectMapperFactory(
                                (Type cls, String charset) -> new JacksonConfiguration().objectMapper()));

    }

}
