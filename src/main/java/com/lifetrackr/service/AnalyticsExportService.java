package com.lifetrackr.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsExportService {

    private final StudyAnalyticsService study;
    private final CodingAnalyticsService coding;
    private final WorkoutAnalyticsService workout;
    private final ProductivityScoreService score;
    private final String userId;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public AnalyticsExportService(
            StudyAnalyticsService study,
            CodingAnalyticsService coding,
            WorkoutAnalyticsService workout,
            ProductivityScoreService score,
            String userId
    ) {
        this.study = study;
        this.coding = coding;
        this.workout = workout;
        this.score = score;
        this.userId = userId;
    }

    public Map<String, Object> generateAnalytics() {

        Map<String, Object> root = new HashMap<>();

        Map<String, Object> studyMap = new HashMap<>();
        studyMap.put("totalTasks", study.getTotalTasks(userId));
        studyMap.put("pendingTasks", study.getPendingTasks(userId));
        studyMap.put("completedTasks", study.getCompletedTasks(userId));
        studyMap.put("overdueTasks", study.getOverdueTasks(userId));
        studyMap.put("highPriority", study.getHighPriorityCount(userId));

        Map<String, Object> codingMap = new HashMap<>();
        codingMap.put("totalSessions", coding.getTotalSessions(userId));
        codingMap.put("solved", coding.getSolvedCount(userId));
        codingMap.put("unsolved", coding.getUnsolvedCount(userId));
        codingMap.put("todayMinutes", coding.getTodayMinutes(userId));
        codingMap.put("easy", coding.getEasyCount(userId));
        codingMap.put("medium", coding.getMediumCount(userId));
        codingMap.put("hard", coding.getHardCount(userId));
        codingMap.put("streak", coding.getCodingStreak(userId));

        Map<String, Object> workoutMap = new HashMap<>();
        workoutMap.put("totalWorkouts", workout.getTotalWorkouts(userId));
        workoutMap.put("totalMinutes", workout.getTotalMinutes(userId));
        workoutMap.put("weeklyMinutes", workout.getWeeklyMinutes(userId));
        workoutMap.put("todayMinutes", workout.getTodayMinutes(userId));
        workoutMap.put("streak", workout.getWorkoutStreak(userId));

        Map<String, Object> scoreMap = new HashMap<>();
        scoreMap.put("studyScore", score.getStudyScore());
        scoreMap.put("codingScore", score.getCodingScore());
        scoreMap.put("workoutScore", score.getWorkoutScore());
        scoreMap.put("streakBonus", score.getStreakBonus());
        scoreMap.put("totalScore", score.getTotalScore());

        root.put("userId", userId);
        root.put("study", studyMap);
        root.put("coding", codingMap);
        root.put("workout", workoutMap);
        root.put("productivityScore", scoreMap);

        return root;
    }

    public void exportToJson(String path) {
        try (FileWriter w = new FileWriter(path)) {
            gson.toJson(generateAnalytics(), w);
        } catch (Exception e) {
            throw new RuntimeException("Failed to export analytics JSON: " + e.getMessage());
        }
    }
}
