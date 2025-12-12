package com.lifetrackr.service;

import com.lifetrackr.model.WorkoutEntry;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutService {
    WorkoutEntry createWorkout(String userId, String description, String type, int durationMinutes, LocalDate date);
    List<WorkoutEntry> getWorkoutsForUser(String userId);
    Optional<WorkoutEntry> getById(String id);
    void deleteById(String id);
}
