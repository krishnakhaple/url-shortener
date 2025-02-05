package com.urlshortener.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urlshortener.entities.URL;
import com.urlshortener.entities.User;
import com.urlshortener.repository.URLRepository;

@Service
public class URLService1 {

    @Autowired
    private URLRepository urlRepository;

    // Shorten URL logic
    public String shortenUrl(String originalUrl,User user) {
        // Check if the URL already exists in the database
        URL existingUrl = urlRepository.findByOriginalUrl(originalUrl).orElse(null);

        // If the original URL exists, return the corresponding shortened URL
        if (existingUrl != null) {
            return existingUrl.getShortenedUrl();
        }

     
        // Otherwise, create a new shortened URL
        String shortenedUrl = generateShortenedUrl();
        URL url = new URL();
        url.setOriginalUrl(originalUrl);
        url.setShortenedUrl(shortenedUrl);
        url.setUser(user);
        
        urlRepository.save(url);
       

        return shortenedUrl;
    }

    // Generate a random shortened URL (6 characters long)
    public String generateShortenedUrl() {
        return UUID.randomUUID().toString().substring(0, 6); // Example: random 6 characters
    }

    // Fetch original URL from database
    public URL getUrl(String shortenedUrl) {
        return urlRepository.findByShortenedUrl(shortenedUrl).orElse(null);
    }
}
