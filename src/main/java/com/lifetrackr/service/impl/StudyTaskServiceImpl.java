package com.lifetrackr.service.impl;

import org.springframework.stereotype.Service;

import com.lifetrackr.model.StudyTask;
import com.lifetrackr.model.TaskStatus;
import com.lifetrackr.repository.StudyTaskRepository;
import com.lifetrackr.service.StudyTaskService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
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

    /* ================= NEW CODE START ================= */

    // ✅ UPDATE TASK
    @Override
    public StudyTask updateTask(
            String id,
            String subject,
            String topic,
            LocalDate dueDate,
            int priority
    ) {
        StudyTask task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setSubject(subject);
        task.setTopic(topic);
        task.setDueDate(dueDate);
        task.setPriority(priority);

        return repository.save(task);
    }

    // ✅ TOGGLE STATUS (PENDING ↔ DONE)
   @Override
public StudyTask toggleStatus(String id) {
    StudyTask task = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));

    if (task.getStatus() == TaskStatus.PENDING) {
        task.setStatus(TaskStatus.COMPLETED);
    } else {
        task.setStatus(TaskStatus.PENDING);
    }

    return repository.save(task);
}

    /* ================= NEW CODE END ================= */

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
