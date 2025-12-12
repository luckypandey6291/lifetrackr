package com.lifetrackr.repository.impl;

import com.lifetrackr.model.WorkoutEntry;
import com.lifetrackr.repository.WorkoutRepository;
import com.lifetrackr.util.JsonUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FileBasedWorkoutRepository implements WorkoutRepository {

    private final File file;
    private final Map<String, WorkoutEntry> storage = new ConcurrentHashMap<>();

    public FileBasedWorkoutRepository(String path) {
        this.file = new File(path);
        loadFromFile();
    }

    private synchronized void loadFromFile() {
        List<WorkoutEntry> list = JsonUtil.readList(file, WorkoutEntry.class);
        storage.clear();
        for (WorkoutEntry e : list) storage.put(e.getId(), e);
    }

    private synchronized void persist() {
        List<WorkoutEntry> list = new ArrayList<>(storage.values());
        JsonUtil.writeList(file, list);
    }

    @Override
    public WorkoutEntry save(WorkoutEntry entry) {
        storage.put(entry.getId(), entry);
        persist();
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
        persist();
    }
}
