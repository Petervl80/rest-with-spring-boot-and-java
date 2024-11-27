package br.com.petervl80.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.petervl80.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
