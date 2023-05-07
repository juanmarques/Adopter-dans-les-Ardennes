package com.jme.adopterdla.adopterdla.animals.dto;

import com.jme.adopterdla.adopterdla.animals.enums.Gender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record AnimalDTO(

        Long id,
        String code,
        @NotBlank
        String name,
        String breed,
        String arrivalDate,
        String imageUrl,
        Gender gender,
        @Min(0)
        Integer age,
        boolean vaccinated,
        boolean castrated,
        boolean wormed,
        String electronicChip,
        String illness,
        String notes,
        boolean isAvailable,
        boolean hasBeenAdopted
) {

    @Builder
    public AnimalDTO {

    }
}
