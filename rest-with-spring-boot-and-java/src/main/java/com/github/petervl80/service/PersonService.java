package com.github.petervl80.service;

import com.github.petervl80.controller.PersonController;
import com.github.petervl80.data.dto.PersonDTO;
import com.github.petervl80.exception.BadRequestException;
import com.github.petervl80.exception.FileStorageException;
import com.github.petervl80.exception.RequiredObjectIsNullException;
import com.github.petervl80.exception.ResourceNotFoundException;
import com.github.petervl80.file.exporter.contract.PersonExporter;
import com.github.petervl80.file.exporter.factory.FileExporterFactory;
import com.github.petervl80.file.importer.contract.FileImporter;
import com.github.petervl80.file.importer.factory.FileImporterFactory;
import com.github.petervl80.model.Person;
import com.github.petervl80.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static com.github.petervl80.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    private final Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    FileImporterFactory importerFactory;

    @Autowired
    FileExporterFactory exporterFactory;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {

        logger.info("Finding all People!");

        var people = repository.findAll(pageable);

        return buildPagedModel(people, methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        String.valueOf(pageable.getSort())));
    }

    public PagedModel<EntityModel<PersonDTO>> findByFirstName(String firstName, Pageable pageable) {

        logger.info("Finding People by name!");

        var people = repository.findByFirstNameContaining(firstName, pageable);

        return buildPagedModel(people, methodOn(PersonController.class)
                .findByFirstName(firstName,
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        String.valueOf(pageable.getSort())));
    }

    public Resource exportPerson(Long id, String acceptHeader) {
        logger.info("Exporting data of a Person!");

        var person = repository.findById(id)
                .map(entity -> parseObject(entity, PersonDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        try {
            PersonExporter exporter = this.exporterFactory.getExporter(acceptHeader);
            return exporter.exportPerson(person);
        } catch (Exception e) {
            throw new RuntimeException("Error during file export!", e);
        }
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        PersonDTO dto = parseObject(entity, PersonDTO.class);
        addingHateoasLinks(dto);
        return dto;
    }

    public Resource exportPage(Pageable pageable, String acceptHeader) {

        logger.info("Exporting a People page!");

        List<PersonDTO> people = repository.findAll(pageable)
                .map(person -> parseObject(person, PersonDTO.class))
                .getContent();

        try {
            PersonExporter exporter = this.exporterFactory.getExporter(acceptHeader);
            return exporter.exportPeople(people);
        } catch (Exception e) {
            throw new RuntimeException("Error during file export!", e);
        }
    }

    public PersonDTO create(PersonDTO person) {

        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Person!");

        var entity = parseObject(person, Person.class);

        PersonDTO dto = parseObject(repository.save(entity), PersonDTO.class);
        addingHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> massCreation(MultipartFile file) {

        logger.info("Importing People from file!");

        if (file.isEmpty()) throw new BadRequestException("Please set a Valid File!");

        try (InputStream inputStream = file.getInputStream()) {
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name cannot be null"));
            FileImporter importer = this.importerFactory.getImporter(fileName);

            List<Person> entities = importer.importFile(inputStream)
                    .stream()
                    .map(dto -> repository.save(parseObject(dto, Person.class)))
                    .toList();

            return entities.stream()
                    .map(person -> {
                        PersonDTO dto = parseObject(person, PersonDTO.class);
                        addingHateoasLinks(dto);
                        return dto;
                    }).toList();
        } catch (Exception e) {
            throw new FileStorageException("Error processing the file!");
        }
    }

    public PersonDTO update(PersonDTO person) {

        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        PersonDTO dto = parseObject(repository.save(entity), PersonDTO.class);
        addingHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {

        logger.info("Disabling one Person!");

        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.disablePerson(id);
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        PersonDTO dto = parseObject(repository.save(entity), PersonDTO.class);
        addingHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {

        logger.info("Deleting one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Page<Person> people, ResponseEntity<PagedModel<EntityModel<PersonDTO>>> pageable) {
        var peopleWithLinks = people.map(person -> {
            PersonDTO dto = parseObject(person, PersonDTO.class);
            addingHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = linkTo(pageable)
                .withSelfRel();

        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private void addingHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class)
                .findByFirstName("", 1, 12, "asc"))
                .withRel("findByFirstName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(0, 12, "asc"))
                .withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).massCreation(null)).withRel("massCreation").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable")
                .withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).exportPage(0, 12, "asc", null))
                .withRel("exportPage").withType("GET").withTitle("Export People"));
    }
}
