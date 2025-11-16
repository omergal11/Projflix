import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './SimilarMovies.css';

const SimilarMovies = ({ movieId }) => {
  const [movies, setMovies] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const token = localStorage.getItem('jwtToken');
  const navigate = useNavigate();
  const API_URL = process.env.REACT_APP_API_URL;
   // handlePlay function
  const handlePlay = (movie) => {
    navigate("/videoPlayer", { state: { videoSrc: movie.movieFile, movieId: movie._id } });
  };
  // fetch similar movies
  useEffect(() => {
    const fetchSimilarMovies = async () => {
      try {
        const response = await fetch(`${API_URL}/api/movies/${movieId}/recommend`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          const message =  await response.json();
          throw new Error(message.error );
        }

        const data = await response.json();
        setMovies(data);
      } catch (err) {
        console.error(err.message);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchSimilarMovies();
  }, [movieId, token]);

  if (loading) {
    return <p>Loading similar movies...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }
 // check if movies is an array and not empty
  if (!Array.isArray(movies) || movies.length === 0) {
    return <p>No similar movies available.</p>;
  }
  // display similar movies
  return (
    <div className="similar-movies-container">
      <h3 className="section-s-title">More Like This</h3>
      <div className="movie-s-grid">
        {movies.map((movie, index) => (
          <div key={index} className="movie-s-card">
            <div className="movie-s-image-container">
              <img 
                src={`${API_URL}/${movie.mainImage}`} 
                alt={movie.name} 
                className="movie-s-image"
              />
              <span className="movie-s-duration">{movie.minutes}</span>
            </div>
            <div className="movie-s-info">
              <h5 className="movie-s-title">{movie.name}</h5>
              <div className="movie-s-meta">
                <span className="hd">HD</span>
                <span className="release-s-year">{movie.releaseYear}</span>
                <button className="add-s-button" onClick={() => handlePlay(movie)}>
                  ▶
                </button>
              </div>
              <p className="movie-s-description">
                {movie.description}
              </p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SimilarMovies;
