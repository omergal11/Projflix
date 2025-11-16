// MedMovie.js (Complete)
import React, { useState, useEffect, use } from "react";
import { useNavigate } from "react-router-dom";
import MainMovie from "../mainMovie/mainMovie";
import Popup from "../popup/Popup";
import "./medMovie.css";

// MedMovie component to display medium size movie 
const MedMovie = ({ movie, a }) => {
  const API_URL = process.env.REACT_APP_API_URL;
  // useState hooks
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const navigate = useNavigate();
  // handleDetailsClick function
  const handleDetailsClick = () => {
    const medPopup = document.querySelector('.med-movie-popup');
    if (medPopup) {
      medPopup.style.display = 'none';
    }
    setIsPopupOpen(true);
  };
  // handlePlay function
  const handlePlay = () => {
    navigate("/videoPlayer", { state: { videoSrc: movie.movieFile, movieId: movie._id } });
  };
// handleClose function
  const handleClose = () => {
    if (a) {
      a ();
    }
    setIsPopupOpen(false);
  };
  
  const categories = Array.isArray(movie.categories) ? movie.categories : [];
  const formattedCategories = categories.map((category) => category.name).join(" • "); 
   
// display MedMovie component
  return (
    <>
      <div className="movie-details">
        <video
          src={`${API_URL}/${movie.trailer}`}
          className="movie-details-trailer"
          autoPlay
          muted
          loop
          playsInline
        ></video>

        <div className="movie-actions">
          <button className="action-button play" onClick={handlePlay}>
            ▶
          </button>
          <button className="action-button" onClick={handleDetailsClick}>
          <i className="bi bi-chevron-compact-down"></i>
          </button>
        </div>

        <div className="movie-meta">
          <span className="duration">{movie.minutes}</span>
        </div>

        <div className="movie-tags">
          <span className="tags">{formattedCategories}</span>
        </div>
      </div>

      <Popup isOpen={isPopupOpen} onClose={handleClose}>
        <MainMovie movie={movie} similarMovies={null} />
      </Popup>
    </>
  );
};

export default MedMovie;