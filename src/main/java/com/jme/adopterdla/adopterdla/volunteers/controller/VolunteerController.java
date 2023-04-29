package com.jme.adopterdla.adopterdla.volunteers.controller;

import com.jme.adopterdla.adopterdla.volunteers.dto.VolunteerDTO;
import com.jme.adopterdla.adopterdla.volunteers.service.VolunteerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a volunteer by ID")
    public Mono<VolunteerDTO> getVolunteerById(
            @Parameter(description = "Volunteer ID", required = true)
            @PathVariable Long id) {
        return volunteerService.findById(id);
    }

    @GetMapping
    @Operation(summary = "Get all volunteers")
    public Flux<VolunteerDTO> getAllVolunteers() {
        return volunteerService.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new volunteer")
    public Mono<VolunteerDTO> createVolunteer(
            @Parameter(description = "Volunteer DTO with the volunteer's information", required = true, schema = @Schema(implementation = VolunteerDTO.class))
            @RequestBody VolunteerDTO volunteerDTO) {
        return volunteerService.save(volunteerDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a volunteer by ID")
    public Mono<VolunteerDTO> updateVolunteer(
            @Parameter(description = "Volunteer ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Volunteer DTO with updated information", required = true, schema = @Schema(implementation = VolunteerDTO.class))
            @RequestBody VolunteerDTO volunteerDTO) {
        return volunteerService.save(volunteerDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a volunteer by ID")
    public Mono<Void> deleteVolunteer(
            @Parameter(description = "Volunteer ID", required = true)
            @PathVariable Long id) {
        return volunteerService.deleteById(id);
    }
}