package com.lifetrackr.repository.impl;

import com.lifetrackr.model.CodingSession;
import com.lifetrackr.repository.CodingSessionRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryCodingSessionRepository implements CodingSessionRepository {

    private final Map<String, CodingSession> storage = new ConcurrentHashMap<>();

    @Override
    public CodingSession save(CodingSession session) {
        storage.put(session.getId(), session);
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
    }
}
