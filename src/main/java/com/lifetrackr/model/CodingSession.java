package com.lifetrackr.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class CodingSession {
    private final String id;
    private String userId;
    private String problemName;
    private String platform; 
    private Difficulty difficulty;
    private int durationMinutes;
    private boolean solved;
    private LocalDateTime createdAt;

    public CodingSession(String userId, String problemName, String platform,
                         Difficulty difficulty, int durationMinutes, boolean solved) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.problemName = problemName;
        this.platform = platform;
        this.difficulty = difficulty;
        this.durationMinutes = durationMinutes;
        this.solved = solved;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getProblemName() { return problemName; }
    public void setProblemName(String problemName) { this.problemName = problemName; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public boolean isSolved() { return solved; }
    public void setSolved(boolean solved) { this.solved = solved; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "CodingSession{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", problemName='" + problemName + '\'' +
                ", platform='" + platform + '\'' +
                ", difficulty=" + difficulty +
                ", durationMinutes=" + durationMinutes +
                ", solved=" + solved +
                ", createdAt=" + createdAt +
                '}';
    }
}
