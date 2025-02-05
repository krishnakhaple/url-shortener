package com.urlshortener.controller;

import java.net.URI;
import org.springframework.http.HttpHeaders;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urlshortener.DTO.URLRequest;
import com.urlshortener.entities.URL;
import com.urlshortener.entities.User;
import com.urlshortener.service.URLService1;

@RestController
@RequestMapping("/url1")
public class URLController1 {

    @Autowired
    private URLService1 urlService;
    

    // Endpoint for shortening URLs (Anonymous)
    @PostMapping("/shorten1")
    public ResponseEntity<String> createShortenedUrl(@RequestBody URL urlRequest) {
        String originalUrl = urlRequest.getOriginalUrl();
        User user = urlRequest.getUser();
        String shortenedUrl = urlService.shortenUrl(originalUrl,user);
        return ResponseEntity.ok("http://localhost:8080/url/" + shortenedUrl); // Returning shortened URL
    }

    // Endpoint to redirect to original URL
    @GetMapping("/{shortenedUrl}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable("shortenedUrl") String shortenedUrl) {
        URL url = urlService.getUrl(shortenedUrl);
        if (url == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.getOriginalUrl()));

        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(headers)
                .build();
    }

}
