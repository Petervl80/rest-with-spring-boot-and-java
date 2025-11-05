package com.github.petervl80.repository;

import com.github.petervl80.integrationtests.testcontainers.AbstractIntegrationTest;
import com.github.petervl80.model.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository repository;
    private static Person person;

    @BeforeAll
    static void setUp() {
        person = new Person();
    }

    @Test
    @Order(1)
    void findByFirstNameContaining() {
        Pageable pageable = PageRequest.of(0, 12,
                Sort.by(Sort.Direction.ASC, "firstName"));

        person = repository.findByFirstNameContaining("Ma", pageable).getContent().getFirst();

        assertNotNull(person);
        assertNotNull(person.getId());
        assertTrue(person.getFirstName().toLowerCase().contains("ma"));
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertNotNull(person.getEnabled());
    }

    @Test
    @Order(2)
    void disablePerson() {

        Long id = person.getId();
        repository.disablePerson(id);

        person = repository.findById(id).orElse(null);
        assertNotNull(person);
        assertNotNull(person.getId());
        assertTrue(person.getFirstName().toLowerCase().contains("ma"));
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertFalse(person.getEnabled());
    }
}