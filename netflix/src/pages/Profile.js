import React, { useState, useEffect } from "react";
import { authServices } from "../services/services";    
import "./Profile.css";

const Profile = () => {
    const [user, setUser] = useState(null);   
    const API_URL = process.env.REACT_APP_API_URL;
   // useEffect to fetch user data
    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const userData = await authServices.getUser();
                setUser(userData);
            } catch (error) {
                console.error("Error fetching user data:", error);
            }
        };

        fetchUserData();
    }, ); // Empty dependency array
    if (!user) {
        return <div>Loading...</div>;
    }

  console.log(user._id);
  // Profile component
return (
    <div className="user-container">
      <div className="user-card">
        <div className="user-header">
          <div className="user-image-container">
            <img 
              src={`${API_URL}/${user.profilePicture}`} 
              alt="Profile" 
              className="user-image" 
            />
          </div>
          <div className="user-title">
            <h1>Profile</h1>
          </div>
        </div>

        <div className="user-info">
  <div className="user-info-item">
    <label>Username</label>
    <p>{user.username}</p>
  </div>
  
  <div className="user-info-item">
    <label>Email</label>
    <p>{user.email}</p>
  </div>
  
  <div className="user-info-item">
    <label>Role</label>
    <p className={user.role === 'admin' ? 'user-admin-role' : 'user-regular-role'}>
      {user.role === 'admin' ? 'Administrator' : 'User'}
    </p>
  </div>
</div>
      </div>
    </div>
);
};

export default Profile;