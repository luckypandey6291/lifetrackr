package com.lifetrackr.repository.impl;

import com.lifetrackr.model.CodingSession;
import com.lifetrackr.repository.CodingSessionRepository;
import com.lifetrackr.util.JsonUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FileBasedCodingSessionRepository implements CodingSessionRepository {

    private final File file;
    private final Map<String, CodingSession> storage = new ConcurrentHashMap<>();

    public FileBasedCodingSessionRepository(String path) {
        this.file = new File(path);
        loadFromFile();
    }

    private synchronized void loadFromFile() {
        List<CodingSession> list = JsonUtil.readList(file, CodingSession.class);
        storage.clear();
        for (CodingSession s : list) storage.put(s.getId(), s);
    }

    private synchronized void persist() {
        List<CodingSession> list = new ArrayList<>(storage.values());
        JsonUtil.writeList(file, list);
    }

    @Override
    public CodingSession save(CodingSession session) {
        storage.put(session.getId(), session);
        persist();
        return session;
    }

    @Override
    public Optional<CodingSession> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<CodingSession> findByUserId(String userId) {
        return storage.values().stream()
                .filter(s -> s.getUserId().equals(userId))
                .sorted(Comparator.comparing(CodingSession::getCreatedAt))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
        persist();
    }
}
