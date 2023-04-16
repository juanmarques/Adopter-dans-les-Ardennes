package com.jme.adopterdla.adopterdla.visits.service;

import com.jme.adopterdla.adopterdla.adopters.entity.Adopter;
import com.jme.adopterdla.adopterdla.adopters.mapper.AdopterMapper;
import com.jme.adopterdla.adopterdla.adopters.repository.AdopterRepository;
import com.jme.adopterdla.adopterdla.animals.entity.Animal;
import com.jme.adopterdla.adopterdla.animals.mapper.AnimalMapper;
import com.jme.adopterdla.adopterdla.animals.repository.AnimalRepository;
import com.jme.adopterdla.adopterdla.common.entity.Schedule;
import com.jme.adopterdla.adopterdla.common.entity.repository.ScheduleRepository;
import com.jme.adopterdla.adopterdla.common.mapper.ScheduleMapper;
import com.jme.adopterdla.adopterdla.visits.dto.ShelterVisitDTO;
import com.jme.adopterdla.adopterdla.visits.entity.ShelterVisit;
import com.jme.adopterdla.adopterdla.visits.mapper.ShelterVisitMapper;
import com.jme.adopterdla.adopterdla.visits.repository.ShelterVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ShelterVisitServiceImpl implements ShelterVisitService {

    private final ShelterVisitRepository shelterVisitRepository;
    private final AnimalRepository animalRepository;
    private final AdopterRepository adopterRepository;
    private final ScheduleRepository schedulerRepository;
    private final ScheduleMapper scheduleMapper;
    private final AnimalMapper animalMapper;
    private final AdopterMapper adopterMapper;
    private final ShelterVisitMapper shelterVisitMapper;

    /**
     * Find a ShelterVisit by id.
     *
     * @param id The id of the ShelterVisit to find.
     * @return A Mono of the ShelterVisitDTO.
     */
    @Override
    public Mono<ShelterVisitDTO> findById(Long id) {
        return shelterVisitRepository.findById(id)
                .flatMap(this::toDTO);
    }

    /**
     * Find all ShelterVisits.
     *
     * @return A Flux of all ShelterVisitDTOs.
     */
    @Override
    public Flux<ShelterVisitDTO> findAll() {
        return shelterVisitRepository.findAll()
                .flatMap(this::toDTO);
    }

    /**
     * Save a ShelterVisit.
     *
     * @param shelterVisitDTO The ShelterVisitDTO to save.
     * @return A Mono of the saved ShelterVisitDTO.
     */
    @Override
    public Mono<ShelterVisitDTO> save(ShelterVisitDTO shelterVisitDTO) {
        // If the id is null, it's a new ShelterVisit.
        if (shelterVisitDTO.id() == null) {
            ShelterVisit shelterVisit = shelterVisitMapper.toEntity(shelterVisitDTO);
            return shelterVisitRepository.save(shelterVisit)
                    .flatMap(this::toDTO);
        } else {
            // If the id is not null, find the existing ShelterVisit and update it.
            return shelterVisitRepository.findById(shelterVisitDTO.id())
                    .flatMap(existingShelterVisit -> {
                        shelterVisitMapper.updateShelterVisitFromDTO(shelterVisitDTO, existingShelterVisit);
                        return shelterVisitRepository.save(existingShelterVisit);
                    })
                    .flatMap(this::toDTO);
        }
    }
    /**
     * Delete a ShelterVisit by id.
     *
     * @param id The id of the ShelterVisit to delete.
     * @return A Mono of void indicating completion of the deletion operation.
     */
    @Override
    public Mono<Void> deleteById(Long id) {
        return shelterVisitRepository.deleteById(id);
    }

    /**
     * Convert a ShelterVisit entity to a ShelterVisitDTO.
     *
     * @param shelterVisit The ShelterVisit entity to convert.
     * @return A Mono of the converted ShelterVisitDTO.
     */
    private Mono<ShelterVisitDTO> toDTO(ShelterVisit shelterVisit) {
        if (shelterVisit == null) {
            return Mono.empty();
        }

        // Retrieve related entities using their respective repositories
        Mono<Animal> animalMono = animalRepository.findById(shelterVisit.getAnimalId());
        Mono<Adopter> adopterMono = adopterRepository.findById(shelterVisit.getAdopterId());
        Mono<Schedule> scheduleMono = schedulerRepository.findById(shelterVisit.getScheduleId());

        // Zip the related entities and map them to a ShelterVisitDTO
        return Mono.zip(scheduleMono, animalMono, adopterMono)
                .map(tuple -> {
                    Schedule schedule = tuple.getT1();
                    Animal animal = tuple.getT2();
                    Adopter adopter = tuple.getT3();

                    return new ShelterVisitDTO(
                            shelterVisit.getId(),
                            scheduleMapper.toDTO(schedule),
                            animalMapper.toAnimalDTO(animal),
                            adopterMapper.toAdopterDTO(adopter)
                    );
                });
    }

}

