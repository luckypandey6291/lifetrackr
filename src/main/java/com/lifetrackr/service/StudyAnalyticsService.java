package com.lifetrackr.service;

public class StudyAnalyticsService {

    private final StudyTaskService studyTaskService;

    public StudyAnalyticsService(StudyTaskService studyTaskService) {
        this.studyTaskService = studyTaskService;
    }

    public int getTotalTasks(String userId) {
        return studyTaskService.getTasksForUser(userId).size();
    }

    public int getPendingTasks(String userId) {
        return studyTaskService.getPendingTasksForUser(userId).size();
    }

    public int getCompletedTasks(String userId) {
        return (int) studyTaskService.getTasksForUser(userId)
                .stream()
                .filter(t -> t.getStatus().toString().equals("COMPLETED"))
                .count();
    }

    public int getOverdueTasks(String userId) {
        return (int) studyTaskService.getTasksForUser(userId)
                .stream()
                .filter(t -> t.getDueDate().isBefore(java.time.LocalDate.now()))
                .count();
    }

    public long getHighPriorityCount(String userId) {
        return studyTaskService.getTasksForUser(userId)
                .stream()
                .filter(t -> t.getPriority() == 1)
                .count();
    }
}
