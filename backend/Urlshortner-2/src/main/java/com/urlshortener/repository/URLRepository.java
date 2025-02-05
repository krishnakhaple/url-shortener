package com.urlshortener.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urlshortener.entities.URL;
import com.urlshortener.entities.User;

import jakarta.transaction.Transactional;

@Repository
public interface URLRepository extends JpaRepository<URL, Long> {
    Optional<URL> findByShortenedUrl(String shortenedUrl);
    Optional<URL> findByOriginalUrl(String originalUrl);
    List<URL> findByUser(User user);
    @Transactional
    void deleteByUserAndShortenedUrl(User user, String shortenedUrl);
    
}
