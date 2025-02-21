import React, { useState,useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom"; // Import useNavigate for redirection
import "../Login.css";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const navigate = useNavigate(); // Hook for redirection


  useEffect(() => {
    // Check if userId exists in localStorage when component mounts
    if (localStorage.getItem("userId")) {
      setIsLoggedIn(true);
    }
  }, []);

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        "http://localhost:8080/users/login",
        { email, password },
        { withCredentials: true }
      );

      console.log(response.data)
      const userId = response.data.userId; // Get userId from response

      if (userId==null) {
        // If response data is null, show error
        setErrorMessage("Invalid credentials. Please try again.");
        return;
      }

      localStorage.setItem("userId", userId); // Store userId in local storage
      setIsLoggedIn(true);
      // Redirect to URL shortener with userId
      navigate(`/url-shortener?`);
    } catch (error) 
    {
      // Handle any errors that occur during the request
    if (error.response) {
      setErrorMessage(error.response.data || "An error occurred. Please try again.");
    } else {
      setErrorMessage("Network error. Please check your connection.");
    }

    }
  };

  return (
    <div className="logindiv">
      <h2>Login</h2>
      {errorMessage && <p style={{ color: "red", fontSize: "18px" }}>{errorMessage}</p>}

      <form onSubmit={handleLogin}>
        <div id="emailid">
          <label>Email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Enter email here"
            required
          />
        </div>
        <div id="passid">
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Enter password here"
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>

       {/* Forgot Password Link */}
       <p className="forgotpassword">
        <a href="#" onClick={() => navigate("/forgot-password")}>
          Forgot Password?
        </a>
      </p>

      {/* Logout Link - Only visible when user is logged in */}
      {isLoggedIn && (
  <p className="logout">
    <a href="#" onClick={() => navigate("/logout")}>Logout</a>
  </p>
)}
      

    </div>
  );
}

export default Login;
