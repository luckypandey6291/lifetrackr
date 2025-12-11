package com.lifetrackr.repository.impl;

import com.lifetrackr.model.StudyTask;
import com.lifetrackr.model.TaskStatus;
import com.lifetrackr.repository.StudyTaskRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryStudyTaskRepository implements StudyTaskRepository {

    private final Map<String, StudyTask> storage = new ConcurrentHashMap<>();

    @Override
    public StudyTask save(StudyTask task) {
        storage.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<StudyTask> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<StudyTask> findByUserId(String userId) {
        return storage.values().stream()
                .filter(t -> t.getUserId().equals(userId))
                .sorted(Comparator.comparing(StudyTask::getDueDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudyTask> findPendingByUserId(String userId) {
        return storage.values().stream()
                .filter(t -> t.getUserId().equals(userId) && t.getStatus() != TaskStatus.COMPLETED)
                .sorted(Comparator.comparing(StudyTask::getDueDate))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }
}
