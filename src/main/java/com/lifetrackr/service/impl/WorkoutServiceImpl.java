package com.lifetrackr.service.impl;

import com.lifetrackr.model.WorkoutEntry;
import com.lifetrackr.model.WorkoutType;
import com.lifetrackr.repository.WorkoutRepository;
import com.lifetrackr.service.WorkoutService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository repository;

    public WorkoutServiceImpl(WorkoutRepository repository) {
        this.repository = repository;
    }

    @Override
    public WorkoutEntry createWorkout(String userId, String description, String type, int durationMinutes, LocalDate date) {
        WorkoutType wt;
        try {
            wt = WorkoutType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            wt = WorkoutType.FLEXIBILITY; // default fallback
        }
        WorkoutEntry entry = new WorkoutEntry(userId, wt, description, durationMinutes, date);
        return repository.save(entry);
    }

    @Override
    public List<WorkoutEntry> getWorkoutsForUser(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public Optional<WorkoutEntry> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
