package com.lifetrackr.service.impl;

import com.lifetrackr.model.StudyTask;
import com.lifetrackr.repository.StudyTaskRepository;
import com.lifetrackr.service.StudyTaskService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class StudyTaskServiceImpl implements StudyTaskService {

    private final StudyTaskRepository repository;

    public StudyTaskServiceImpl(StudyTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public StudyTask createTask(String userId, String subject, String topic, LocalDate dueDate, int priority) {
        StudyTask task = new StudyTask(userId, subject, topic, dueDate, priority);
        return repository.save(task);
    }

    @Override
    public List<StudyTask> getTasksForUser(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<StudyTask> getPendingTasksForUser(String userId) {
        return repository.findPendingByUserId(userId);
    }

    @Override
    public Optional<StudyTask> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
