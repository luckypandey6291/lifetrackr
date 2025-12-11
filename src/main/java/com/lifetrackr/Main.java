package com.lifetrackr;

import com.lifetrackr.model.StudyTask;
import com.lifetrackr.model.User;
import com.lifetrackr.repository.impl.InMemoryStudyTaskRepository;
import com.lifetrackr.service.StudyTaskService;
import com.lifetrackr.service.impl.StudyTaskServiceImpl;

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

        // wiring
        InMemoryStudyTaskRepository repo = new InMemoryStudyTaskRepository();
        StudyTaskService service = new StudyTaskServiceImpl(repo);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add study task");
            System.out.println("2. List all tasks");
            System.out.println("3. List pending tasks");
            System.out.println("4. Delete task by id");
            System.out.println("5. Show task by id");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    try {
                        System.out.print("Subject: ");
                        String subject = sc.nextLine().trim();
                        System.out.print("Topic: ");
                        String topic = sc.nextLine().trim();
                        System.out.print("Due date (YYYY-MM-DD): ");
                        LocalDate due = LocalDate.parse(sc.nextLine().trim());
                        System.out.print("Priority (1-High,2-Med,3-Low): ");
                        int p = Integer.parseInt(sc.nextLine().trim());
                        StudyTask t = service.createTask(userId, subject, topic, due, p);
                        System.out.println("Created: " + t);
                    } catch (Exception e) {
                        System.out.println("Error creating task: " + e.getMessage());
                    }
                }
                case "2" -> {
                    List<StudyTask> all = service.getTasksForUser(userId);
                    System.out.println("--- All Tasks (" + all.size() + ") ---");
                    all.forEach(System.out::println);
                }
                case "3" -> {
                    List<StudyTask> pending = service.getPendingTasksForUser(userId);
                    System.out.println("--- Pending Tasks (" + pending.size() + ") ---");
                    pending.forEach(System.out::println);
                }
                case "4" -> {
                    System.out.print("Enter task id to delete: ");
                    String id = sc.nextLine().trim();
                    Optional<StudyTask> before = service.getById(id);
                    if (before.isPresent()) {
                        service.deleteById(id);
                        System.out.println("Deleted task: " + id);
                    } else {
                        System.out.println("Task not found: " + id);
                    }
                }
                case "5" -> {
                    System.out.print("Enter task id to show: ");
                    String id = sc.nextLine().trim();
                    Optional<StudyTask> t = service.getById(id);
                    t.ifPresentOrElse(
                            task -> System.out.println("Task: " + task),
                            () -> System.out.println("Not found")
                    );
                }
                case "0" -> {
                    System.out.println("Bye!");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
