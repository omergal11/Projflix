import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import {authServices} from "../../services/services";
import "./NavBar.css";

const NavBar = () => {
  const [scrolled, setScrolled] = useState(false);
  const [lightMode, setLightMode] = useState(false);
  const [query, setQuery] = useState(""); 
  const [dropdownOpen, setDropdownOpen] = useState(false); 
  const [isAdmin, setIsAdmin] = useState(false);
  const [user, setUser] = useState(null);
  const navigate = useNavigate();
  const { query: urlQuery } = useParams();
  const profilePicture = user ? user.profilePicture : null;

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 50);
    };
    const token = localStorage.getItem("jwtToken");
    if (token) {
      try { 
        const decodedToken = jwtDecode(token);
        if (decodedToken.role === "admin") {
          setIsAdmin(true);
        }
      } catch (error) {
        console.error("Error decoding token:", error);
      }
    }

    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);
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
  } ,); // Empty dependency array

  const toggleLightMode = () => {
    setLightMode(!lightMode);
    document.body.classList.toggle("light-mode");
  };

  const handleHomePage = () => {
    navigate("/homePage");
  };

  const handleMoviePage = () => {
    navigate("/moviesPage");
  };

  const handleSearchChange = (event) => {
    const newQuery = event.target.value;
    setQuery(newQuery);

    if (newQuery.trim()) {
      navigate(`/search/${newQuery}`);
    } else {
      navigate(-1);
    }
  };
  const handleLogout = () => {
    localStorage.removeItem("jwtToken");
    navigate("/login");
  }
  const handleAdminArea = () => {
    navigate("/admin");
  };
  const handleProfile = () => {
    navigate("/profile");
  }

  return (
    <nav
      className={`navbar navbar-expand-lg fixed-top ${
        scrolled ? "bg-black" : "bg-transparent"
      }`}
    >
      <div className="container-fluid">
        {/*  projflix logo*/}
        <a className="navbar-brand" href="#">
          <img
            src="/projflix_logo.svg"
            alt="Projflix Logo"
            height="40"
          />
        </a>

        {/* menu */}
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0 align-items-center">
            <li className="nav-item">
              <button
                className="nav-link btn btn-link"
                onClick={handleHomePage}
                style={{ textDecoration: "none", color: "inherit" }}
              >
                Home
              </button>
            </li>
            <li className="nav-item">
              <button
                className="nav-link btn btn-link"
                onClick={handleMoviePage}
                style={{ textDecoration: "none", color: "inherit" }}
              >
                Movies
              </button>
            </li>
            {/* admin */}
            {isAdmin && (
              <li className="nav-item">
                <button
                  className="nav-link btn btn-link"
                  onClick={handleAdminArea}
                  style={{ textDecoration: "none", color: "inherit" }}
                >
                  AdminArea
                </button>
              </li>
            )}
          </ul>

          {/* search */}
          <form className="d-flex search-form" role="search">
            <input
              className="form-control me-2 search-input"
              type="search"
              placeholder="Titles, people, genres"
              aria-label="Search"
              value={query}
              onChange={handleSearchChange}
            />
          </form>

          {/* profile menu*/}
          <div className="profile-dropdown ms-3"
            onMouseEnter={() => setDropdownOpen(true)}  
            onMouseLeave={() => setDropdownOpen(false)} 
      >
            <div className="profile-container" >
              <img
                src={`http://localhost:3001/${profilePicture}`}
                alt="Profile"
                className="profile-pic"
              />
              <span className="dropdown-nav-arrow">▼</span>
            </div>
            {dropdownOpen && (
              
              <div className="dropdown-nav-menu">
              <button className="dropdown-nav-item" onClick={handleProfile}>Personal Area</button>
             <button className="dropdown-nav-item" onClick={handleLogout}>Log Out</button>
              </div>
            )}
          </div>

          <button
            className="btn btn-outline-secondary light-mode-btn ms-3"
            onClick={toggleLightMode}
          >
            {lightMode ? "Dark Mode" : "Light Mode"}
          </button>
        </div>
      </div>
    </nav>
  );
};

export default NavBar;
