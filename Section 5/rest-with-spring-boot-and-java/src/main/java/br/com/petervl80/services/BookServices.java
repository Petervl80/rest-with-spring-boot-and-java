package br.com.petervl80.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import br.com.petervl80.controllers.BookController;
import br.com.petervl80.data.vo.v1.BookVO;
import br.com.petervl80.exceptions.RequiredObjectIsNullException;
import br.com.petervl80.exceptions.ResourceNotFoundException;
import br.com.petervl80.mapper.DozerMapper;
import br.com.petervl80.model.Book;
import br.com.petervl80.repositories.BookRepository;

@Service
public class BookServices {

	private Logger logger = Logger.getLogger(BookServices.class.getName());

	@Autowired
	private BookRepository repository;

	public BookVO findById(Long id) {

		logger.info("Finding one book!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		var vo = DozerMapper.parseObject(entity, BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return vo;
	}

	public List<BookVO> findAll() {

		logger.info("Finding all book!");

		var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
		books.forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
		return books;
	}

	public BookVO create(BookVO book) {

		if(book == null) throw new RequiredObjectIsNullException();
		
		logger.info("creating one book!");

		var entity = DozerMapper.parseObject(book, Book.class);

		var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public BookVO update(BookVO book) {
		
		if(book == null) throw new RequiredObjectIsNullException();

		logger.info("updating one book!");

		var entity = DozerMapper.parseObject(findById(book.getKey()), Book.class);

		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());

		var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public void delete(Long id) {

		logger.info("deleting one book!");

		var entity = DozerMapper.parseObject(findById(id), Book.class);

		repository.delete(entity);
	}
}
