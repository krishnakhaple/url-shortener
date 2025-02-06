package com.urlshortener.controller;


import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urlshortener.entities.URL;
import com.urlshortener.service.UrlService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/url")
public class URLController {

    @Autowired
    private UrlService urlService;
    
    

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody Map<String, String> request) {
    	String baseUrl = "http://localhost:8080";
    	
        String originalUrl = request.get("originalUrl");
        
        // Check if userId exists before parsing
        Long userId = request.containsKey("userId") && request.get("userId") != null && !request.get("userId").isEmpty()
                ? Long.parseLong(request.get("userId"))
                : null;  // If not provided, set to null
        
        
         System.out.println("the  user from front end is id :"+userId);

        String shortUrl = urlService.generateShortUrl(originalUrl, userId);
        return ResponseEntity.ok(baseUrl+"/url/"+shortUrl);
    }

    @GetMapping("/user-urls/{userId}")
    public ResponseEntity<?> getUserUrls(@PathVariable("userId") Long userId) {
    	
    	List<Map<String, Object>> urls=urlService.getUserUrls(userId);
    	
    	System.out.println(urls);
    	
    	 return ResponseEntity.ok(Collections.singletonMap("urls", urls));
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateShortUrl(@RequestBody Map<String, String> request) {
    	
        Long userId = Long.parseLong(request.get("userId"));
        String oldShortUrl = request.get("oldShortUrl");
        String newShortUrl = request.get("newShortUrl");  // Only update the shortened URL
        
        System.out.println("frontend userid:"+userId+" oldShortUrl : "+ oldShortUrl+"  newShortUrl: "+newShortUrl);

        // Check if the URL exists and if the user is authorized to modify it
        boolean updated = urlService.updateShortUrl(userId, oldShortUrl, newShortUrl);

        return updated ? ResponseEntity.ok("Shortened URL updated successfully") : ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized or Not Found");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteShortUrl(@RequestBody Map<String, String> request) {
        Long userId = Long.parseLong(request.get("userId"));
        String shortUrl = request.get("shortUrl");

        boolean deleted = urlService.deleteShortUrl(userId, shortUrl);
        return deleted ? ResponseEntity.ok("Deleted Successfully") : ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized or Not Found");
    }
    
    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToOriginalURL(@PathVariable("shortUrl") String shortUrl) {
        // Find the URL record from the database
        String longUrl = urlService.findbyshort(shortUrl);
        
        System.out.println(longUrl);

        if (longUrl != null) {
            // Create an HttpHeaders object for setting the Location header
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(longUrl)); // Set the long URL as the location for redirection
            
            // Return a response with the Location header
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            // Return a NOT_FOUND response if the short URL does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping("/getUrl/{id}")
    public ResponseEntity<URL> getUrlById(@PathVariable("id") Long id) {
        Optional<URL> urlData = urlService.getUrlById(id);
        return ResponseEntity.ok(urlData.get());
    }

}
    


