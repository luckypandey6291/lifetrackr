package com.lifetrackr.service;

import com.lifetrackr.model.CodingSession;
import com.lifetrackr.model.Difficulty;

import java.util.List;
import java.util.Optional;

public interface CodingSessionService {

    CodingSession createSession(String userId, String problemName, String platform,
                                Difficulty difficulty, int durationMinutes, boolean solved);

    List<CodingSession> getSessionsForUser(String userId);

    Optional<CodingSession> getById(String id);

    void deleteById(String id);
}
