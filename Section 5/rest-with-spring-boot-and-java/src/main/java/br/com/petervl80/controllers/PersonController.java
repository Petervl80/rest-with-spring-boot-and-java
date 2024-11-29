package br.com.petervl80.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.petervl80.data.vo.v1.PersonVO;
import br.com.petervl80.services.PersonServices;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {
	
	@Autowired
	private PersonServices service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<PersonVO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.findById(id));
	}
	
	@GetMapping
	public ResponseEntity<List<PersonVO>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}
	
	@PostMapping
	public ResponseEntity<PersonVO> create(@RequestBody PersonVO PersonVo) {
		return ResponseEntity.ok().body(service.create(PersonVo));
	}
	
	@PutMapping
	public ResponseEntity<PersonVO> update(@RequestBody PersonVO PersonVo) {
		return ResponseEntity.ok().body(service.update(PersonVo));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<PersonVO> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
