package com.urlshortener.DTO;

public class URLRequest {
    private String originalUrl;
    
    private Long userId;

    // Getter and Setter
    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long user_id) {
		this.userId = userId;
	}
	
	
    
}
