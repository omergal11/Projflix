import React, { useState, useEffect } from "react";
import TextInput from "../textInput/TextInput";
import NumberInput from "../numberInput/NumberInput";
import TextAreaInput from "../textAreaInput/TextAreaInput";
import SearchBox from "../searchBox/searchBox";
import { movieServices, categoryServices } from "../../services/services";
import FileInput from '../fileInput/FileInput';


const PutMovie = () => {
    // initial state
  const [movies, setMovies] = useState([]); 
  const [selectedMovie, setSelectedMovie] = useState(null); 
  const [name, setName] = useState(""); 
  const [minutes, setMinutes] = useState(""); 
  const [description, setDescription] = useState(""); 
  const [releaseYear, setReleaseYear] = useState("");
  const [mainImage, setMainImage] = useState("");
  const [selectedCategories, setSelectedCategories] = useState([]);
  const [categories, setCategories] = useState([]);
  const [director, setDirector] = useState("");
  const [cast, setCast] = useState("");
  const [trailer, setTrailer] = useState(""); 
  const [movieFile, setMovieFile] = useState(null);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const token = localStorage.getItem("jwtToken");
  const releaseYearNum = Number(releaseYear);

  useEffect(() => {
    // Fetch all categories
    const fetchCategories = async () => {
      try {
        const response = await categoryServices.getAllCategories();
        const data = await response.json();
        if (Array.isArray(data)) {
          setSelectedCategories(data); 
          
        } else {
          console.error("Categories data is not an array");
        }
        if (data.error) {
          throw new Error(data.error);
        }
      } catch (error) {
        console.error(error.message);
      }
    };

    fetchCategories();
  }, [token]);
  // search movies
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
      setMovies([]); 
    }
  };
 // select movie
  const handleMovieSelect = (name) => {
    const movie = movies.find((m) => m.name === name);
    if (movie) {
      setSelectedMovie(movie); 
      console.log("Selected movie:", movie);
    }
  };
  // update movie
  const handleSubmit = async (e) => {
    e.preventDefault();
    // Validate form
    if (!selectedMovie) {
      window.alert("Please select a movie first.");
      return;
    }
    // send data to the server
    try {
      const formData = new FormData();
      formData.append('name', name);
      formData.append('minutes', minutes);
      formData.append('description', description);
      formData.append('releaseYear', releaseYearNum);
      const categorArr = categories.split(',').map((cat) => cat.trim());
      console.log(categorArr);
      console.log(JSON.stringify(categorArr));
      formData.append('categories', JSON.stringify(categorArr));
      formData.append('director', director);
      const castArr = cast.split(',').map((actor) => actor.trim());
      formData.append('cast', JSON.stringify(castArr));

      if (mainImage) formData.append('mainImage', mainImage);
      if (trailer) formData.append('trailer', trailer);
      if (movieFile) formData.append('movieFile', movieFile);


      const response = await movieServices.updateMovie(selectedMovie._id, formData);
      if (response.status !== 204) {
        const responseError = await response.json();
        throw new Error(responseError.error || "Failed to update movie.");
      }

      window.alert("Movie updated successfully!");
      setError("");
    } catch (err) {
      window.alert(err.message);
      setSuccess("");
    }
  };
   // return form
  return (
    <div className="container">
      <h2 className="heading">Edit Movie</h2>
      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}

    <SearchBox
    data={movies.map((movie) => movie.name)} 
    onSearch={handleSearch} 
    onSelect={handleMovieSelect} 
    />

    <form onSubmit={handleSubmit} className="form">
    <TextInput id="name" value={name} onChange={(e) => setName(e.target.value)} label="Movie Name" required placeholder="Enter movie name ..." />
    <TextInput id="minutes" value={minutes} onChange={(e) => setMinutes(e.target.value)} label="Duration" required placeholder="Enter movie duraion ..." />
    <div className="form-row">
      <TextAreaInput id="description" value={description} onChange={(e) => setDescription(e.target.value)} label="Description" required placeholder="Enter movie description ..."  />
    <NumberInput id="releaseYear" value={releaseYear} onChange={(e) => setReleaseYear(e.target.value)} label="Release Year" required placeholder="Enter movie release year ..." /> 
    </div>
    <TextInput id="categories" value={categories} onChange={(e) => setCategories(e.target.value)} label="Categories" required placeholder=" format:(first,second,...)"  />

    <TextInput id="director" value={director} onChange={(e) => setDirector(e.target.value)} label="Director" required placeholder="Enter movie director ..." />
    <TextInput id="cast" value={cast} onChange={(e) => setCast(e.target.value)} label="Cast" required placeholder="Enter movie cast: x,y,z ..." />
    <FileInput 
        label="Main Image"
        accept="image/*"
        onChange={(e) => setMainImage(e.target.files[0])}
        required
    />

    <FileInput 
        label="Trailer (MP4)"
        accept="video/mp4"
        onChange={(e) => setTrailer(e.target.files[0])}
        required
    />

    <FileInput 
        label="Movie File (MP4)"
        accept="video/mp4"
        onChange={(e) => setMovieFile(e.target.files[0])}
        required
    />

        <button type="submit" className="button">
          Update Movie
        </button>
      </form>
    </div>
  );
};

export default PutMovie;
