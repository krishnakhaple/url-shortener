import React, { useState } from "react";
import axios from "axios";
import "../ForgotPassword.css"

function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");

  const handleForgotPassword = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post("http://localhost:8080/auth/forgot-password", { email });

     // setMessage("If the email exists, a reset link has been sent.");
     setMessage(response.data)
    } catch (error) {
      setMessage("Error: Please try again later.");
    }
  };

  return (
    <div className="forgot-password-container">
      <h2>Forgot Password</h2>
      {message && <p style={{ color: "blue" }}>{message}</p>}
      <form onSubmit={handleForgotPassword}>
        <div className="inputdiv">
          <label>Enter your email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Enter email here"
            required
          />
        </div>
        <button type="submit">Submit</button>
      </form>
    </div>
  );
}

export default ForgotPassword;
