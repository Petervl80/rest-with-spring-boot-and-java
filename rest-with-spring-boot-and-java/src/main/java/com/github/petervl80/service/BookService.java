package com.github.petervl80.service;

import com.github.petervl80.controller.BookController;
import com.github.petervl80.data.dto.BookDTO;
import com.github.petervl80.exception.RequiredObjectIsNullException;
import com.github.petervl80.exception.ResourceNotFoundException;
import com.github.petervl80.model.Book;
import com.github.petervl80.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.petervl80.mapper.ObjectMapper.parseListObjects;
import static com.github.petervl80.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private final Logger logger = LoggerFactory.getLogger(BookService.class.getName());


    @Autowired
    private BookRepository repository;

    public List<BookDTO> findAll() {

        logger.info("Finding all Books!");

        List<BookDTO> books = parseListObjects(repository.findAll(), BookDTO.class);
        books.forEach(this::addingHateoasLinks);
        return books;
    }

    public BookDTO findById(Long id) {
        logger.info("Finding one Book!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        BookDTO dto = parseObject(entity, BookDTO.class);
        addingHateoasLinks(dto);
        return dto;
    }

    public BookDTO create(BookDTO book) {

        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Book!");

        var entity = parseObject(book, Book.class);

        BookDTO dto = parseObject(repository.save(entity), BookDTO.class);
        addingHateoasLinks(dto);
        return dto;
    }

    public BookDTO update(BookDTO book) {

        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Book!");
        Book entity = repository.findById(book.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        BookDTO dto = parseObject(repository.save(entity), BookDTO.class);
        addingHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {

        logger.info("Deleting one Person!");

        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }

    private void addingHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
