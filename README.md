# Animal Shelter Backend App (JaxRS)

Animal Shelter Application backend based on jaxRs. Working with h2 DB via Hibernate.
Working in pair with [Frontend React App](https://github.com/AdeleDev/animal_shelter_react_frontend). 
Backend performs next modules:

* User: can add, update, delete users
* Pet: can add, update, delete new pets of type cat and dog, seracg pet by type, name
* Donate: donation of animal, statistic of pet donations

[//]: # (Book: book an animal to take away)

### Built With

* [![Java][Java.io]][Java-url]
* [![SpringBoot][SpringBoot.io]][SpringBoot-url]
* [![Hibernate][Hibernate.io]][Hibernate-url]
* [![OpenApi][OpenApi.io]][OpenApi-url]
* [![Wiremock][Wiremock.io]][Wiremock-url]
* [![Junit5][Junit5.io]][Junit5-url]

## Pre-installations

#### Clone the repo:

```sh
git clone https://github.com/AdeleDev/animal_shelter_jaxrs_java_backend.git
```

#### Build project:

```sh
gradle clean
```

```sh
gradle build
```

#### Build interface and objects for service:

```sh
gradle generateApiAndModel
```

## Usage

#### Setup endpoints for client and server, set connection to db:

```
resources/application.yaml
```

#### Run service:

```sh
gradle bootRun
```

## API example requests:

API :
```
http://127.0.0.1:8080/service/shelter/
```

Get all cats:
```
http://127.0.0.1:8080/service/shelter/pets/findByName/name
```

Post donation :
```
http://127.0.0.1:8080/service/shelter/donate/1
```

<!-- MARKDOWN LINKS & IMAGES -->

[Java.io]: https://img.shields.io/badge/-☕%20Java-blue?style=for-the-badge

[Java-url]: https://www.java.com/ru/

[SpringBoot.io]: https://img.shields.io/badge/-Springboot-green?style=for-the-badge&logo=springboot

[SpringBoot-url]: https://spring.io/projects/spring-boot

[Hibernate.io]: https://img.shields.io/badge/-Hibernate-gray?style=for-the-badge&logo=hibernate

[Hibernate-url]: https://hibernate.org/

[OpenApi.io]: https://img.shields.io/badge/-OpenApi-blueviolet?style=for-the-badge&logo=openapiinitiative

[OpenApi-url]: https://www.openapis.org/

[Wiremock.io]: https://img.shields.io/badge/-🍊%20Wiremock-lightblue?style=for-the-badge

[Wiremock-url]: https://wiremock.org/

[RestAssured.io]: https://img.shields.io/badge/-🪐️%20Rest%20Assured-brightgreen?style=for-the-badge&logo=restAssured

[RestAssured-url]: https://rest-assured.io/

[Junit5.io]: https://img.shields.io/badge/-JUnit5-yellow?style=for-the-badge&logo=JUnit5

[Junit5-url]: https://junit.org/junit5/
