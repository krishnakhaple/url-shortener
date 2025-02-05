package com.urlshortener.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urlshortener.entities.URL;
import com.urlshortener.entities.User;
import com.urlshortener.repository.URLRepository;
import com.urlshortener.repository.UserRepository;

@Service
public class UrlService {

    @Autowired
    private URLRepository urlRepository;

    @Autowired
    private UserRepository userRepository;

    public String generateShortUrl(String originalUrl, Long userId) {
        URL existingUrl = urlRepository.findByOriginalUrl(originalUrl).orElse(null);

        if (existingUrl != null) {
            return existingUrl.getShortenedUrl();
        }

        String shortUrl = generateRandomString(6);  // Generate a random string with a length of 6 characters

        User user = (userId != null) ? userRepository.findById(userId).orElse(null) : null;

        URL url = new URL(originalUrl, shortUrl, user);
        urlRepository.save(url);
        return shortUrl;
    }

    
 // Helper function to generate a random alphanumeric string of a specified length
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }
    
    

    public List<Map<String, Object>> getUserUrls(Long userId) {
        // Find user by userId
        User user = userRepository.findById(userId).orElse(null);

        // If user not found, return an empty list
        if (user == null) {
            return new ArrayList<>();
        }

        // Fetch URLs associated with the user
        List<URL> urls = urlRepository.findByUser(user);
        
        // Map URLs to desired structure
        List<Map<String, Object>> responseUrls = new ArrayList<>();
        for (URL url : urls) {
            Map<String, Object> urlMap = new HashMap<>();
            urlMap.put("id", url.getId());
            urlMap.put("originalUrl", url.getOriginalUrl());
            urlMap.put("shortenedUrl", url.getShortenedUrl());

            // Creating user map to include necessary fields (for example, email)
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("email", user.getEmail());  // Adjust according to the fields you need

            urlMap.put("user", userMap);  // Add the user information to the URL response

            responseUrls.add(urlMap);
        }

        return responseUrls;
    }

    public boolean updateShortUrl(Long userId, String oldShortUrl, String newShortUrl) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        Optional<URL> existingUrl = urlRepository.findByShortenedUrl(oldShortUrl);
        
        if (existingUrl.isPresent() && existingUrl.get().getUser().equals(user)) {
            URL url = existingUrl.get();
            url.setShortenedUrl(newShortUrl);
            urlRepository.save(url);
            System.out.println("updated sucessfully bhai");
            return true;
        }
        return false;
    }

    public boolean deleteShortUrl(Long userId, String shortUrl) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        Optional<URL> existingUrl = urlRepository.findByShortenedUrl(shortUrl);
        
        if (existingUrl.isPresent()) {
            URL url = existingUrl.get();
            if (url.getUser() == null) {
                return false; // Anonymous users cannot delete their URLs
            }
            if (url.getUser().equals(user)) {
                urlRepository.deleteByUserAndShortenedUrl(user, shortUrl);
                return true;
            }
        }
        return false;
    }
    
       public String findbyshort(String shorturl)
       {
    	   URL url=urlRepository.findByShortenedUrl(shorturl).orElse(null);
    	   
    	   System.out.println(shorturl);
    	   
    	   if(url!=null)
    	   {
    	   return url.getOriginalUrl();
    	   }
    	   
    	   
    	   return "";
    	   
       }


	public Optional<URL> getUrlById(Long id) {
		
		Optional<URL> url = urlRepository.findById(id);
		
		System.out.println("the url id passed here is :"+id);
		if(url.isPresent())
		{
			return url;
		}
		
		return null;
		
	}
}
