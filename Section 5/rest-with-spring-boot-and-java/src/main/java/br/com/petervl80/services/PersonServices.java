package br.com.petervl80.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.petervl80.exceptions.ResourceNotFoundException;
import br.com.petervl80.model.Person;
import br.com.petervl80.repositories.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	@Autowired
	private PersonRepository repository;

	public Person findById(Long id) {

		logger.info("Finding one person!");

		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
	}

	public List<Person> findAll() {

		logger.info("Finding all people!");

		return repository.findAll();
	}

	public Person create(Person person) {

		logger.info("creating one person!");

		return repository.save(person);
	}

	public Person update(Person person) {

		logger.info("updating one person!");
		
		var entity = findById(person.getId());
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());

		return repository.save(entity);
	}

	public void delete(Long id) {

		logger.info("deleting one person!");
		
		var entity = findById(id);
		
		repository.delete(entity);
	}
}
