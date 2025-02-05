package com.urlshortener.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class URL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalUrl;
    private String shortenedUrl;
    
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    // Getters and Setters
    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	public URL( String originalUrl, String shortenedUrl, User user) {
		super();
	
		this.originalUrl = originalUrl;
		this.shortenedUrl = shortenedUrl;
		this.user = user;
	}

	public URL() {
		super();
		
	}

	public Long getId() {
		return id;
	}


	@Override
	public String toString() {
		return "URL [id=" + id + ", originalUrl=" + originalUrl + ", shortenedUrl=" + shortenedUrl + ", user=" + user
				+ "]";
	}
    
	
	
    
}
