import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "../UpdateActions.css"

const UpdateAction = () => {
  const { urlId } = useParams(); // Get URL ID from URL params
  const navigate = useNavigate();

  // State to store old and new short URLs
  const [oldShort, setOldShort] = useState("");
  const [newShort, setNewShort] = useState("");

  // State to store original URL (read-only)
  const [originalUrl, setOriginalUrl] = useState("");

  // Fetch existing URL data
  useEffect(() => {
    const fetchUrlData = async () => {
      try {
        const response = await fetch(`http://localhost:8080/url/getUrl/${urlId}`);
        if (!response.ok) {
          throw new Error("Failed to fetch URL data");
        }
        const data = await response.json();

        // Set oldShort to the fetched shortened URL
        setOldShort(data.shortenedUrl);
        setOriginalUrl(data.originalUrl); // Keep this read-only
      } catch (error) {
        console.error("Error fetching URL data:", error);
      }
    };

    fetchUrlData();
  }, [urlId]);

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(`http://localhost:8080/url/update`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          userId: localStorage.getItem("userId"), // Get user ID from local storage
          oldShortUrl: oldShort, // Send the existing short URL
          newShortUrl: newShort, // Send the new short URL
        }),
      });

      if (response.ok) {
        alert("Shortened URL updated successfully!");
        navigate("/user-links"); // Redirect after successful update
      } else {
        alert("Error updating shortened URL.");
      }
    } catch (error) {
      console.error("Error updating URL:", error);
    }
  };

  return (
    <div className="container">
      <h2>Update Shortened URL</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Original URL</label>
          <input type="text" value={originalUrl} readOnly />
        </div>
        <div>
          <label>Old Shortened URL</label>
          <input type="text" value={oldShort} readOnly />
        </div>
        <div>
          <label>New Shortened URL</label>
          <input
            type="text"
            value={newShort}
            onChange={(e) => setNewShort(e.target.value)}
            required
          />
        </div>
        <button type="submit">Update Short URL</button>
      </form>
    </div>
  );
};

export default UpdateAction;
