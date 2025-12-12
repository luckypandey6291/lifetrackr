package com.lifetrackr.repository.impl;

import com.lifetrackr.model.StudyTask;
import com.lifetrackr.model.TaskStatus;
import com.lifetrackr.repository.StudyTaskRepository;
import com.lifetrackr.util.JsonUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FileBasedStudyTaskRepository implements StudyTaskRepository {

    private final File file;
    private final Map<String, StudyTask> storage = new ConcurrentHashMap<>();

    public FileBasedStudyTaskRepository(String path) {
        this.file = new File(path);
        loadFromFile();
    }

    private synchronized void loadFromFile() {
        List<StudyTask> list = JsonUtil.readList(file, StudyTask.class);
        storage.clear();
        for (StudyTask t : list) storage.put(t.getId(), t);
    }

    private synchronized void persist() {
        List<StudyTask> list = new ArrayList<>(storage.values());
        JsonUtil.writeList(file, list);
    }

    @Override
    public StudyTask save(StudyTask task) {
        storage.put(task.getId(), task);
        persist();
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
        persist();
    }
}
