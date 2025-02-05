import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import "../ResetPassword.css"

function ResetPassword() {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const token = queryParams.get("token"); // Extract token from URL query params

  const [password, setpassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleResetPassword = async (e) => {
    e.preventDefault();

    if (!token) {
      setMessage("Invalid reset link.");
      return;
    }

    if (password !== confirmPassword) {
      setMessage("Passwords do not match!");
      return;
    }

    try {
      await axios.post("http://localhost:8080/auth/reset-password", {
        token,
        password,
      });

      setMessage("Password reset successful! Redirecting to login...");
      setTimeout(() => navigate("/login"), 3000);
    } catch (error) {
      setMessage("Invalid or expired token. Try again.");
    }
  };

  return (
    <div className="reset-password-container">
      <h2>Reset Password</h2>
      {message && <p style={{ color: "blue" }}>{message}</p>}
      <form onSubmit={handleResetPassword}>
        <div className="first">
          
        <label>New Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setpassword(e.target.value)}
            placeholder="Enter new password"
            required
          />
        </div>
        <div className="second">
          <label>Confirm Password:</label>
          <input
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            placeholder="Confirm new password"
            required
          />
        </div>
        <button type="submit">Reset Password</button>
      </form>
    </div>
  );
}

export default ResetPassword;

