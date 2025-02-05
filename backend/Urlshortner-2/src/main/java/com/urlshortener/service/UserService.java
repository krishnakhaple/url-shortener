package com.urlshortener.service;

import com.urlshortener.entities.User;
import com.urlshortener.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register a new user
    public User registerUser(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists with this email.");
        }
        User user = new User(email, password);
        return userRepository.save(user);
    }

    // Authenticate user (Login)
    public boolean authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.isPresent() && userOpt.get().getPassword().equals(password);
    }

    // Get User by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get User by Email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
