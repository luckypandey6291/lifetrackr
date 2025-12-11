package com.lifetrackr.model;

import java.time.LocalDate;
import java.util.UUID;

public class StudyTask {
    private final String id;
    private String userId;
    private String subject;
    private String topic;
    private LocalDate dueDate;
    private TaskStatus status;
    private int priority; // 1 = High, 2 = Medium, 3 = Low

    public StudyTask(String userId, String subject, String topic, LocalDate dueDate, int priority) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.subject = subject;
        this.topic = topic;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = TaskStatus.PENDING;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    @Override
    public String toString() {
        return "StudyTask{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", subject='" + subject + '\'' +
                ", topic='" + topic + '\'' +
                ", dueDate=" + dueDate +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }
}
