package com.jme.adopterdla.adopterdla.animals.repository;

import com.jme.adopterdla.adopterdla.animals.entity.Animal;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AnimalRepository extends ReactiveCrudRepository<Animal, Long> {

    Flux<Animal> findAllByIsAvailable(boolean isAvailable);
}
