import React, {useEffect, useState} from 'react';
import MovieList from '../components/movielist/MovieList.js';
import 'bootstrap/dist/css/bootstrap.min.css';
import HomeVideoPlayer from '../components/HomeVideoPlayer/HomeVideoPlayer.js';
import { movieServices } from '../services/services.js'; 
import '../App.js';

function HomePage() {
  const [categories, setCategories] = useState([]);
  const [homeMovie, setHomeMovie] = useState(null);

  useEffect(() => {
    const fetchMovies = async () => {
      try {
        const data = await movieServices.getAllMovies();
        if (Array.isArray(data)) {
          setCategories(data);
          
          // Flatten all movies from all categories into single array
          const allMovies = data.reduce((acc, category) => {
            if (Array.isArray(category.movies)) {
              return [...acc, ...category.movies];
            }
            return acc;
          }, []);

          if (allMovies.length > 0) {
            // Select random movie from flattened array
            const randomIndex = Math.floor(Math.random() * allMovies.length);
            setHomeMovie(allMovies[randomIndex]);
          }
        }
      } catch (error) {
        console.error('Error fetching movies:', error);
      }
    };

    fetchMovies();
  }, []);

  if (!homeMovie) {
    return <div>Loading...</div>;
  }
  // Return the home page
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
}

export default HomePage;