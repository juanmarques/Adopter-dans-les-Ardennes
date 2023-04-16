package com.jme.adopterdla.adopterdla.volunteers.dto;

import java.time.DayOfWeek;
import java.util.Set;

public record VolunteerDTO(
        Long id,
        String notes,
        Long scheduleId,
        Set<DayOfWeek> days,
        int startTimeHour,
        int startTimeMinute,
        int endTimeHour,
        int endTimeMinute,
        String scheduleString) {
}

