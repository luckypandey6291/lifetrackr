package com.lifetrackr.dto;

import com.lifetrackr.model.WorkoutType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateWorkoutRequest {

    @NotBlank
    private String userId;

    @NotNull
    private WorkoutType type;

    @NotBlank
    private String description;

    @NotNull
    private Integer durationMinutes;

    @NotBlank
    private String date; // yyyy-MM-dd

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public WorkoutType getType() { return type; }
    public void setType(WorkoutType type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
