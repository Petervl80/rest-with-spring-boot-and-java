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

import br.com.petervl80.data.vo.v1.BookVO;
import br.com.petervl80.services.BookServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Book", description = "Endpoints for Managing Book")
public class BookController {

	@Autowired
	private BookServices service;

	@GetMapping(value = "/{id}")
	@Operation(summary = "Finds a Book", description = "Finds a Book", tags = { "Book" }, responses = {
			@ApiResponse(description = "Sucess", responseCode = "200", content =
					@Content(schema = @Schema(implementation = BookVO.class))),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) })
	public ResponseEntity<BookVO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.findById(id));
	}

	@GetMapping
	@Operation(summary = "Finds all Book", description = "Finds all Book", tags = { "Book" }, responses = {
			@ApiResponse(description = "Sucess", responseCode = "200", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BookVO.class))) }),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) })
	public ResponseEntity<List<BookVO>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}

	@PostMapping
	@Operation(summary = "Adds a new Book",
		description = "Adds a new Book by passing in a JSON, XML or YML representation of the book", tags = { "Book" },
		responses = {
			@ApiResponse(description = "Sucess", responseCode = "200", content =
					@Content(schema = @Schema(implementation = BookVO.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) })
	public ResponseEntity<BookVO> create(@RequestBody BookVO BookVo) {
		return ResponseEntity.ok().body(service.create(BookVo));
	}

	@PutMapping
	@Operation(summary = "Updates a Book",
	description = "Updates a Book by passing in a JSON, XML or YML representation of the book", tags = { "Book" },
	responses = {
		@ApiResponse(description = "Updated", responseCode = "200", content =
				@Content(schema = @Schema(implementation = BookVO.class))),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) })
	public ResponseEntity<BookVO> update(@RequestBody BookVO BookVo) {
		return ResponseEntity.ok().body(service.update(BookVo));
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a Book",
	description = "Deletes a Book by passing in a JSON, XML or YML representation of the book", tags = { "Book" },
	responses = {
		@ApiResponse(description = "No content", responseCode = "204", content = @Content),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) })
	public ResponseEntity<BookVO> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
