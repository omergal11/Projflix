import React, { useState, useEffect } from "react";
import "./AddMovie.css";
import TextInput from "../textInput/TextInput";
import NumberInput from "../numberInput/NumberInput";
import TextAreaInput from "../textAreaInput/TextAreaInput";
import { categoryServices, movieServices } from '../../services/services';
import FileInput from '../fileInput/FileInput';
const AddMovie = () => {
  const [name, setName] = useState("");
  const [minutes, setMinutes] = useState("");
  const [description, setDescription] = useState("");
  const [releaseYear, setReleaseYear] = useState("");
  const [mainImage, setMainImage] = useState(null);
  const [selectedCategories, setSelectedCategories] = useState([]);
  const [categories, setCategories] = useState("");
  const [director, setDirector] = useState("");
  const [cast, setCast] = useState("");
  const [trailer, setTrailer] = useState(null);
  const [movieFile, setMovieFile] = useState(null);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const releaseYearNum = Number(releaseYear);
  console.log(releaseYearNum);

   // Fetch all categories
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const data = await categoryServices.getAllCategories();
        if (Array.isArray(data)) {
          setSelectedCategories(data);
        } else {
          console.error("Categories data is not an array");
        }
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };

    fetchCategories();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    // send data to server
    try {
      const formData = new FormData();
      formData.append('name', name);
      formData.append('minutes', minutes);
      formData.append('description', description);
      formData.append('releaseYear', releaseYearNum);
      const categorArr = categories.split(',').map((cat) => cat.trim());
      formData.append('categories', JSON.stringify(categorArr));
      formData.append('director', director);
      const castArr = cast.split(',').map((actor) => actor.trim());
      formData.append('cast', JSON.stringify(castArr));
      if (mainImage) formData.append('mainImage', mainImage);
      if (trailer) formData.append('trailer', trailer);
      if (movieFile) formData.append('movieFile', movieFile);
       // Add movie
      const response = await movieServices.addMovie(formData);
      if (response.status !== 201) {
        const data = await response.json();
        throw new Error(data.error);
      }
      window.alert("Movie added successfully!");
      setError("");
    } catch (error) {
      window.alert(error.message);
    }
  };
   // Add movie form
  return (
    <div className="container">
      <h2 className="heading">Add Movie</h2>
      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}
      <form onSubmit={handleSubmit}>
      <TextInput id="name" value={name} onChange={(e) => setName(e.target.value)} label="Movie Name" required placeholder="Enter movie name ..."/>
      <TextInput id="minutes" value={minutes} onChange={(e) => setMinutes(e.target.value)} label="Duration" required placeholder="Enter movie duraion ..."/>
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
          Add Movie
        </button>
      </form>
    </div>
  );
};

export default AddMovie;