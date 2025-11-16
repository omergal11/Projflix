import React, {useEffect} from 'react';
import { Navigate } from 'react-router-dom';
import {jwtDecode} from 'jwt-decode';

// PrivateRoute component
const PrivateRoute = ({ children, adminOnly= false }) => {
  const token = localStorage.getItem('jwtToken');
  // useEffect to check if token is present and if user is admin
 useEffect(() => {
    // cant access page without token
    if (!token) {
        window.alert('You must be logged in to access this page.');
    }
    else {
       const decoded =  jwtDecode(token);
       // cant access page if user is not admin
       if (adminOnly && decoded.role !== 'admin') {
        window.alert('You do not have permission to access this page.');
      }
    }
  } , [token]);

  if (!token) {
    return <Navigate to="/login" />;
  }
  // if user is not admin, redirect to homePage
  if (adminOnly) {
    const decoded = jwtDecode(token);
    if (decoded.role !== 'admin') {
      return <Navigate to="/homePage" />;
    }
  }


  return children;
};

export default PrivateRoute;