package com.lifetrackr.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:lifetrackr.db";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                createTables();
            }
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to DB: " + e.getMessage());
        }
    }

    private static void createTables() {
        try (Statement stmt = connection.createStatement()) {

            // Users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id TEXT PRIMARY KEY,
                    username TEXT NOT NULL,
                    email TEXT NOT NULL
                );
            """);

            // Study tasks table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS study_tasks (
                    id TEXT PRIMARY KEY,
                    user_id TEXT NOT NULL,
                    subject TEXT,
                    topic TEXT,
                    due_date TEXT,
                    status TEXT,
                    priority INTEGER,
                    FOREIGN KEY(user_id) REFERENCES users(id)
                );
            """);

            // Coding sessions table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS coding_sessions (
                    id TEXT PRIMARY KEY,
                    user_id TEXT NOT NULL,
                    problem_name TEXT,
                    platform TEXT,
                    difficulty TEXT,
                    duration_minutes INTEGER,
                    solved INTEGER,
                    created_at TEXT,
                    FOREIGN KEY(user_id) REFERENCES users(id)
                );
            """);

            // Workouts table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS workouts (
                    id TEXT PRIMARY KEY,
                    user_id TEXT NOT NULL,
                    type TEXT,
                    description TEXT,
                    duration_minutes INTEGER,
                    date TEXT,
                    FOREIGN KEY(user_id) REFERENCES users(id)
                );
            """);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create tables: " + e.getMessage());
        }
    }
}
