import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import '../UserLinks.css'; // Import the CSS for the component

const UserLinks = () => {
  const { userId } = useParams(); // Get user ID from URL params
  const [urls, setUrls] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate(); // Initialize the navigate function

  useEffect(() => {
    const fetchUserUrls = async () => {
      try {
        const response = await fetch(`http://localhost:8080/url/user-urls/${userId}`);
        if (!response.ok) {
          throw new Error("Failed to fetch URLs");
        }
        const data = await response.json();
        setUrls(data.urls); // The data now contains the "urls" field
      } catch (error) {
        console.error("Error fetching user URLs:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchUserUrls();
  }, [userId]);

  const handleUpdate = (id) => {
    // Redirect to the update page for the URL
    navigate(`/update/${id}`);
  };

  const handleDelete = async (id, shortUrl) => {
    try {
      const response = await fetch('http://localhost:8080/url/delete', {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          userId: userId,  // Pass the userId
          shortUrl: shortUrl,  // Pass the shortUrl
        }),
      });
      if (response.ok) {
        setUrls(urls.filter((url) => url.id !== id)); // Remove the deleted URL from the state
        alert("URL deleted successfully");
      } else {
        alert("Failed to delete URL");
      }
    } catch (error) {
      console.error("Error deleting URL:", error);
    }
  };

  return (
    <div className="container">
      <h2>Your Shortened URLs are</h2>

      <p className="logout">
        <a href="/logout">Logout</a> {/* Link to the logout page */}
      </p>

      {loading ? (
        <p>Loading...</p>
      ) : urls.length === 0 ? (
        <p>No URLs found.</p>
      ) : (
        <table className="table">
          <thead>
            <tr>
              <th>Original URL</th>
              <th>Short URL</th>
              <th>User Email</th>
              <th>Actions</th> {/* Actions column */}
            </tr>
          </thead>
          <tbody>
            {urls.map((url) => (
              <tr key={url.id}>
                <td>
                  <a href={url.originalUrl} target="_blank" rel="noopener noreferrer">
                    {url.originalUrl}
                  </a>
                </td>
                <td>
                  <a href={`http://localhost:8080/url/${url.shortenedUrl}`} target="_blank" rel="noopener noreferrer">
                    {url.shortenedUrl}
                  </a>
                </td>
                <td>{url.user.email}</td>
                <td className="action-buttons">
                  <button onClick={() => handleUpdate(url.id)} className="btn btn-warning btn-sm">
                    Update
                  </button>
                  <button 
                    onClick={() => handleDelete(url.id, url.shortenedUrl)}  // Pass both id and shortUrl
                    className="btn btn-danger btn-sm"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default UserLinks;
