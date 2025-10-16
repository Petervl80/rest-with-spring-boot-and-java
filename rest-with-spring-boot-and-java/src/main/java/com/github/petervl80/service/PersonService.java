package com.github.petervl80.service;

import com.github.petervl80.exception.ResourceNotFoundException;
import com.github.petervl80.model.Person;
import com.github.petervl80.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonService {
    
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private PersonRepository repository;
    
    public Person create(Person person) {
        return repository.save(person);
    }
    
    public Person update(Person person) {
        Person entity = findById(person.getId());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return repository.save(entity);
    }    
    
    public void delete(Long id) {
        Person entity = findById(id);
        repository.delete(entity);
    }
    
    public Person findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
    }
    
    public List<Person> findAll() {
        return repository.findAll();
    }
}
