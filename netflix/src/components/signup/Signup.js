// Signup.js
import React, { useState } from 'react';
import { Link } from "react-router-dom";
import { authServices } from '../../services/services';
import './Signup.css';
import TextInput from '../textInput/TextInput';
import FileInput from '../fileInput/FileInput';
import { useNavigate } from 'react-router-dom';
const Signup = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [image, setImage] = useState(null);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    // Check if passwords match
    if (password !== confirmPassword) {
      setError("Passwords don't match!");
      return;
    }
  
    setError('');
    setLoading(true);
  
    // Create FormData object
  const formData = new FormData();
  formData.append('username', username);
  formData.append('email', email);
  formData.append('password', password);
  if (image) {
    formData.append('profilePicture', image);
  }
  
    try {
        // Call register service
      const response = await authServices.register(formData);
      if (response.status !== 201) {
        const data = await response.json();
        throw new Error(data.error);
      }
      // If successful, display success message and redirect to login page
      window.alert('Registration successful! You can now log in.');
      navigate('/login');
    } catch (error) {
      window.alert(error.message);
      setSuccessMessage('');
    } finally {
      setLoading(false);
    }
  };
 // display signup form
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
      <h2>Sign Up</h2>
      {error && <p className="error-message">{error}</p>}
      {successMessage && <p className="success-message">{successMessage}</p>}
      <form onSubmit={handleSubmit}>
       
        <TextInput id="email" value={email} onChange={(e) => setEmail(e.target.value)} label="Email" placeholder="Enter your email" />
        <TextInput id="username" value={username} onChange={(e) => setUsername(e.target.value)} label="Username" placeholder="Enter your username" />
        <TextInput id="password" value={password} onChange={(e) => setPassword(e.target.value)} label="Password" placeholder="Enter your password" type="password" />    
        <TextInput id="confirmPassword" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} label="Confirm Password" placeholder="Confirm your password" type="password" />
        <FileInput label="profilePicture" accept="image/*" onChange={(e) => setImage(e.target.files[0])} />
        
        <button type="submit" className="button" disabled={loading}>
          {loading ? 'Signing up...' : 'Sign Up'}
        </button>
      </form>
      <p className="auth-link">
        Already have an account?{' '}
        <Link to="/login">Login</Link>
      </p>
    </div>
    </div>
  );
};

export default Signup;