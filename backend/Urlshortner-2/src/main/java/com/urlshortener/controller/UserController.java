package com.urlshortener.controller;

import com.urlshortener.entities.User;
import com.urlshortener.repository.UserRepository;
import com.urlshortener.security.CustomUserDetailsService;
import com.urlshortener.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
	

    @Autowired
    private AuthenticationManager authenticationManager; // Inject AuthenticationManager

    @Autowired
    private CustomUserDetailsService customUserDetailsService; 

    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder passwordencoder;
    
   
    @Autowired
    private UserRepository userRepository;
    // User Registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
    	
    	String email=user.getEmail();
    	User user1 = userService.getUserByEmail(email).orElse(null);
    	
    	if(user1!=null)
    	{
    		System.out.println("user already exist");
    		return new ResponseEntity<>("user already exist",HttpStatus.CONFLICT);
    	}
    	
    	String pass=passwordencoder.encode(user.getPassword());
    	System.out.println("encoded password is :"+pass);
    	
       userService.registerUser(user.getEmail(),pass);
        return ResponseEntity.ok("User registered successfully");
    }


    
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user,Model model) {
    	
//        System.out.println("Attempting to log in with email: " + user.getEmail()); // Print incoming email
//        
//        
//
//        User existingUser = userService.getUserByEmail(user.getEmail()).orElse(null);
//
//        if (existingUser == null) {
//            System.out.println("User not found!");
//            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
//        }
//
//        
//        
//        System.out.println("User found: " + existingUser.getEmail()); // Print found user details
//        
//       
//        Long userId=existingUser.getId();
//        
//        
//
//        if (!existingUser.getPassword().equals(user.getPassword())) {
//            System.out.println("Invalid password");
//            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
//        }
//
//        System.out.println("login successful");
//        
//        session.setAttribute("user_id", userId);
//        
//        //System.out.println("userid of the user is :"+session.getAttribute("user_id"));
//        
//        return new ResponseEntity<>("Login successful", HttpStatus.OK);
    	
    	
    	
    	
    	Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Assuming the user details are fetched from the database
        Long userId = getUserIdFromDatabase(user.getEmail());

        // Store user_id in the session
        model.addAttribute("user_id", userId);
        
        System.out.println("the user is is :"+userId);
        
       
        

        return ResponseEntity.ok(userId.toString());
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }

    
    public Long getUserIdFromDatabase(String email) {
    	
        User user = userRepository.findByEmail(email).orElse(null); // Assuming you have a method findByEmail
        
        
        return user != null ? user.getId() : null; // Return user ID or null if not found
          
    }
    
    
}
