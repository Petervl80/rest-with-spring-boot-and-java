package com.github.petervl80.integrationtests.controller.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petervl80.config.TestConfigs;
import com.github.petervl80.integrationtests.dto.AccountCredentialsDTO;
import com.github.petervl80.integrationtests.dto.PersonDTO;
import com.github.petervl80.integrationtests.dto.TokenDTO;
import com.github.petervl80.integrationtests.dto.wrapper.json.WrapperPersonDTO;
import com.github.petervl80.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(0)
    void signin() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("leandro", "admin123");

        tokenDTO = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().as(TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/persons/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter((LogDetail.ALL)))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        person = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(person.getId());

        assertTrue(person.getId() > 0);

        assertEquals("Richard", person.getFirstName());
        assertEquals("Stallman", person.getLastName());
        assertEquals("New York City, New York, US", person.getAddress());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        person.setLastName("Stallman Jr.");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        person = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(person.getId());

        assertTrue(person.getId() > 0);

        assertEquals("Richard", person.getFirstName());
        assertEquals("Stallman Jr.", person.getLastName());
        assertEquals("New York City, New York, US", person.getAddress());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        person = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(person.getId());
        assertTrue(person.getId() > 0);

        assertEquals("Richard", person.getFirstName());
        assertEquals("Stallman Jr.", person.getLastName());
        assertEquals("New York City, New York, US", person.getAddress());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(4)
    void disablePersonTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        person = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(person.getId());
        assertTrue(person.getId() > 0);

        assertEquals("Richard", person.getFirstName());
        assertEquals("Stallman Jr.", person.getLastName());
        assertEquals("New York City, New York, US", person.getAddress());
        assertEquals("Male", person.getGender());
        assertFalse(person.getEnabled());
    }

    @Test
    @Order(5)
    void deleteTest(){

        given(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 2, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body().asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();
//        List<PersonDTO> people = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>() {});

        PersonDTO personOne = people.getFirst();

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Aleece", personOne.getFirstName());
        assertEquals("Howgego", personOne.getLastName());
        assertEquals("3 Dakota Park", personOne.getAddress());
        assertEquals("Female", personOne.getGender());
        assertFalse(personOne.getEnabled());

        PersonDTO personTwo = people.get(1);

        assertNotNull(personTwo.getId());
        assertTrue(personTwo.getId() > 0);

        assertEquals("Alejoa", personTwo.getFirstName());
        assertEquals("Heugle", personTwo.getLastName());
        assertEquals("442 Declaration Road", personTwo.getAddress());
        assertEquals("Male", personTwo.getGender());
        assertTrue(personTwo.getEnabled());
    }

    @Test
    @Order(7)
    void findByNameTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("firstName", "and")
                .queryParams("page", 0, "size", 2, "direction", "desc")
                .when()
                .get("/findByFirstName/{firstName}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body().asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();
//        List<PersonDTO> people = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>() {});

        PersonDTO personOne = people.getFirst();

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Shandie", personOne.getFirstName());
        assertEquals("Maywood", personOne.getLastName());
        assertEquals("8904 Maple Wood Park", personOne.getAddress());
        assertEquals("Female", personOne.getGender());
        assertFalse(personOne.getEnabled());

        PersonDTO personTwo = people.get(1);

        assertNotNull(personTwo.getId());
        assertTrue(personTwo.getId() > 0);

        assertEquals("Sanders", personTwo.getFirstName());
        assertEquals("Richard", personTwo.getLastName());
        assertEquals("2682 Paget Place", personTwo.getAddress());
        assertEquals("Male", personTwo.getGender());
        assertFalse(personTwo.getEnabled());
    }

    private void mockPerson() {
        person.setFirstName("Richard");
        person.setLastName("Stallman");
        person.setAddress("New York City, New York, US");
        person.setGender("Male");
        person.setEnabled(true);
    }
}