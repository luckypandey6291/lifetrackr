package com.lifetrackr.repository;

import com.lifetrackr.model.CodingSession;

import java.util.List;
import java.util.Optional;

public interface CodingSessionRepository {
    CodingSession save(CodingSession session);
    Optional<CodingSession> findById(String id);
    List<CodingSession> findByUserId(String userId);
    void deleteById(String id);
}
