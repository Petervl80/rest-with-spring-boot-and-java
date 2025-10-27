package com.github.petervl80.controller.docs;

import com.github.petervl80.data.dto.BookDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BookControllerDocs {

    @Operation(summary = "Finds all Books",
            description = "Finds all Books",
            tags = "Books",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
                            )),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            })
    List<BookDTO> findAll();

    @Operation(summary = "Finds a Book by ID",
            description = "Finds a Book by ID",
            tags = "Books",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookDTO.class)
                            )),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            })
    BookDTO findById(@PathVariable("id") Long id);

    @Operation(summary = "Creates a new Book",
            description = "Creates a new Book",
            tags = "Books",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookDTO.class)
                            )),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            })
    BookDTO create(@RequestBody BookDTO book);

    @Operation(summary = "Updates a Book",
            description = "Updates a Book",
            tags = "Books",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookDTO.class)
                            )),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            })
    BookDTO update(@RequestBody BookDTO book);

    @Operation(summary = "Deletes a Book",
            description = "Deletes a Book",
            tags = "Books",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            })
    ResponseEntity<?> delete(@PathVariable("id") Long id);
}
