package com.github.petervl80.integrationtests.controller.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petervl80.config.TestConfigs;
import com.github.petervl80.integrationtests.dto.BookDTO;
import com.github.petervl80.integrationtests.dto.wrapper.json.WrapperBookDTO;
import com.github.petervl80.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static BookDTO book;
    private static Date launchDate;
    private static BigDecimal price;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        book = new BookDTO();
        launchDate = Date.from(Instant.parse("2004-06-12T00:00:00.000Z"));
        price = BigDecimal.valueOf(5);
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockBook();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/books/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter((LogDetail.ALL)))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        book = objectMapper.readValue(content, BookDTO.class);

        assertNotNull(book.getId());

        assertTrue(book.getId() > 0);

        assertEquals("Steve McConnell", book.getAuthor());
        assertNotNull(book.getLaunchDate());

        assertEquals(price, book.getPrice());
        assertEquals("Code complete", book.getTitle());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        book.setAuthor("Steve McConnell Jr.");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        book = objectMapper.readValue(content, BookDTO.class);

        assertNotNull(book.getId());

        assertTrue(book.getId() > 0);

        assertEquals("Steve McConnell Jr.", book.getAuthor());
        assertNotNull(book.getLaunchDate());
        assertEquals(price, book.getPrice());
        assertEquals("Code complete", book.getTitle());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        book = objectMapper.readValue(content, BookDTO.class);

        assertNotNull(book.getId());

        assertTrue(book.getId() > 0);

        assertEquals("Steve McConnell Jr.", book.getAuthor());
        assertNotNull(book.getLaunchDate());
        assertEquals(0, book.getPrice().compareTo(price));
        assertEquals("Code complete", book.getTitle());
    }

    @Test
    @Order(4)
    void deleteTest(){

        given(specification)
                .pathParam("id", book.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    void findAllTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 0, "size", 5, "direction", "desc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body().asString();

        WrapperBookDTO wrapper = objectMapper.readValue(content, WrapperBookDTO.class);
        List<BookDTO> books = wrapper.getEmbedded().getBooks();

        BookDTO bookOne = books.getFirst();

        assertNotNull(bookOne.getId());

        assertTrue(bookOne.getId() > 0);

        assertEquals("Michael C. Feathers", bookOne.getAuthor());
        assertNotNull(book.getLaunchDate());
        assertEquals(0, bookOne.getPrice().compareTo(BigDecimal.valueOf(49)));
        assertEquals("Working effectively with legacy code", bookOne.getTitle());
    }

    private void mockBook() {
        book.setAuthor("Steve McConnell");
        book.setLaunchDate(launchDate);
        book.setPrice(price);
        book.setTitle("Code complete");
    }
}