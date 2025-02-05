package com.urlshortener.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.urlshortener.entities.User;
import com.urlshortener.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;  // Mail service

    // Generate Token and Send Email
    public String generateResetToken(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return "User not found";
        }

        User user = userOptional.get();
        String token = UUID.randomUUID().toString();  // Generate random token
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(10)); // Token valid for 15 min
        userRepository.save(user);

        sendResetEmail(user.getEmail(), token); // Send email

        return "Password reset link sent!";
    }

    // Send Email
    private void sendResetEmail(String email, String token) {
        String resetLink = "http://localhost:5173/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Your Password");
        message.setText("Click the link below to reset your password: \n" + resetLink);

        mailSender.send(message);
    }

    public String updatePassword(String password, String token) {
        User user = userRepository.findByresetToken(token).orElse(null);

        if (user == null) {
            return "Invalid or expired token.";
        }

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return "Token has expired. Request a new one.";
        }

        // Hash the password before saving
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        user.setPassword(encodedPassword);
        
        // Clear the reset token after password update
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        
        userRepository.save(user);
        return "Password updated successfully";
    }

}
