package com.lifetrackr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateStudyTaskRequest {

    @NotBlank
    private String subject;

    @NotBlank
    private String topic;

    @NotBlank
    private String dueDate;

    @NotNull
    private Integer priority;

    // 🔥 GETTERS
    public String getSubject() {
        return subject;
    }

    public String getTopic() {
        return topic;
    }

    public String getDueDate() {
        return dueDate;
    }

    public Integer getPriority() {
        return priority;
    }

    //  SETTERS
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}

