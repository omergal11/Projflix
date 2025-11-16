import React, {useState} from "react";
import { useNavigate } from "react-router-dom";
import "./mainMovie.css";
import SimilarMovies from "../similarMovies/SimilarMovies";
 // MainMovie component to show all movie details
const MainMovie = ({ movie }) => {
  const [isPlaying, setIsPlaying] = useState(false);
  const navigate = useNavigate();
  const categories =Array.isArray(movie.categories) ? movie.categories : [];
  const castArr = Array.isArray(movie.cast) ? movie.cast : [];
  const API_URL = process.env.REACT_APP_API_URL;
  // handlePlay function
  const handlePlay = () => {
    setIsPlaying(true);
    navigate("/videoPlayer", { state: { videoSrc: movie.movieFile, movieId: movie._id } });
  }
  // display MainMovie component
  return (
      <div className="main-container">
        <div className="movie-trailer-container">
        <div className="movie-trailer-container">
    <video className="movie-trailer" autoPlay muted loop>
      <source src={`${API_URL}/${movie.trailer}`} type="video/mp4" />
    </video>
    <button className="play-btn" onClick={handlePlay}>▶ Play</button>
  </div>
  </div>

    <div className="movie-info my-4">
    <h1 className="movie-title">{movie.name}</h1>
    
    <div className="movie-meta">
      <span>{movie.releaseYear}</span> | <span>{movie.minutes}</span> 
    </div>
    
    <p className="movie-description mt-3">{movie.description}</p>
    
    <div className="lower-info">
      <div className="movie-cast mt-2">
        <p>
          <strong>Cast:</strong> {castArr.join(", ")}
        </p>
      </div>
      <div className="movie-categories mt-2">
        <p>
          <strong>Categories:</strong> {categories.length > 0 ? categories.map((c) => c.name).join(", ") : "No categories available"}
        </p>
      </div>
    </div>
  </div>
      <SimilarMovies movieId={movie._id} />
      <div className="about">
        <h2>About {movie.name}</h2>
        <p>Director: {movie.director}</p>
        <p>Cast: {castArr.join(", ")}</p>
        <p>Genres: {categories.map((c) => c.name).join(", ")}</p>
      </div>
    </div>
  );
};

export default MainMovie;
