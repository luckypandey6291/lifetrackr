package com.lifetrackr.service;

import com.lifetrackr.model.CodingSession;
import com.lifetrackr.model.Difficulty;

import java.time.LocalDate;
import java.util.List;

public class CodingAnalyticsService {

    private final CodingSessionService codingService;

    public CodingAnalyticsService(CodingSessionService codingService) {
        this.codingService = codingService;
    }

    public int getTotalSessions(String userId) {
        return codingService.getSessionsForUser(userId).size();
    }

    public int getSolvedCount(String userId) {
        return (int) codingService.getSessionsForUser(userId)
                .stream()
                .filter(CodingSession::isSolved)
                .count();
    }

    public int getUnsolvedCount(String userId) {
        return (int) codingService.getSessionsForUser(userId)
                .stream()
                .filter(s -> !s.isSolved())
                .count();
    }

    public int getTotalMinutes(String userId) {
        return codingService.getSessionsForUser(userId)
                .stream()
                .mapToInt(CodingSession::getDurationMinutes)
                .sum();
    }

    public long getEasyCount(String userId) {
        return countByDifficulty(userId, Difficulty.EASY);
    }

    public long getMediumCount(String userId) {
        return countByDifficulty(userId, Difficulty.MEDIUM);
    }

    public long getHardCount(String userId) {
        return countByDifficulty(userId, Difficulty.HARD);
    }

    private long countByDifficulty(String userId, Difficulty diff) {
        return codingService.getSessionsForUser(userId)
                .stream()
                .filter(s -> s.getDifficulty() == diff)
                .count();
    }

    public int getTodayMinutes(String userId) {
        LocalDate today = LocalDate.now();
        return codingService.getSessionsForUser(userId)
                .stream()
                .filter(s -> s.getCreatedAt().toLocalDate().equals(today))
                .mapToInt(CodingSession::getDurationMinutes)
                .sum();
    }

    // Streak calculation: continuous days with coding activity
    public int getCodingStreak(String userId) {
        List<CodingSession> sessions = codingService.getSessionsForUser(userId);
        if (sessions.isEmpty()) return 0;

        LocalDate today = LocalDate.now();
        int streak = 0;

        for (int i = 0; ; i++) {
            LocalDate checkDay = today.minusDays(i);
            boolean hasSession = sessions.stream()
                    .anyMatch(s -> s.getCreatedAt().toLocalDate().equals(checkDay));

            if (hasSession) streak++;
            else break;
        }
        return streak;
    }
}
