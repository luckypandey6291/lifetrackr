package com.lifetrackr.repository.impl;

import com.lifetrackr.model.WorkoutEntry;
import com.lifetrackr.repository.WorkoutRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryWorkoutRepository implements WorkoutRepository {

    private final Map<String, WorkoutEntry> storage = new ConcurrentHashMap<>();

    @Override
    public WorkoutEntry save(WorkoutEntry entry) {
        storage.put(entry.getId(), entry);
        return entry;
    }

    @Override
    public Optional<WorkoutEntry> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<WorkoutEntry> findByUserId(String userId) {
        return storage.values().stream()
                .filter(e -> e.getUserId().equals(userId))
                .sorted(Comparator.comparing(WorkoutEntry::getDate))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }
}
