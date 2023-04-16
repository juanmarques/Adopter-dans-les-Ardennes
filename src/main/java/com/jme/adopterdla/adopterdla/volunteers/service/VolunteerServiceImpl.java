package com.jme.adopterdla.adopterdla.volunteers.service;

import com.jme.adopterdla.adopterdla.common.entity.Schedule;
import com.jme.adopterdla.adopterdla.common.entity.repository.ScheduleRepository;
import com.jme.adopterdla.adopterdla.volunteers.dto.VolunteerDTO;
import com.jme.adopterdla.adopterdla.volunteers.entity.Volunteer;
import com.jme.adopterdla.adopterdla.volunteers.mapper.VolunteerMapper;
import com.jme.adopterdla.adopterdla.volunteers.repository.VolunteerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service implementation for managing volunteers.
 */
@Service
@AllArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final ScheduleRepository scheduleRepository;
    private final VolunteerMapper volunteerMapper;

    /**
     * Finds a volunteer by their id and returns the corresponding VolunteerDTO.
     *
     * @param id the volunteer's id
     * @return the corresponding VolunteerDTO
     */
    @Override
    public Mono<VolunteerDTO> findById(Long id) {
        return volunteerRepository.findById(id)
                .flatMap(volunteer -> scheduleRepository.findById(volunteer.getScheduleId())
                        .map(schedule -> volunteerMapper.toDTO(volunteer, schedule)));
    }

    /**
     * Returns all volunteers as a Flux of VolunteerDTOs.
     *
     * @return all volunteers as a Flux of VolunteerDTOs
     */
    @Override
    public Flux<VolunteerDTO> findAll() {
        return volunteerRepository.findAll()
                .flatMap(volunteer -> scheduleRepository.findById(volunteer.getScheduleId())
                        .map(schedule -> volunteerMapper.toDTO(volunteer, schedule)));
    }

    /**
     * Saves a volunteer based on the given VolunteerDTO and returns the saved VolunteerDTO.
     * Also handles updating or inserting the associated Schedule.
     *
     * @param volunteerDTO the VolunteerDTO to be saved
     * @return the saved VolunteerDTO
     */
    @Override
    public Mono<VolunteerDTO> save(VolunteerDTO volunteerDTO) {
        Schedule schedule = new Schedule(0L, volunteerDTO.days(),
                volunteerDTO.startTimeHour(), volunteerDTO.startTimeMinute(),
                volunteerDTO.endTimeHour(), volunteerDTO.endTimeMinute());

        // Determine if we need to save a new schedule or update an existing one
        Mono<Schedule> scheduleMono;
        if (volunteerDTO.scheduleId() == null) {
            scheduleMono = scheduleRepository.save(schedule);
        } else {
            scheduleMono = scheduleRepository.findById(volunteerDTO.scheduleId())
                    .doOnNext(existingSchedule -> {
                        existingSchedule.setDays(schedule.getDays());
                        existingSchedule.setStartTimeHour(schedule.getStartTimeHour());
                        existingSchedule.setStartTimeMinute(schedule.getStartTimeMinute());
                        existingSchedule.setEndTimeHour(schedule.getEndTimeHour());
                        existingSchedule.setEndTimeMinute(schedule.getEndTimeMinute());
                    })
                    .flatMap(scheduleRepository::save);
        }

        return scheduleMono.flatMap(savedSchedule -> {
            Volunteer volunteer = volunteerMapper.toEntity(volunteerDTO);
            volunteer.setScheduleId(savedSchedule.getId());

            // Determine if we need to save a new volunteer or update an existing one
            if (volunteerDTO.id() == null) {
                return volunteerRepository.save(volunteer);
            } else {
                return volunteerRepository.findById(volunteerDTO.id())
                        .doOnNext(existingVolunteer -> volunteerMapper.updateAdoptionProcessFromDTO(volunteerDTO, existingVolunteer))
                        .flatMap(volunteerRepository::save);
            }
        }).flatMap(savedVolunteer -> scheduleRepository.findById(savedVolunteer.getScheduleId())
                .map(savedSchedule -> volunteerMapper.toDTO(savedVolunteer, savedSchedule)));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return volunteerRepository.deleteById(id);
    }
}