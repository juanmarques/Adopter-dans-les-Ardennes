package com.jme.adopterdla.adopterdla.volunteers.mapper;

import com.jme.adopterdla.adopterdla.common.entity.Schedule;
import com.jme.adopterdla.adopterdla.volunteers.dto.VolunteerDTO;
import com.jme.adopterdla.adopterdla.volunteers.entity.Volunteer;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VolunteerMapper {
    @Mapping(target = "id", source = "volunteer.id")
    @Mapping(target = "scheduleString", source = "schedule", qualifiedByName = "scheduleToString")
    VolunteerDTO toDTO(Volunteer volunteer, Schedule schedule);

    Volunteer toEntity(VolunteerDTO volunteerDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdoptionProcessFromDTO(VolunteerDTO volunteerDTO, @MappingTarget Volunteer volunteer);

    @Named("scheduleToString")
    default String scheduleToString(Schedule schedule) {
        return schedule == null ? null : schedule.toString();
    }
}



