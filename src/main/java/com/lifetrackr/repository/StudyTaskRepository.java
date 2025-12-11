package com.lifetrackr.repository;

import com.lifetrackr.model.StudyTask;

import java.util.List;
import java.util.Optional;

public interface StudyTaskRepository {
    StudyTask save(StudyTask task);
    Optional<StudyTask> findById(String id);
    List<StudyTask> findByUserId(String userId);
    List<StudyTask> findPendingByUserId(String userId);
    void deleteById(String id);
}
