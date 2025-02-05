package com.urlshortener.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disabling CSRF protection (can enable if required)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/url/shorten", "/users/login", "/users/register","/url/{shortUrl}","/url/user-urls/{userId}","/url/getUrl/{id}","/auth/forgot-password","/auth/reset-password").permitAll()  // Public URLs
                .anyRequest().authenticated()  // Other URLs require authentication
            
       
            );

        return http.build();
    }

 
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        
        // You need to set your custom UserDetailsService and PasswordEncoder
        authenticationManagerBuilder.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder);  // Use your PasswordEncoder
        
        return authenticationManagerBuilder.build();
    }
}
