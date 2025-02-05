import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function Logout() {
  const [message, setMessage] = useState("Logging out...");
  const navigate = useNavigate();

  useEffect(() => {
    // Set a delay to ensure the process happens after the component is mounted
    const timer = setTimeout(() => {
      const userId = localStorage.getItem("userId");

      if (userId) {
        localStorage.removeItem("userId"); // Only remove userId if it exists
        setMessage("You have logged out successfully.");
      } else {
        setMessage("You were not logged in.");
      }

      // Redirect to home page after showing message for 2 seconds
      setTimeout(() => {
        navigate("/"); // Redirect to the home page
      }, 2000);
    }, 500); // Delay to ensure localStorage updates first

    return () => clearTimeout(timer); // Cleanup the timeout when the component is unmounted
  }, [navigate]);

  return <h2>{message}</h2>;
}

export default Logout;
