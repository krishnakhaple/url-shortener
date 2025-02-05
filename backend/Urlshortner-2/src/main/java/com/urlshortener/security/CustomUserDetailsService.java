package com.urlshortener.security;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.urlshortener.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        com.urlshortener.entities.User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Return user details without roles
        return new User(user.getEmail(), user.getPassword(), new ArrayList<>()); // No roles
    }
}
