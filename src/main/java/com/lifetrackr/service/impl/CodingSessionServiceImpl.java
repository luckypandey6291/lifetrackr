package com.lifetrackr.service.impl;

import com.lifetrackr.model.CodingSession;
import com.lifetrackr.model.Difficulty;
import com.lifetrackr.repository.CodingSessionRepository;
import com.lifetrackr.service.CodingSessionService;

import java.util.List;
import java.util.Optional;

public class CodingSessionServiceImpl implements CodingSessionService {

    private final CodingSessionRepository repository;

    public CodingSessionServiceImpl(CodingSessionRepository repository) {
        this.repository = repository;
    }

    @Override
    public CodingSession createSession(String userId, String problemName, String platform,
                                       Difficulty difficulty, int durationMinutes, boolean solved) {

        CodingSession session = new CodingSession(
                userId,
                problemName,
                platform,
                difficulty,
                durationMinutes,
                solved
        );

        return repository.save(session);
    }

    @Override
    public List<CodingSession> getSessionsForUser(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public Optional<CodingSession> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
