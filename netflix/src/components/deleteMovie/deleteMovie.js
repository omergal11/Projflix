import React, { useState } from "react";
import SearchBox from "../searchBox/searchBox";
import { movieServices } from "../../services/services";

const DeleteMovie = () => {
  const [movies, setMovies] = useState([]); 
  const [selectedMovie, setSelectedMovie] = useState(null); 
  const [error, setError] = useState(""); 
  const [success, setSuccess] = useState(""); 

 // Search for movies
  const handleSearch = async (query) => {
    try {
      const response = await movieServices.searchMovies(query);

      if (!response.ok) {
        throw new Error("Failed to fetch search results.");
      }

      const data = await response.json();
      setMovies(data); 
    } catch (error) {
      console.error("Error searching movies:", error);
      window.alert("Failed to search movies.");
    }
  };

 // Select a movie
  const handleMovieSelect = (name) => {
    const movie = movies.find((m) => m.name === name);
    if (movie) {
      setSelectedMovie(movie); 
      setError(""); 
    }
  };

  // Delete selected movie
  const handleDelete = async () => {
    if (!selectedMovie) {
      setError("Please select a movie first.");
      return;
    }

    try {
      const response = await movieServices.deleteMovie(selectedMovie._id); 
      if (response.status !== 204) {
        const responseError = await response.json();
        throw new Error(responseError.error || "Failed to delete movie.");
      }

      window.alert(`Movie "${selectedMovie.name}" deleted successfully!`);
      setMovies([]); 
      setSelectedMovie(null);
    } catch (err) {
      window.alert(err.message);
      setSuccess("");
    }
  };
 // Display delete movie form
  return (
    <div className="container">
      <h2 className="heading">Delete Movie</h2>
      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}

   
      <SearchBox
        data={movies.map((movie) => movie.name)} 
        onSearch={handleSearch} 
        onSelect={handleMovieSelect} 
      />

   
      {selectedMovie && (
        <div className="delete-section">
          <button className="button delete-button" onClick={handleDelete}>
            Delete Movie
          </button>
        </div>
      )}
    </div>
  );
};

export default DeleteMovie;
