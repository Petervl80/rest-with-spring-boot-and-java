package br.com.petervl80.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.petervl80.data.vo.v1.PersonVO;
import br.com.petervl80.exceptions.ResourceNotFoundException;
import br.com.petervl80.mapper.DozerMapper;
import br.com.petervl80.model.Person;
import br.com.petervl80.repositories.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	@Autowired
	private PersonRepository repository;

	public PersonVO findById(Long id) {

		logger.info("Finding one person!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		return DozerMapper.parseObject(entity, PersonVO.class);
	}

	public List<PersonVO> findAll() {

		logger.info("Finding all people!");

		return DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
	}

	public PersonVO create(PersonVO person) {

		logger.info("creating one person!");

		var entity = DozerMapper.parseObject(person, Person.class);

		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);

		return vo;
	}

	public PersonVO update(PersonVO person) {

		logger.info("updating one person!");

		var entity = DozerMapper.parseObject(findById(person.getId()), Person.class);

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());

		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);

		return vo;
	}

	public void delete(Long id) {

		logger.info("deleting one person!");

		var entity = DozerMapper.parseObject(findById(id), Person.class);

		repository.delete(entity);
	}
}
