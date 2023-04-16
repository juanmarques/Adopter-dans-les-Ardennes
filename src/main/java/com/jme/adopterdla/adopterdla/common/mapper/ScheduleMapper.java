package com.jme.adopterdla.adopterdla.common.mapper;

import com.jme.adopterdla.adopterdla.common.ScheduleDTO;
import com.jme.adopterdla.adopterdla.common.entity.Schedule;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ScheduleMapper {
    @Mapping(target = "scheduleId", source = "id")
    @Mapping(target = "scheduleString", expression = "java(schedule.toString())")
    ScheduleDTO toDTO(Schedule schedule);

    @Mapping(target = "id", source = "scheduleId")
    Schedule toEntity(ScheduleDTO scheduleDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateScheduleFromDTO(ScheduleDTO scheduleDTO, @MappingTarget Schedule schedule);
}

