package com.jme.adopterdla.adopterdla.animals.service;

import com.jme.adopterdla.adopterdla.animals.dto.AnimalDTO;
import com.jme.adopterdla.adopterdla.animals.entity.Animal;
import com.jme.adopterdla.adopterdla.animals.mapper.AnimalMapper;
import com.jme.adopterdla.adopterdla.animals.repository.AnimalRepository;
import com.jme.adopterdla.adopterdla.common.utils.AzurePersistImageUtils;
import com.jme.adopterdla.adopterdla.configs.AzureFileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Log4j2
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;
    private final AzureFileUploadService azureFileUploadService;
    private final AzurePersistImageUtils azurePersistImageUtils;

    /**
     * Creates a new animal with the given data and image data.
     *
     * @param animalDTO the data for the animal to be created
     * @param imageData the image data for the animal to be created
     * @return a Mono containing the created AnimalDTO
     */
    public Mono<AnimalDTO> createAnimal(AnimalDTO animalDTO, FilePart imageData) {

        // Save the image data to a storage service and get the public URL of the saved image
        return azurePersistImageUtils.saveImageData(imageData.content(), azurePersistImageUtils.getExtensionByStringHandling(imageData.filename()).orElse("jpeg"))
                .flatMap(publicImageUrl -> {
                    // Create a new animal from the given data and set its image URL to the public URL of the saved image
                    Animal animal = animalMapper.toAnimal(animalDTO);
                    animal.setImageUrl(publicImageUrl);
                    animal.setCode(generateCode());
                    // Save the new animal to the database and return its DTO
                    return animalRepository.save(animal)
                            .map(animalMapper::toAnimalDTO);
                });
    }


    /**
     * Get an animal by ID
     *
     * @param id the ID of the animal
     * @return the animal with the given ID, or empty if not found
     */
    public Mono<AnimalDTO> getAnimal(Long id) {
        return animalRepository.findById(id)
                .map(animalMapper::toAnimalDTO);
    }

    /**
     * Get all animals
     *
     * @return all animals
     */
    public Flux<AnimalDTO> getAllAnimals() {
        return animalRepository.findAll()
                .map(animalMapper::toAnimalDTO);
    }

    /**
     * Get all animals by availability
     *
     * @param isAvailable the availability status to filter by
     * @return all animals with the given availability status
     */
    public Flux<AnimalDTO> getAllAnimalsByIsAvailable(boolean isAvailable) {
        Flux<Animal> animals = animalRepository.findAllByIsAvailable(isAvailable);
        return animals.map(animalMapper::toAnimalDTO);
    }

    /**
     * Updates an animal with the given ID and data, including an optional image.
     *
     * @param id         The ID of the animal to update.
     * @param animalDTO  The data to update the animal with.
     * @param imageData  The image data to update the animal with, or null if no image is being updated.
     * @return A Mono<Void> that completes when the animal has been updated.
     */
    public Mono<Void> updateAnimal(Long id, AnimalDTO animalDTO, FilePart imageData) {
        return animalRepository.findById(id)
                .flatMap(animal -> {
                    if (imageData != null) {
                        // If an image is being updated, delete the old image and upload the new one.
                        Mono.fromCallable(() -> azureFileUploadService.deleteImage(animal.getImageUrl()))
                                .subscribeOn(Schedulers.boundedElastic())
                                .subscribe();
                        return azurePersistImageUtils.saveImageData(imageData.content(), azurePersistImageUtils.getExtensionByStringHandling(imageData.filename()).orElse("jpeg"))
                                .flatMap(publicImageUrl -> {
                                    animal.setImageUrl(publicImageUrl);
                                    return Mono.just(animal);
                                });
                    }
                    // If no image is being updated, return the animal as is.
                    return Mono.just(animal);
                })
                // Map the updated animal data to an Animal object.
                .map(animal -> animalMapper.toAnimalUpdate(animalDTO, animal))
                // Save the updated animal to the database.
                .flatMap(animalRepository::save)
                // Complete the Mono when the animal has been saved.
                .then();
    }

    /**
     * Delete an animal by ID
     *
     * @param id the ID of the animal to delete
     * @return an empty Mono when the deletion is complete
     */
    public Mono<Void> deleteAnimal(Long id) {
        return animalRepository.deleteById(id);
    }

    public String generateCode() {
        Random random = new Random();
        int codeNumber = random.nextInt(9000) + 1000; // generate a random number between 1000 and 9999
        return Integer.toString(codeNumber);
    }

}
