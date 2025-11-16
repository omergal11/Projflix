// MovieList.js
import React, { useRef, useState, useEffect } from 'react';
import Movie from '../movie/Movie';
import './MovieList.css';

// MovieList component
const MovieList = ({category}) => {
  const movieListRef = useRef(null);
  const [showLeftButton, setShowLeftButton] = useState(false);
  const [showRightButton, setShowRightButton] = useState(true);
  const [isHovered, setIsHovered] = useState(false);
  const movies = Array.isArray(category.movies) ? category.movies : [];
  // handleScroll function
  const handleScroll = () => {
    const movieList = movieListRef.current;
    if (movieList) {
      setShowLeftButton(movieList.scrollLeft > 0);
      setShowRightButton(
        movieList.scrollLeft < movieList.scrollWidth - movieList.clientWidth
      );
    }
  };
  // scrollList function
  const scrollList = (direction) => {
    const scrollAmount = 1200;
    if (movieListRef.current) {
      movieListRef.current.scrollBy({
        left: direction === 'left' ? -scrollAmount : scrollAmount,
        behavior: 'smooth',
      });
    }
  };
    // handleMouseEnter function
  const handleMouseEnter = () => {
    setIsHovered(true);
  };
  
  const handleMouseLeave = () => {
    setIsHovered(false);
  };
// useEffect to handle scroll event
  useEffect(() => {
    const movieList = movieListRef.current;
    if (movieList) {
      movieList.addEventListener('scroll', handleScroll);
      return () => {
        movieList.removeEventListener('scroll', handleScroll);
      };
    }
  }, []);
  // if no movies, return null
  if (movies.length === 0) {
    return null;
  }
 // return list of movies
  return (
    <div className="movie-list-container">
      <h2 className="movie-list-title">{category.name || category.category}</h2>
      <div
        className="movie-list-wrapper"
        onMouseEnter={handleMouseEnter}
        onMouseLeave={handleMouseLeave}
      >
        {showLeftButton && (
          <button
            className="scroll-button left"
            onClick={() => scrollList('left')}
          >
            &#8249;
          </button>
        )}
        <div className="movie-list" ref={movieListRef}>
          {movies.map((movie, index) => (
            <Movie key={index} movie={movie} />
          ))}
        </div>
        {showRightButton && (
          <button
            className="scroll-button right"
            onClick={() => scrollList('right')}
          >
            &#8250;
          </button>
        )}
      </div>
    </div>
  );
};

export default MovieList;