package com.lifetrackr.repository.impl;

import com.lifetrackr.db.Database;
import com.lifetrackr.model.WorkoutEntry;
import com.lifetrackr.model.WorkoutType;
import com.lifetrackr.repository.WorkoutRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteWorkoutRepository implements WorkoutRepository {

    private final Connection conn = Database.getConnection();

    @Override
    public WorkoutEntry save(WorkoutEntry workout) {
        try {
            String sql =
                    "INSERT INTO workouts (id, user_id, type, description, duration_minutes, date) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, workout.getId());
            stmt.setString(2, workout.getUserId());
            stmt.setString(3, workout.getType().name());
            stmt.setString(4, workout.getDescription());
            stmt.setInt(5, workout.getDurationMinutes());
            stmt.setString(6, workout.getDate().toString());

            stmt.executeUpdate();
            return workout;

        } catch (Exception e) {
            throw new RuntimeException("Failed to save workout", e);
        }
    }

    @Override
    public Optional<WorkoutEntry> findById(String id) {
        try {
            PreparedStatement stmt =
                    conn.prepareStatement("SELECT * FROM workouts WHERE id = ?");
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Failed to find workout by id", e);
        }
    }

    @Override
    public List<WorkoutEntry> findByUserId(String userId) {
        List<WorkoutEntry> list = new ArrayList<>();
        try {
            PreparedStatement stmt =
                    conn.prepareStatement("SELECT * FROM workouts WHERE user_id = ?");
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("Failed to find workouts for user", e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            PreparedStatement stmt =
                    conn.prepareStatement("DELETE FROM workouts WHERE id = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete workout", e);
        }
    }

    private WorkoutEntry mapRow(ResultSet rs) throws Exception {
        WorkoutEntry workout = new WorkoutEntry(
                rs.getString("user_id"),
                WorkoutType.valueOf(rs.getString("type")),
                rs.getString("description"),
                rs.getInt("duration_minutes"),
                LocalDate.parse(rs.getString("date"))
        );

        // hydrate DB-generated fields
        workout.setId(rs.getString("id"));
        return workout;
    }
}
