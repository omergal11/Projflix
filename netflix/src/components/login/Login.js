import React, { useState } from 'react';
import { Link } from "react-router-dom";
import './Login.css'; // Import CSS specific to Login component
import { authServices } from '../../services/services';
import TextInput from '../textInput/TextInput';
import { useNavigate } from 'react-router-dom';
// Login component
const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
   const [successMessage, setSuccessMessage] = useState(''); 
  const [error, setError] = useState(''); // State for error message
  const [loading, setLoading] = useState(false); // To track loading state
  const navigate = useNavigate(); // instance  useNavigate

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    setError(''); // Clear error if no issues
    setLoading(true); // Start loading when submitting
     // Call login service
    try {
      const data = {
        email: email,
        password: password
        };
        const response = await authServices.login(data);
        const responseData = await response.json();
        if (!response.ok) {
             if (responseData.error) {
                  throw new Error(responseData.error);
            }
        }

      // If you want to store the token in localStorage
      localStorage.setItem('jwtToken',responseData.token);
      setSuccessMessage('Login successful! .');
      // Call onSubmit to handle any additional actions in parent component (optional)
      navigate('/homePage');

    } catch (error) {
      // Handle errors such as invalid credentials or server issues
      setError(error.message);
    } finally {
      setLoading(false); // Stop loading once the request is completed
    }
  };
   // display login form    
  return (
    <div className="hero-section1">
      <header className="header">
        <img
          src="/projflix_logo.svg"
          alt="Projflix Logo"
          className="logo"
        />
      </header>
    <div className="auth-form">
      <h2>Sign in</h2>
      {error && <p className="error-message">{error}</p>}
      {successMessage && <p className="success-message">{successMessage}</p>} 
      <form onSubmit={handleSubmit}>
        <TextInput id="email" value={email} onChange={(e) => setEmail(e.target.value)} label="Email" placeholder="Enter your email" />
        <TextInput id="password" value={password} onChange={(e) => setPassword(e.target.value)} label="Password" placeholder="Enter your password" type="password" />    
        <button type="submit" className="button" disabled={loading}>
          {loading ? 'Logging in...' : 'Login'}
        </button>
      </form>
      <p className="auth-link">
        Don't have an account?{' '}
        <Link to="/signup">Sign Up</Link>
      </p>
    </div>
    </div>
  );
};

export default Login;