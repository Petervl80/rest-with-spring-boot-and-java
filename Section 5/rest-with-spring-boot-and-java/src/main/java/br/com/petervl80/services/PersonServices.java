package br.com.petervl80.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import br.com.petervl80.model.Person;

@Service
public class PersonServices {

	private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	public Person findById(String id) {

		logger.info("Finding one person!");

		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Pedro");
		person.setLastName("Lapa");
		person.setAddress("Algum lugar");
		person.setGender("Male");

		return person;
	}

	public List<Person> findAll() {
		
		logger.info("Finding all people!");

		List<Person> persons = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			Person person = mockPerson(i);
			persons.add(person);
		}

		return persons;
	}
	
	public Person create(Person person) {

		logger.info("creating one person!");

		return person;
	}
	
	public Person update(Person person) {

		logger.info("updating one person!");

		return person;
	}
	
	public void delete(String id) {

		logger.info("deleting one person!");
	}

	private Person mockPerson(int i) {
		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Person name " + i);
		person.setLastName("Laste name " + i);
		person.setAddress("Addres " + i);
		person.setGender("Male");

		return person;
	}

}
