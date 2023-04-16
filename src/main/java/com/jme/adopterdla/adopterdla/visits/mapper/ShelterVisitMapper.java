package com.jme.adopterdla.adopterdla.visits.mapper;

import com.jme.adopterdla.adopterdla.adopters.mapper.AdopterMapper;
import com.jme.adopterdla.adopterdla.animals.mapper.AnimalMapper;
import com.jme.adopterdla.adopterdla.common.mapper.ScheduleMapper;
import com.jme.adopterdla.adopterdla.visits.dto.ShelterVisitDTO;
import com.jme.adopterdla.adopterdla.visits.entity.ShelterVisit;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ScheduleMapper.class, AnimalMapper.class, AdopterMapper.class}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShelterVisitMapper {

    @Mapping(target = "scheduleId", source = "schedule.scheduleId")
    @Mapping(target = "animalId", source = "animal.id")
    @Mapping(target = "adopterId", source = "adopter.id")
    ShelterVisit toEntity(ShelterVisitDTO shelterVisitDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateShelterVisitFromDTO(ShelterVisitDTO shelterVisitDTO, @MappingTarget ShelterVisit shelterVisit);
}
