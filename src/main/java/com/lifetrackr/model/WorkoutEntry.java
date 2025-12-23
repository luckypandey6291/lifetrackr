package com.lifetrackr.model;

import java.time.LocalDate;
import java.util.UUID;

public class WorkoutEntry {

    private String id;   // ❗ removed final
    private String userId;
    private WorkoutType type;
    private String description; // e.g., "Chest + Triceps"
    private int durationMinutes;
    private LocalDate date;

    public WorkoutEntry(String userId, WorkoutType type, String description,
                        int durationMinutes, LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.type = type;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.date = date;
    }

    // ===== getters & setters =====

    public String getId() { return id; }

    // 🔥 REQUIRED for SQLite / DB hydration
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public WorkoutType getType() { return type; }
    public void setType(WorkoutType type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    @Override
    public String toString() {
        return "WorkoutEntry{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", date=" + date +
                '}';
    }
}
