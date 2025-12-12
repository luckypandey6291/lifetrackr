package com.lifetrackr.repository;

import com.lifetrackr.model.WorkoutEntry;

import java.util.List;
import java.util.Optional;

public interface WorkoutRepository {
    WorkoutEntry save(WorkoutEntry entry);
    Optional<WorkoutEntry> findById(String id);
    List<WorkoutEntry> findByUserId(String userId);
    void deleteById(String id);
}
