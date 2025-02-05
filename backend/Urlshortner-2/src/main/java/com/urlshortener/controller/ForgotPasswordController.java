package com.urlshortener.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.urlshortener.service.ForgotPasswordService;

@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody Map<String, String> requestBody) {
    	String email=requestBody.get("email");
        return forgotPasswordService.generateResetToken(email);
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String,String> requestBody)
    {
    	String password=requestBody.get("password");
    	String token=requestBody.get("token");
    	String response= forgotPasswordService.updatePassword(password,token);
    	
    	if(response.equals(response))
    	{
    		return ResponseEntity.ok(response);
    	}
    	
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
