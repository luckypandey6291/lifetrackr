package com.lifetrackr.repository.impl;

import com.lifetrackr.db.Database;
import com.lifetrackr.model.User;
import com.lifetrackr.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class SQLiteUserRepository implements UserRepository {

    private final Connection conn = Database.getConnection();

    @Override
    public User save(User user) {
        try {
            String sql = """
                INSERT INTO users (id, username, email)
                VALUES (?, ?, ?)
                ON CONFLICT(id) DO UPDATE SET
                    username = excluded.username,
                    email = excluded.email;
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();

            return user;

        } catch (Exception e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public Optional<User> findById(String id) {
        try {
            PreparedStatement stmt =
                    conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by id", e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            PreparedStatement stmt =
                    conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by username", e);
        }
    }

    private User mapRow(ResultSet rs) throws Exception {
        User user = new User(
                rs.getString("username"),
                rs.getString("email")
        );
        user.setId(rs.getString("id"));
        return user;
    }
}

