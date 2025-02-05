// DeleteAction.jsx
import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";

const DeleteAction = () => {
  const { urlId } = useParams();
  const [urlData, setUrlData] = useState(null);
  const navigate = useNavigate();

  // Fetch URL data for confirmation
  useEffect(() => {
    const fetchUrlData = async () => {
      try {
        const response = await fetch(`http://localhost:8080/url/${urlId}`);
        const data = await response.json();
        setUrlData(data);
      } catch (error) {
        console.error("Error fetching URL data:", error);
      }
    };

    fetchUrlData();
  }, [urlId]);

  const handleDelete = async () => {
    try {
      const response = await fetch(`http://localhost:8080/url/delete/${urlId}`, {
        method: "DELETE",
      });

      if (response.ok) {
        alert("URL deleted successfully!");
        navigate("/user-links"); // Redirect to user links page after deletion
      } else {
        alert("Error deleting URL.");
      }
    } catch (error) {
      console.error("Error deleting URL:", error);
    }
  };

  if (!urlData) {
    return <p>Loading URL data...</p>;
  }

  return (
    <div className="container">
      <h2>Delete Shortened URL</h2>
      <p>Are you sure you want to delete this URL?</p>
      <p>Original URL: {urlData.originalUrl}</p>
      <p>Shortened URL: {urlData.shortenedUrl}</p>
      <button onClick={handleDelete} className="btn-danger">Delete</button>
      <button onClick={() => navigate("/user-links")} className="btn-secondary">Cancel</button>
    </div>
  );
};

export default DeleteAction;
