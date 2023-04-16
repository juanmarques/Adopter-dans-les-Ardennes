package com.jme.adopterdla.adopterdla.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.DayOfWeek;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("Schedule")
public class Schedule {

    @Id
    private Long id;
    private Set<DayOfWeek> days;
    private int startTimeHour;
    private int startTimeMinute;
    private int endTimeHour;
    private int endTimeMinute;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hours: ");
        for (DayOfWeek day : days) {
            sb.append(day.toString(), 0, 3).append(", ");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append("from ")
                .append(String.format("%02d:%02d", startTimeHour, startTimeMinute))
                .append(" to ")
                .append(String.format("%02d:%02d", endTimeHour, endTimeMinute));
        return sb.toString();
    }
}
