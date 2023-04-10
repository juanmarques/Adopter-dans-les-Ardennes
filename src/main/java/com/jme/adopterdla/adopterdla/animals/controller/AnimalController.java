package com.jme.adopterdla.adopterdla.animals.controller;

import com.jme.adopterdla.adopterdla.animals.dto.AnimalDTO;
import com.jme.adopterdla.adopterdla.animals.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/animals")
@RequiredArgsConstructor
@Tag(name = "Animal", description = "API for Animal operations")
public class AnimalController {

    private final AnimalService animalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new animal")
    @ApiResponse(responseCode = "201", description = "Animal created", content = @Content(schema = @Schema(implementation = AnimalDTO.class)))
    public Mono<AnimalDTO> createAnimal(@RequestPart("imageData") FilePart imageData, @RequestPart("data") AnimalDTO animalDTO) {
        return animalService.createAnimal(animalDTO, imageData);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an animal by ID")
    @ApiResponse(responseCode = "200", description = "Animal found", content = @Content(schema = @Schema(implementation = AnimalDTO.class)))
    @ApiResponse(responseCode = "404", description = "Animal not found")
    public Mono<AnimalDTO> getAnimal(@Parameter(description = "Animal ID") @PathVariable Long id) {
        return animalService.getAnimal(id);
    }

    @GetMapping
    @Operation(summary = "Get all animals")
    @ApiResponse(responseCode = "200", description = "List of animals", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AnimalDTO.class))))
    public Flux<AnimalDTO> getAllAnimals() {
        return animalService.getAllAnimals();
    }

    @GetMapping("/available")
    @Operation(summary = "Get all animals by availability")
    @ApiResponse(responseCode = "200", description = "List of animals", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AnimalDTO.class))))
    public Flux<AnimalDTO> getAllAnimalsByIsAvailable(@Parameter(description = "Availability status") @RequestParam boolean isAvailable) {
        return animalService.getAllAnimalsByIsAvailable(isAvailable);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Update an animal by ID")
    @ApiResponse(responseCode = "204", description = "Animal updated")
    @ApiResponse(responseCode = "404", description = "Animal not found")
    public Mono<Void> updateAnimal(@Parameter(description = "Animal ID") @PathVariable("id") Long id,
                                   @RequestPart("data") AnimalDTO animalDTO,
                                   @RequestPart(value = "imageData",required = false) FilePart imageData) {
        return animalService.updateAnimal(id, animalDTO, imageData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an animal by ID")
    @ApiResponse(responseCode = "204", description = "Animal deleted")
    @ApiResponse(responseCode = "404", description = "Animal not found")
    public Mono<Void> deleteAnimal(@Parameter(description = "Animal ID") @PathVariable Long id) {
        return animalService.deleteAnimal(id);
    }
}
