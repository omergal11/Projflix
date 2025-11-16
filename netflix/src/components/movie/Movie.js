// Movie.js
import React, { useState, useEffect } from "react";
import MedMovie from "../medMovie/medMovie";
import ReactDOM from "react-dom";
import { movieServices } from "../../services/services";
import "./Movie.css";

const Movie = ({ movie }) => {
  const [isHovered, setIsHovered] = useState(false);
  const [hoverDelay, setHoverDelay] = useState(false);
  const [popupPosition, setPopupPosition] = useState({ top: 0, left: 0 });
  const [curMovie, setMovie] = useState({});
  const movieId = movie.id || movie._id;
  const API_URL = process.env.REACT_APP_API_URL;
  // useEffect to handle hover delay
  useEffect(() => {
    let timer;
    if (isHovered) {
      timer = setTimeout(() => {
        setHoverDelay(true);
      }, 500);
    } else {
      setHoverDelay(false);
    }
    return () => clearTimeout(timer);
  }, [isHovered]);
  // handleMouseEnter function
  const handleMouseEnter = (event) => {
    // getBoundingClientRect() method returns the size 
    //of an element and its position relative to the viewport.
    const rect = event.currentTarget.getBoundingClientRect();
    const popupWidth = 300;
    const appWidth = 1400;
    const padding = 20;
    
    let left = rect.left + rect.width / 2;
    
    if (left - popupWidth/2 < padding) {
      left = padding + popupWidth/2;
    } else if (left + popupWidth/2 > appWidth - padding) {
      left = appWidth - padding - popupWidth/2;
    }
    // setPopupPosition
    setPopupPosition({
      top: rect.top + 170 + window.scrollY,
      left: left
    });
    setIsHovered(true);
  };
  // handleMouseLeave function
  const handleMouseLeave = () => {
    setIsHovered(false);
  };
  // useEffect to fetch movie by id
  useEffect(() => {
    const fetchMovie = async () => {
      try {
        const data = await movieServices.getMovieById(movieId);
        if (data.error) {
          throw new Error(data.error);
        }
        setMovie(data);
      } catch (error) {
        console.error("Error fetching movie:", error);
      }
    };
  
    fetchMovie();
  }, [movieId]);
  // return movie block
  return (
    <div
      className="movie-block"
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      <img  src={`${API_URL}/${movie.mainImage}`} alt="Movie Thumbnail" className="movie-image" />

      {hoverDelay &&
        ReactDOM.createPortal(
          <div
            className="med-movie-popup"
            style={{
              top: `${popupPosition.top}px`,
              left: `${popupPosition.left}px`,
              transform: "translate(-50%, -60%)",
              zIndex: 9999,
            }}
          >
            <MedMovie movie={curMovie} a={handleMouseLeave}/>
          </div>,
          document.body
        )}
    </div>
  );
};

export default Movie;
