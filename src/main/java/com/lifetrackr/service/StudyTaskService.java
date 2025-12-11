package com.lifetrackr.service;

import com.lifetrackr.model.StudyTask;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyTaskService {
    StudyTask createTask(String userId, String subject, String topic, LocalDate dueDate, int priority);
    List<StudyTask> getTasksForUser(String userId);
    List<StudyTask> getPendingTasksForUser(String userId);
    Optional<StudyTask> getById(String id);
    void deleteById(String id);
}
