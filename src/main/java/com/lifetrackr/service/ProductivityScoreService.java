package com.lifetrackr.service;

public class ProductivityScoreService {

    private final StudyAnalyticsService studyAnalytics;
    private final CodingAnalyticsService codingAnalytics;
    private final WorkoutAnalyticsService workoutAnalytics;

    private final String userId;

    public ProductivityScoreService(
            StudyAnalyticsService studyAnalytics,
            CodingAnalyticsService codingAnalytics,
            WorkoutAnalyticsService workoutAnalytics,
            String userId
    ) {
        this.studyAnalytics = studyAnalytics;
        this.codingAnalytics = codingAnalytics;
        this.workoutAnalytics = workoutAnalytics;
        this.userId = userId;
    }

    public int getStudyScore() {
        int total = studyAnalytics.getTotalTasks(userId);
        int completed = studyAnalytics.getCompletedTasks(userId);

        if (total == 0) return 0;

        double ratio = (double) completed / total;
        return (int) (ratio * 40); // Study = 40%
    }

    public int getCodingScore() {
        int total = codingAnalytics.getTotalSessions(userId);
        int solved = codingAnalytics.getSolvedCount(userId);

        if (total == 0) return 0;

        double accuracy = (double) solved / total;
        int minutes = codingAnalytics.getTodayMinutes(userId);

        int accuracyPart = (int) (accuracy * 25); // max 25
        int minutesPart = Math.min(minutes / 5, 15); // max 15 for 75 min+

        return accuracyPart + minutesPart; // Coding = 40%
    }

    public int getWorkoutScore() {
        int minutes = workoutAnalytics.getTodayMinutes(userId);
        int score = Math.min(minutes / 5, 20); // Every 5 min = 1 point, max 20
        return score;
    }

    public int getStreakBonus() {
        int codingStreak = codingAnalytics.getCodingStreak(userId);
        int workoutStreak = workoutAnalytics.getWorkoutStreak(userId);

        int combined = Math.max(codingStreak, workoutStreak);

        return Math.min(combined * 2, 10); // Max +10 bonus
    }

    public int getTotalScore() {
        int study = getStudyScore();
        int coding = getCodingScore();
        int workout = getWorkoutScore();
        int streak = getStreakBonus();

        return Math.min(study + coding + workout + streak, 100);
    }
}
