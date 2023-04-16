package com.jme.adopterdla.adopterdla.visits.dto;

import com.jme.adopterdla.adopterdla.adopters.dto.AdopterDTO;
import com.jme.adopterdla.adopterdla.animals.dto.AnimalDTO;
import com.jme.adopterdla.adopterdla.common.ScheduleDTO;

public record ShelterVisitDTO(
        Long id,
        ScheduleDTO schedule,
        AnimalDTO animal,
        AdopterDTO adopter
) {
}
