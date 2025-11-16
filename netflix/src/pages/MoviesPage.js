import React, { useEffect, useState } from "react";
import MovieList from '../components/movielist/MovieList.js';
import 'bootstrap/dist/css/bootstrap.min.css';
import HomeVideoPlayer from '../components/HomeVideoPlayer/HomeVideoPlayer.js';
import { categoryServices, movieServices } from '../services/services.js';


const MoviesPage = () => {
  const [categories, setCategories] = useState([]);
  const [homeMovie, setHomeMovie] = useState(null);

  useEffect(() => {
    const fetchMovieData = async () => {
      try {
        // 1. Get all categories with movies
        const data = await categoryServices.getAllCategories();
        setCategories(data);

        // 2. Flatten all movies to get IDs
        const allMovieIds = data.reduce((acc, category) => {
          if (Array.isArray(category.movies)) {
            return [...acc, ...category.movies.map(movie => movie._id)];
          }
          return acc;
        }, []);

        if (allMovieIds.length > 0) {
          // 3. Select random movie ID
          const randomIndex = Math.floor(Math.random() * allMovieIds.length);
          const randomId = allMovieIds[randomIndex];

          // 4. Fetch specific movie details
          const movieDetails = await movieServices.getMovieById(randomId);
          setHomeMovie(movieDetails);
        }
      } catch (error) {
        console.error('Error fetching movie data:', error);
      }
    };

    fetchMovieData();
  }, []);

  if (!homeMovie) {
    return <div>Loading...</div>;
  }
  
  return (
    <div className="App">
      <body>
      <HomeVideoPlayer Movie={homeMovie}/>
      <div className="movie-lists">
        {categories.map((category,index) => (
        <MovieList key={index} category={category} />
        ))}
      </div>
      </body>
    </div>
  );
};

export default MoviesPage;
