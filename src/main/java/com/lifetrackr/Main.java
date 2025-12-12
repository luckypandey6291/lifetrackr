package com.lifetrackr;

import com.lifetrackr.model.*;
import com.lifetrackr.repository.impl.InMemoryStudyTaskRepository;
import com.lifetrackr.repository.impl.InMemoryCodingSessionRepository;
import com.lifetrackr.repository.impl.InMemoryWorkoutRepository;
import com.lifetrackr.service.StudyTaskService;
import com.lifetrackr.service.CodingSessionService;
import com.lifetrackr.service.WorkoutService;
import com.lifetrackr.service.impl.StudyTaskServiceImpl;
import com.lifetrackr.service.impl.CodingSessionServiceImpl;
import com.lifetrackr.service.impl.WorkoutServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== LifeTrackr Console App ===");

        User demo = new User("Lucky", "lucky@example.com");
        String userId = demo.getId();
        System.out.println("Demo user: " + demo);

        // repositories & services
        InMemoryStudyTaskRepository taskRepo = new InMemoryStudyTaskRepository();
        InMemoryCodingSessionRepository codingRepo = new InMemoryCodingSessionRepository();
        InMemoryWorkoutRepository workoutRepo = new InMemoryWorkoutRepository();

        StudyTaskService taskService = new StudyTaskServiceImpl(taskRepo);
        CodingSessionService codingService = new CodingSessionServiceImpl(codingRepo);
        WorkoutService workoutService = new WorkoutServiceImpl(workoutRepo);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== MENU ===");
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
