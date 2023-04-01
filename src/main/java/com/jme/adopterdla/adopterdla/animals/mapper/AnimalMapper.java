package com.jme.adopterdla.adopterdla.animals.mapper;

import com.jme.adopterdla.adopterdla.animals.dto.AnimalDTO;
import com.jme.adopterdla.adopterdla.animals.entity.Animal;
import org.mapstruct.*;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AnimalMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "breed", target = "breed"),
            @Mapping(source = "arrivalDate", target = "arrivalDate"),
            @Mapping(source = "gender", target = "gender"),
            @Mapping(source = "age", target = "age"),
            @Mapping(source = "vaccinated", target = "vaccinated",defaultValue = "false"),
            @Mapping(source = "castrated", target = "castrated",defaultValue = "false"),
            @Mapping(source = "wormed", target = "wormed",defaultValue = "false"),
            @Mapping(source = "imageUrl", target = "imageUrl"),
            @Mapping(source = "electronicChip", target = "electronicChip"),
            @Mapping(source = "illness", target = "illness"),
            @Mapping(source = "notes", target = "notes"),
            @Mapping(source = "isAvailable", target = "isAvailable",defaultValue = "false")
    })
    AnimalDTO toAnimalDTO(Animal animal);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "breed", target = "breed"),
            @Mapping(source = "arrivalDate", target = "arrivalDate"),
            @Mapping(source = "gender", target = "gender"),
            @Mapping(source = "age", target = "age"),
            @Mapping(source = "vaccinated", target = "vaccinated"),
            @Mapping(source = "castrated", target = "castrated"),
            @Mapping(source = "wormed", target = "wormed"),
            @Mapping(source = "electronicChip", target = "electronicChip"),
            @Mapping(source = "illness", target = "illness"),
            @Mapping(source = "imageUrl", target = "imageUrl"),
            @Mapping(source = "notes", target = "notes"),
            @Mapping(source = "isAvailable", target = "isAvailable"),
    })
    Animal toAnimalUpdate(AnimalDTO animalDto, @MappingTarget Animal animal);


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "breed", target = "breed"),
            @Mapping(source = "arrivalDate", target = "arrivalDate"),
            @Mapping(source = "gender", target = "gender"),
            @Mapping(source = "age", target = "age"),
            @Mapping(source = "vaccinated", target = "vaccinated",defaultValue = "false"),
            @Mapping(source = "castrated", target = "castrated",defaultValue = "false"),
            @Mapping(source = "wormed", target = "wormed",defaultValue = "false"),
            @Mapping(source = "imageUrl", target = "imageUrl"),
            @Mapping(source = "electronicChip", target = "electronicChip"),
            @Mapping(source = "illness", target = "illness"),
            @Mapping(source = "notes", target = "notes"),
            @Mapping(source = "isAvailable", target = "isAvailable",defaultValue = "false")
    })
    Animal toAnimal(AnimalDTO animalDto);

}
