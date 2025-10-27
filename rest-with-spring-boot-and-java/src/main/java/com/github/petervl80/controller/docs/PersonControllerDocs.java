package com.github.petervl80.controller.docs;

import com.github.petervl80.data.dto.PersonDTO;
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

public interface PersonControllerDocs {

    @Operation(summary = "Finds all People",
            description = "Finds all People",
            tags = "People",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                            )),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            })
    List<PersonDTO> findAll();

    @Operation(summary = "Finds a Person by ID",
            description = "Finds a Person by ID",
            tags = "People",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PersonDTO.class)
                            )),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            })
    PersonDTO findById(@PathVariable("id") Long id);

    @Operation(summary = "Creates a new Person",
            description = "Creates a new Person",
            tags = "People",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PersonDTO.class)
                            )),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            })
    PersonDTO create(@RequestBody PersonDTO person);

    @Operation(summary = "Updates a Person",
            description = "Updates a Person",
            tags = "People",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PersonDTO.class)
                            )),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            })
    PersonDTO update(@RequestBody PersonDTO person);

    @Operation(summary = "Deletes a Person",
            description = "Deletes a Person",
            tags = "People",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            })
    ResponseEntity<?> delete(@PathVariable("id") Long id);
}
