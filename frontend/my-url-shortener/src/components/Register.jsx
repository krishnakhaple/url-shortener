import React, { useState } from "react";
import axios from "axios"; // Import axios for making HTTP requests
import "../Register.css";

function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    
    // Create an object to send to the backend
    const userData = {
      email: email,
      password: password,
    };

    // Send data to backend via POST request
    axios
      .post("http://localhost:8080/users/register", userData)
      .then((response) => {
        console.log(response.data);
        setSuccessMessage(response.data); 
        setErrorMessage(""); // Clear any previous errors
      })
      .catch((error) => {
        console.error("There was an error registering the user!", error);
        if (error.response && error.response.data) {
            setErrorMessage(error.response.data);  // Show error message if the user already exists
          } else {
            setErrorMessage("An error occurred. Please try again.");  // Generic error message
          }
          setSuccessMessage("");  // Clear success message if any
      });
  };

  return (
    <div className="registerdiv" >
      <h2>Register</h2>

         {/* Show the success or error message */}
      {successMessage && <p style={{ color: "green" }}>{successMessage}</p>}
      {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}

      <form onSubmit={handleSubmit}>
        <div id="emailid">
          <label>Email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="enter email here"
            required
          />
        </div>
        <div id="passid">
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="enter password here"
            required
          />
        </div>
        <button type="submit">Register</button>
      </form>
    </div>
  );
}

export default Register;
