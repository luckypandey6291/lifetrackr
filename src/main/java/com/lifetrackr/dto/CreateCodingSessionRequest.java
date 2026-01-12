package com.lifetrackr.dto;

import com.lifetrackr.model.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCodingSessionRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String problemName;

    @NotBlank
    private String platform;

    @NotNull
    private Difficulty difficulty;

    @NotNull
    private Integer durationMinutes;

    @NotNull
    private Boolean solved;

    // getters/setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getProblemName() { return problemName; }
    public void setProblemName(String problemName) { this.problemName = problemName; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Boolean getSolved() { return solved; }
    public void setSolved(Boolean solved) { this.solved = solved; }
}

