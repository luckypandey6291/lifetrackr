package com.lifetrackr;
import com.lifetrackr.repository.WorkoutRepository;
import com.lifetrackr.repository.impl.SQLiteWorkoutRepository;

import com.lifetrackr.repository.CodingSessionRepository;
import com.lifetrackr.repository.impl.SQLiteCodingSessionRepository;
import com.lifetrackr.repository.StudyTaskRepository;
import com.lifetrackr.repository.impl.SQLiteStudyTaskRepository;

import com.lifetrackr.service.AnalyticsExportService;
import com.lifetrackr.service.StudyAnalyticsService;
import com.lifetrackr.service.CodingAnalyticsService;
import com.lifetrackr.service.WorkoutAnalyticsService;
import com.lifetrackr.service.ProductivityScoreService;




import com.lifetrackr.model.*;
import com.lifetrackr.service.StudyTaskService;
import com.lifetrackr.service.CodingSessionService;
import com.lifetrackr.service.WorkoutService;
import com.lifetrackr.service.impl.StudyTaskServiceImpl;
import com.lifetrackr.service.impl.CodingSessionServiceImpl;
import com.lifetrackr.service.impl.WorkoutServiceImpl;
import com.lifetrackr.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== LifeTrackr (File-backed) ===");

        // --- data file paths ---
        String base = "data";
        String userFilePath = base + "/user.json";
        String studyFile = base + "/study_tasks.json";
        String codingFile = base + "/coding_sessions.json";
        String workoutFile = base + "/workouts.json";

        // Ensure data directory exists
        new File(base).mkdirs();

        // --- load or create demo user and persist to data/user.json ---
        User demo = null;
        File userFile = new File(userFilePath);
        try {
            if (userFile.exists()) {
                // try load existing user
                try (FileReader fr = new FileReader(userFile)) {
                    demo = JsonUtil.gson().fromJson(fr, User.class);
                } catch (Exception e) {
                    System.out.println("⚠ Warning: failed to parse user.json -> " + e.getMessage());
                    demo = null;
                }

                if (demo != null) {
                    System.out.println("Loaded demo user from file: " + demo);
                } else {
                    System.out.println("user.json existed but contained no valid user — recreating.");
                    demo = new User("Lucky", "lucky@example.com");
                    try (FileWriter fw = new FileWriter(userFile)) {
                        JsonUtil.gson().toJson(demo, fw);
                    }
                    System.out.println("Created and saved demo user: " + demo);
                }
            } else {
                // create and save demo user
                demo = new User("Lucky", "lucky@example.com");
                try (FileWriter fw = new FileWriter(userFile)) {
                    JsonUtil.gson().toJson(demo, fw);
                }
                System.out.println("Created and saved demo user: " + demo);
            }
        } catch (Exception e) {
            System.out.println("⚠ Warning: could not read/write user file: " + e.getMessage());
            // fallback to in-memory demo user
            demo = new User("Lucky", "lucky@example.com");
            System.out.println("Using temporary demo user: " + demo);
        }

        String userId = demo.getId();

        // --- repositories (file-backed) ---


        StudyTaskRepository taskRepo = new SQLiteStudyTaskRepository();
        CodingSessionRepository codingRepo = new SQLiteCodingSessionRepository();

        WorkoutRepository workoutRepo = new SQLiteWorkoutRepository();


        StudyTaskService taskService = new StudyTaskServiceImpl(taskRepo);
        CodingSessionService codingService = new CodingSessionServiceImpl(codingRepo);
        WorkoutService workoutService = new WorkoutServiceImpl(workoutRepo);

        StudyAnalyticsService studyAnalytics = new StudyAnalyticsService(taskService);
        CodingAnalyticsService codingAnalytics = new CodingAnalyticsService(codingService);
        WorkoutAnalyticsService workoutAnalytics = new WorkoutAnalyticsService(workoutService);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\\n=== MENU ===");
            System.out.println("1. Add study task");
            System.out.println("2. List study tasks");
            System.out.println("3. List pending study tasks");
            System.out.println("4. Delete study task");
            System.out.println("5. Show study task by ID");

            System.out.println("----- Coding Sessions -----");
            System.out.println("6. Add coding session");
            System.out.println("7. List coding sessions");
            System.out.println("8. Delete coding session");
            System.out.println("9. Show coding session by ID");

            System.out.println("----- Workouts -----");
            System.out.println("10. Add workout");
            System.out.println("11. List workouts");
            System.out.println("12. Delete workout");
            System.out.println("13. Show workout by ID");

            System.out.println("------Analytics------");
            System.out.println("14. Analytics Dashboard");

            System.out.println("-------Overall score----");
            System.out.println("15. Productivity score");

            System.out.println("16. Export Analytics (JSON)");


            System.out.println("0. Exit");

            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Subject: ");
                        String subject = sc.nextLine().trim();
                        System.out.print("Topic: ");
                        String topic = sc.nextLine().trim();
                        System.out.print("Due date (YYYY-MM-DD): ");
                        LocalDate due = LocalDate.parse(sc.nextLine().trim());
                        System.out.print("Priority (1=High, 2=Moderate, 3=Low): ");
                        int priority = Integer.parseInt(sc.nextLine().trim());
                        StudyTask task = taskService.createTask(userId, subject, topic, due, priority);
                        System.out.println("Created: " + task);
                    }

                    case "2" -> {
                        List<StudyTask> all = taskService.getTasksForUser(userId);
                        System.out.println("--- All Study Tasks (" + all.size() + ") ---");
                        all.forEach(System.out::println);
                    }

                    case "3" -> {
                        List<StudyTask> pending = taskService.getPendingTasksForUser(userId);
                        System.out.println("--- Pending Tasks (" + pending.size() + ") ---");
                        pending.forEach(System.out::println);
                    }

                    case "4" -> {
                        System.out.print("Enter task ID to delete: ");
                        String id = sc.nextLine().trim();
                        taskService.deleteById(id);
                        System.out.println("Deleted task: " + id);
                    }

                    case "5" -> {
                        System.out.print("Enter Task ID: ");
                        String id = sc.nextLine().trim();
                        Optional<StudyTask> t = taskService.getById(id);
                        t.ifPresentOrElse(
                                System.out::println,
                                () -> System.out.println("Task not found!")
                        );
                    }

                    // Coding Session menu
                    case "6" -> {
                        System.out.print("Problem Name: ");
                        String prob = sc.nextLine().trim();

                        System.out.print("Platform (LeetCode/Codeforces/etc): ");
                        String platform = sc.nextLine().trim();

                        System.out.print("Difficulty (EASY/MEDIUM/HARD): ");
                        Difficulty diff = Difficulty.valueOf(sc.nextLine().trim().toUpperCase());

                        System.out.print("Duration (minutes): ");
                        int duration = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Solved? (true/false): ");
                        boolean solved = Boolean.parseBoolean(sc.nextLine().trim());

                        CodingSession session = codingService.createSession(
                                userId, prob, platform, diff, duration, solved
                        );

                        System.out.println("Session added: " + session);
                    }

                    case "7" -> {
                        List<CodingSession> sessions = codingService.getSessionsForUser(userId);
                        System.out.println("--- Coding Sessions (" + sessions.size() + ") ---");
                        sessions.forEach(System.out::println);
                    }

                    case "8" -> {
                        System.out.print("Enter Coding Session ID to delete: ");
                        String id = sc.nextLine().trim();
                        codingService.deleteById(id);
                        System.out.println("Deleted coding session: " + id);
                    }

                    case "9" -> {
                        System.out.print("Enter Coding Session ID: ");
                        String id = sc.nextLine().trim();
                        Optional<CodingSession> s = codingService.getById(id);
                        s.ifPresentOrElse(
                                System.out::println,
                                () -> System.out.println("Coding session not found!")
                        );
                    }

                    // Workouts
                    case "10" -> {
                        System.out.print("Workout type (CARDIO/STRENGTH/FLEXIBILITY/SPORTS): ");
                        String type = sc.nextLine().trim();
                        System.out.print("Description: ");
                        String desc = sc.nextLine().trim();
                        System.out.print("Duration minutes: ");
                        int mins = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Date (YYYY-MM-DD): ");
                        LocalDate date = LocalDate.parse(sc.nextLine().trim());

                        WorkoutEntry entry = workoutService.createWorkout(userId, desc, type, mins, date);
                        System.out.println("Workout added: " + entry);
                    }

                    case "11" -> {
                        List<WorkoutEntry> workouts = workoutService.getWorkoutsForUser(userId);
                        System.out.println("--- Workouts (" + workouts.size() + ") ---");
                        workouts.forEach(System.out::println);
                    }

                    case "12" -> {
                        System.out.print("Enter Workout ID to delete: ");
                        String id = sc.nextLine().trim();
                        workoutService.deleteById(id);
                        System.out.println("Deleted workout: " + id);
                    }

                    case "13" -> {
                        System.out.print("Enter Workout ID: ");
                        String id = sc.nextLine().trim();
                        Optional<WorkoutEntry> w = workoutService.getById(id);
                        w.ifPresentOrElse(
                                System.out::println,
                                () -> System.out.println("Workout not found!")
                        );
                    }
                    case "14" -> {
                        while (true) {
                            System.out.println("\n=== Analytics Dashboard ===");
                            System.out.println("1. Study Stats");
                            System.out.println("2. Coding Stats");
                            System.out.println("3. Workout Stats");
                            System.out.println("0. Back");

                            System.out.print("Choice: ");
                            String ch = sc.nextLine().trim();

                            if (ch.equals("1")) {
                                System.out.println("\n--- Study Analytics ---");
                                System.out.println("Total tasks: " + studyAnalytics.getTotalTasks(userId));
                                System.out.println("Pending tasks: " + studyAnalytics.getPendingTasks(userId));
                                System.out.println("Completed tasks: " + studyAnalytics.getCompletedTasks(userId));
                                System.out.println("Overdue tasks: " + studyAnalytics.getOverdueTasks(userId));
                                System.out.println("High priority tasks: " + studyAnalytics.getHighPriorityCount(userId));

                            } else if (ch.equals("2")) {
                                System.out.println("\n--- Coding Analytics ---");
                                System.out.println("Total sessions: " + codingAnalytics.getTotalSessions(userId));
                                System.out.println("Solved: " + codingAnalytics.getSolvedCount(userId));
                                System.out.println("Unsolved: " + codingAnalytics.getUnsolvedCount(userId));
                                System.out.println("Total minutes: " + codingAnalytics.getTotalMinutes(userId));
                                System.out.println("Easy: " + codingAnalytics.getEasyCount(userId));
                                System.out.println("Medium: " + codingAnalytics.getMediumCount(userId));
                                System.out.println("Hard: " + codingAnalytics.getHardCount(userId));
                                System.out.println("Today's minutes: " + codingAnalytics.getTodayMinutes(userId));
                                System.out.println("Coding streak: " + codingAnalytics.getCodingStreak(userId));

                            } else if (ch.equals("3")) {
                                System.out.println("\n--- Workout Analytics ---");
                                System.out.println("Total workouts: " + workoutAnalytics.getTotalWorkouts(userId));
                                System.out.println("Total minutes: " + workoutAnalytics.getTotalMinutes(userId));
                                System.out.println("Today's minutes: " + workoutAnalytics.getTodayMinutes(userId));
                                System.out.println("Weekly minutes (last 7 days): " + workoutAnalytics.getWeeklyMinutes(userId));
                                System.out.println("Workout streak: " + workoutAnalytics.getWorkoutStreak(userId));
                                System.out.println("Best workout day: " + workoutAnalytics.getBestWorkoutDay(userId));

                            } else if (ch.equals("0")) {
                                break;
                            } else {
                                System.out.println("Invalid choice!");
                            }
                        }
                    }

                    case "15" -> {
                        ProductivityScoreService scoreService = new ProductivityScoreService(
                                studyAnalytics,
                                codingAnalytics,
                                workoutAnalytics,
                                userId
                        );

                        System.out.println("\n=== Productivity Score ===");
                        System.out.println("Study Score:   " + scoreService.getStudyScore() + "/40");
                        System.out.println("Coding Score:  " + scoreService.getCodingScore() + "/40");
                        System.out.println("Workout Score: " + scoreService.getWorkoutScore() + "/20");
                        System.out.println("Streak Bonus:  " + scoreService.getStreakBonus());

                        System.out.println("-----------------------------");
                        System.out.println("TOTAL SCORE:   " + scoreService.getTotalScore() + "/100");

                        int score = scoreService.getTotalScore();
                        if (score >= 85) System.out.println("🔥 Outstanding day! Keep dominating!");
                        else if (score >= 70) System.out.println("💪 Great progress, stay consistent!");
                        else if (score >= 50) System.out.println("🙂 Decent effort, push a little more!");
                        else System.out.println("😴 Low productivity day — tomorrow is yours!");
                    }

                    case "16" -> {
                        ProductivityScoreService scoreService = new ProductivityScoreService(
                                studyAnalytics,
                                codingAnalytics,
                                workoutAnalytics,
                                userId
                        );

                        AnalyticsExportService exportService = new AnalyticsExportService(
                                studyAnalytics,
                                codingAnalytics,
                                workoutAnalytics,
                                scoreService,
                                userId
                        );

                        String outPath = "data/analytics_export.json";
                        exportService.exportToJson(outPath);

                        System.out.println("Analytics exported successfully to: " + outPath);
                    }




                    case "0" -> {
                        System.out.println("Bye!");
                        sc.close();
                        return;
                    }

                    default -> System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("⚠ Error: " + e.getMessage());
            }
        }
    }
}
