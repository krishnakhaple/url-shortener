import React, { useState, useEffect } from "react";
import "../UrlShortenerForm.css";

const URLShortener = () => {
  const [longUrl, setLongUrl] = useState("");
  const [shortUrl, setShortUrl] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [userId, setUserId] = useState(null); // Initialize with null

  // Retrieve userId from localStorage on component mount and set auto-clear timer
  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    if (storedUserId) {
      setUserId(storedUserId);
    }

    // Set a timer to remove userId from localStorage after 5 minute
    const timer = setTimeout(() => {
      localStorage.removeItem("userId");
      setUserId(null); // Also clear the state
      console.log("userId removed from localStorage after 1 minute");
    }, 300000); // 60000 ms = 1 minute

    // Cleanup the timer when the component unmounts
    return () => clearTimeout(timer);
  }, []);

  // Regular expression for basic URL validation
  const isValidUrl = (url) => {
    try {
      new URL(url);
      return true;
    } catch (_) {
      return false;
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!isValidUrl(longUrl)) {
      setErrorMessage("Please enter a valid URL");
      setShortUrl("");
      return;
    }

    setErrorMessage(""); // Clear error message if URL is valid

    // Prepare data to send, including user_id if available
    const data = {
      originalUrl: longUrl,
      userId: userId || null, // Send userId if available, otherwise null
    };

    try {
      const response = await fetch("http://localhost:8080/url/shorten", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      });

      if (!response.ok) {
        throw new Error("Failed to shorten the URL");
      }

      const result = await response.text(); // Get the shortened URL as text
      setShortUrl(result); // Set the shortened URL in state
    } catch (error) {
      alert("There was an error generating the short URL");
      console.error(error);
    }
  };

  return (
    <div className="container">
      {/* Top-right corner links */}
      {userId && (
        <div className="top-right-links">
          <a href={`/user-links/${userId}`}>See All Links</a>
        </div>
      )}

      <h2>URL Shortener</h2>

      {/* Display user ID */}
      {/* <p>User ID: {userId ? userId : "Not Logged In"}</p> */}

      {errorMessage && <div className="error-message">{errorMessage}</div>}

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Enter your long URL"
          value={longUrl}
          onChange={(e) => setLongUrl(e.target.value)}
        />
        <button type="submit">Generate Short URL</button>
      </form>

      {shortUrl && (
        <div id="shorturl">
          <h4 className="headingvalue">Your short URL is:</h4>
          <h3>
            <a href={shortUrl} target="_blank" rel="noopener noreferrer">
              {shortUrl}
            </a>
          </h3>
        </div>
      )}
    </div>
  );
};

export default URLShortener;
