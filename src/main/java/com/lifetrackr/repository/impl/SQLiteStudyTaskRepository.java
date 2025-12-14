package com.lifetrackr.repository.impl;

import com.lifetrackr.model.TaskStatus;


import com.lifetrackr.db.Database;
import com.lifetrackr.model.StudyTask;
import com.lifetrackr.repository.StudyTaskRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteStudyTaskRepository implements StudyTaskRepository {

    private final Connection conn = Database.getConnection();

    @Override
    public StudyTask save(StudyTask task) {
        try {
            String sql =
                    "INSERT INTO study_tasks (id, user_id, subject, topic, due_date, status, priority) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                            "ON CONFLICT(id) DO UPDATE SET " +
                            "subject = excluded.subject, " +
                            "topic = excluded.topic, " +
                            "due_date = excluded.due_date, " +
                            "status = excluded.status, " +
                            "priority = excluded.priority";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, task.getId());
            stmt.setString(2, task.getUserId());
            stmt.setString(3, task.getSubject());
            stmt.setString(4, task.getTopic());
            stmt.setString(5, task.getDueDate().toString());
            stmt.setString(6, task.getStatus().name());
            stmt.setInt(7, task.getPriority());

            stmt.executeUpdate();
            return task;

        } catch (Exception e) {
            throw new RuntimeException("Failed to save study task", e);
        }
    }

    @Override
    public Optional<StudyTask> findById(String id) {
        try {
            PreparedStatement stmt =
                    conn.prepareStatement("SELECT * FROM study_tasks WHERE id = ?");
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Failed to find study task by id", e);
        }
    }

    @Override
    public List<StudyTask> findByUserId(String userId) {
        List<StudyTask> list = new ArrayList<>();
        try {
            PreparedStatement stmt =
                    conn.prepareStatement("SELECT * FROM study_tasks WHERE user_id = ?");
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("Failed to find tasks by user", e);
        }
    }

    @Override
    public List<StudyTask> findPendingByUserId(String userId) {
        List<StudyTask> list = new ArrayList<>();
        try {
            PreparedStatement stmt =
                    conn.prepareStatement(
                            "SELECT * FROM study_tasks WHERE user_id = ? AND status = 'PENDING'");
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("Failed to find pending tasks", e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            PreparedStatement stmt =
                    conn.prepareStatement("DELETE FROM study_tasks WHERE id = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete task", e);
        }
    }

    private StudyTask mapRow(ResultSet rs) throws Exception {

        StudyTask task = new StudyTask(
                rs.getString("user_id"),
                rs.getString("subject"),
                rs.getString("topic"),
                LocalDate.parse(rs.getString("due_date")),
                rs.getInt("priority")
        );

        // overwrite auto-generated values with DB values
        task.setId(rs.getString("id"));
        task.setStatus(TaskStatus.valueOf(rs.getString("status")));

        return task;
    }
}

