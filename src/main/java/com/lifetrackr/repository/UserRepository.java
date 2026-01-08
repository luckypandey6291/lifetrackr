package com.lifetrackr.repository;

import com.lifetrackr.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);
}
