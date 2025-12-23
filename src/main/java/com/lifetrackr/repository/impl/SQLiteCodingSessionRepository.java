package com.lifetrackr.repository.impl;

import com.lifetrackr.db.Database;
import com.lifetrackr.model.CodingSession;
import com.lifetrackr.model.Difficulty;
import com.lifetrackr.repository.CodingSessionRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteCodingSessionRepository implements CodingSessionRepository {

    private final Connection conn = Database.getConnection();

    @Override
    public CodingSession save(CodingSession session) {
        try {
            String sql =
                    "INSERT INTO coding_sessions " +
                            "(id, user_id, problem_name, platform, difficulty, duration_minutes, solved, created_at) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, session.getId());
            stmt.setString(2, session.getUserId());
            stmt.setString(3, session.getProblemName());
            stmt.setString(4, session.getPlatform());
            stmt.setString(5, session.getDifficulty().name());
            stmt.setInt(6, session.getDurationMinutes());
            stmt.setInt(7, session.isSolved() ? 1 : 0);
            stmt.setString(8, session.getCreatedAt().toString());

            stmt.executeUpdate();
            return session;

        } catch (Exception e) {
            throw new RuntimeException("Failed to save coding session", e);
        }
    }

    @Override
    public Optional<CodingSession> findById(String id) {
        try {
            PreparedStatement stmt =
                    conn.prepareStatement("SELECT * FROM coding_sessions WHERE id = ?");
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Failed to find coding session", e);
        }
    }

    @Override
    public List<CodingSession> findByUserId(String userId) {
        List<CodingSession> list = new ArrayList<>();
        try {
            PreparedStatement stmt =
                    conn.prepareStatement("SELECT * FROM coding_sessions WHERE user_id = ?");
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch coding sessions", e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            PreparedStatement stmt =
                    conn.prepareStatement("DELETE FROM coding_sessions WHERE id = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete coding session", e);
        }
    }

    private CodingSession mapRow(ResultSet rs) throws Exception {
        CodingSession session = new CodingSession(
                rs.getString("user_id"),
                rs.getString("problem_name"),
                rs.getString("platform"),
                Difficulty.valueOf(rs.getString("difficulty")),
                rs.getInt("duration_minutes"),
                rs.getInt("solved") == 1
        );

        // hydrate DB fields
        session.setId(rs.getString("id"));
        session.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));

        return session;
    }
}

