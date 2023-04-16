package com.jme.adopterdla.adopterdla.visits.controller;

import com.jme.adopterdla.adopterdla.visits.dto.ShelterVisitDTO;
import com.jme.adopterdla.adopterdla.visits.service.ShelterVisitService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/shelter-visits")
public class ShelterVisitController {

    private final ShelterVisitService shelterVisitService;

    public ShelterVisitController(ShelterVisitService shelterVisitService) {
        this.shelterVisitService = shelterVisitService;
    }

    @GetMapping("/{id}")
    public Mono<ShelterVisitDTO> getShelterVisitById(@PathVariable Long id) {
        return shelterVisitService.findById(id);
    }

    @GetMapping
    public Flux<ShelterVisitDTO> getAllShelterVisits() {
        return shelterVisitService.findAll();
    }

    @PostMapping
    public Mono<ShelterVisitDTO> createShelterVisit(@RequestBody ShelterVisitDTO shelterVisitDTO) {
        return shelterVisitService.save(shelterVisitDTO);
    }

    @PutMapping("/{id}")
    public Mono<ShelterVisitDTO> updateShelterVisit(@PathVariable Long id, @RequestBody ShelterVisitDTO shelterVisitDTO) {
        shelterVisitDTO = new ShelterVisitDTO(id, shelterVisitDTO.schedule(), shelterVisitDTO.animal(), shelterVisitDTO.adopter());
        return shelterVisitService.save(shelterVisitDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteShelterVisit(@PathVariable Long id) {
        return shelterVisitService.deleteById(id);
    }
}