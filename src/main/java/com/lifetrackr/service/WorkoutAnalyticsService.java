package com.lifetrackr.service;

import com.lifetrackr.model.WorkoutEntry;
import com.lifetrackr.model.WorkoutType;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkoutAnalyticsService {

    private final WorkoutService workoutService;

    public WorkoutAnalyticsService(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    public int getTotalWorkouts(String userId) {
        return workoutService.getWorkoutsForUser(userId).size();
    }

    public int getTotalMinutes(String userId) {
        return workoutService.getWorkoutsForUser(userId)
                .stream()
                .mapToInt(WorkoutEntry::getDurationMinutes)
                .sum();
    }

    public int getTodayMinutes(String userId) {
        LocalDate today = LocalDate.now();
        return workoutService.getWorkoutsForUser(userId)
                .stream()
                .filter(w -> w.getDate().equals(today))
                .mapToInt(WorkoutEntry::getDurationMinutes)
                .sum();
    }

    public int getMinutesByType(String userId, WorkoutType type) {
        return workoutService.getWorkoutsForUser(userId)
                .stream()
                .filter(w -> w.getType() == type)
                .mapToInt(WorkoutEntry::getDurationMinutes)
                .sum();
    }

    public int getWeeklyMinutes(String userId) {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(6);

        return workoutService.getWorkoutsForUser(userId)
                .stream()
                .filter(w -> {
                    LocalDate d = w.getDate();
                    return !d.isBefore(weekAgo) && !d.isAfter(today);
                })
                .mapToInt(WorkoutEntry::getDurationMinutes)
                .sum();
    }

    // Continuous days with workout activity
    public int getWorkoutStreak(String userId) {
        List<WorkoutEntry> workouts = workoutService.getWorkoutsForUser(userId);
        if (workouts.isEmpty()) return 0;

        LocalDate today = LocalDate.now();
        int streak = 0;

        for (int i = 0; ; i++) {
            LocalDate checkDay = today.minusDays(i);
            boolean hasWorkout = workouts.stream()
                    .anyMatch(w -> w.getDate().equals(checkDay));

            if (hasWorkout) streak++;
            else break;
        }
        return streak;
    }

    public String getBestWorkoutDay(String userId) {
        return workoutService.getWorkoutsForUser(userId)
                .stream()
                .collect(Collectors.groupingBy(
                        w -> w.getDate(),
                        Collectors.summingInt(WorkoutEntry::getDurationMinutes)
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey() + " (" + e.getValue() + " mins)")
                .orElse("No workouts yet");
    }
}
