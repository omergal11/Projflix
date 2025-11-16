import React, { useState, useEffect } from "react";
import SearchList from "../components/searchList/searchList";
import { useParams } from "react-router-dom";
import { movieServices } from '../services/services';
import "./searchPage.css";
// SearchPage 
const SearchPage = () => {
  const [movies, setMovies] = useState([]);
  const [error, setError] = useState(null);
  const { query } = useParams();
  // fetch movies
  useEffect(() => {
    const fetchMovies = async () => {
      try {
        const data = await movieServices.searchMovies(query);
        if (!data.ok) {
          throw new Error(data.error);
        }
        const dataM = await data.json();
        setMovies(dataM);
      } catch (error) {
        console.error('Error searching movies:', error);
        setError(error.message);
      }
    };

    fetchMovies();
  }, [query]);
    // group movies by five
  const groupMoviesByFive = (movies) => {
    const grouped = [];
    for (let i = 0; i < movies.length; i += 7) {
      grouped.push(movies.slice(i, i + 7));
    }
    return grouped;
  };
   

  const groupedMovies = groupMoviesByFive(movies); 
  
// return search page
  return (
    <div className="search-page">
      <h1>Search Results</h1>
      {movies.length > 0 ? (
        groupedMovies.map((group, index) => (
          <div key={index} className="search-list-wrapper">
            <SearchList movies={group} />
          </div>
        ))
      ) : (
        <p>No results found for "{query}"</p>
      )}
    </div>
  );
};

export default SearchPage;
