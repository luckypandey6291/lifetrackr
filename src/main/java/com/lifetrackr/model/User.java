package com.lifetrackr.model;

import java.util.UUID;

public class User {

    private String id;   // ❗ removed final
    private String username;
    private String email;

    public User(String username, String email) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
    }

    // ===== getters & setters =====

    public String getId() { return id; }

    // 🔥 REQUIRED for SQLite / DB hydration
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

