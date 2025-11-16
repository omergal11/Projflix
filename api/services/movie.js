const fs = require('fs');
const path = require('path');
const Movie = require('../models/movie');
const validateMovieData = async (name, minutes, description, releaseYear, categories, mainImage,movieFile) => {
    const { findCategoryByName } = require('./category');
    // Check if required fields are present
    if (name == undefined) return { error: "name is required", status: 400 };
    if (minutes === undefined) return { error: "minutes is required", status: 400 };
    if (!description) return { error: "description is required", status: 400 };
    if (!releaseYear) return { error: "release year is required", status: 400 };
    if (!mainImage) return { error: "main image is required", status: 400 };
    if (!movieFile) return { error: "movie file is required", status: 400 };
    if (!categories || categories.length === 0) return { error: "at least one category is required", status: 400 };
    
    console.log(categories);
    // Check for duplicate categories
    const uniqueCategories = [...new Set(categories)];
    console.log(uniqueCategories);
    if (uniqueCategories.length !== categories.length) return { error: "Categories cannot contain duplicates", status: 400 };
    
    // Validate categories exist
    const invalidCategories = [];
    for (let categoryName of categories) {
        const category = await findCategoryByName(categoryName);
        if (!category) invalidCategories.push(categoryName);
    }
    if (invalidCategories.length > 0) {
        return { error: `Categories not found: ${invalidCategories.join(", ")}`, status: 400 };
    }

    // Check if a movie with the same name exists
    const existingMovie = await Movie.findOne({ 
        name: { $regex: new RegExp('^' + name + '$', 'i') }  // i for case-insensitive search
    });
    if (existingMovie) return { error: "Movie with same name already exists", status: 400 };

    return null;  // All validations passed
};
const addMovieToCategories = async (categories, movieId) => {
    const { addMovieToCategory } = require('./category');
    
    // Loop through each category and add the movie
    for (let categoryId of categories) {
        const result = await addMovieToCategory(categoryId, movieId);
        if (result.error) {
            return { error: result.error, status: result.status };  // Return error if adding to any category fails
        }
    }
    return { status: 200 };  // Return success status if all categories are updated
};
const createMovie = async (name, minutes, description, releaseYear, categories, mainImage, director, cast, trailer,movieFile) => {
    const { replaceNameToId } = require('./category');
    // Validate movie data
    const validationError = await validateMovieData(name, minutes, description, releaseYear, categories, mainImage,movieFile);
    if (validationError) return validationError;  // Return validation error if any

    // Replace category names with IDs
    const categoriesIds = await replaceNameToId(categories);
    if (categoriesIds.error) return categoriesIds;  // Return error if categories are invalid

    // Create new movie document
    const movie = new Movie({
        name, minutes, description, releaseYear,  categories: categoriesIds, 
        mainImage, director, cast, trailer,movieFile
    });

    try {
        // Save movie and update categories with movie reference
        await movie.save();
        await addMovieToCategories(categoriesIds, movie._id);
        return movie;  // Return the created movie
    } catch (error) {
        return { error: error.message, status: 500 };  // Return error if movie creation fails
    }
};
const getMovieById = async (id) => {
    try {
        // Attempt to find movie by ID
        const movie = await Movie.findById(id);
        if (!movie) {
            return { error: 'Movie not found', status: 404 };  // Return 404 if movie is not found
        }
        return movie;  // Return the movie if found
    } catch (error) {
        return { error: 'Movie id is not in the right format', status: 400 };  // Return error if ID format is invalid
    }
};
const deleteMoviefromCategories = async (categories, movieId) => {
    const { deleteMovieFromCategory } = require('./category');
    
    // Loop through each category and remove the movie
    for (let categoryId of categories) {
        const result = await deleteMovieFromCategory(categoryId, movieId);
        if (result.error) {
            return { error: result.error, status: result.status };  // Return error if any deletion fails
        }
    }
    return { status: 200 };  // Return success status if movie is removed from all categories
};
const updateMovie = async (name, id, minutes, description, releaseYear, categories, mainImage, director, cast, trailer,movieFile) => {
    const { replaceNameToId } = require('./category');
    // Validate movie ID
    if (!id) return { error: "id is required", status: 400 };

    // Fetch existing movie
    const updatedMovie = await getMovieById(id);
    if (updatedMovie.error) return updatedMovie;  // Return error if movie not found

    // Validate updated movie data
    const validationError = await validateMovieData(name, minutes, description, releaseYear, categories, mainImage,movieFile);
    if (validationError) {
        if (validationError.error !== 'Movie with same name already exists' || 
            (validationError.error === 'Movie with same name already exists' && updatedMovie.name.toLowerCase() !== name.toLowerCase())) {
            return validationError;  // Return validation error if needed
        }
    }
   

    // Update movie properties
    updatedMovie.name = name;
    updatedMovie.minutes = minutes;
    updatedMovie.description = description;
    updatedMovie.releaseYear = releaseYear;
    updatedMovie.mainImage = mainImage;
    updatedMovie.director = director;
    updatedMovie.cast = cast;
    updatedMovie.trailer = trailer;
    updatedMovie.movieFile = movieFile;

    // Update categories
    await deleteMoviefromCategories(updatedMovie.categories, updatedMovie._id);
    const categoriesIds = await replaceNameToId(categories);
    updatedMovie.categories = categoriesIds;
    await addMovieToCategories(updatedMovie.categories, updatedMovie._id);

    try {
        // Save updated movie and return status
        await updatedMovie.save();
        return { updatedMovie, status: 204 };
    } catch (error) {
        return { error: error.message, status: 500 };  // Return error if save fails
    }
};
const getCleanMovieData = async (movie) => {
    const { getCategoriesNames } = require('./category'); 
    if (movie) {
        const movieData = movie.toObject ? movie.toObject() : movie;

       
        delete movieData.viewedBy; 
        delete movieData.movieId;
      

        const categories = await getCategoriesNames(movieData.categories); 
        movieData.categories = categories;
        return movieData;  // Return cleaned movie data
    }
    return {};  // Return empty object if no movie is found
};

const getCleanMovieById = async (id) => {
    const movie = await getMovieById(id);  // Fetch movie by ID
    if (movie.error) return movie;  // Return error if movie not found

    const cleanMovie = await getCleanMovieData(movie);  // Clean the movie data
    return cleanMovie;  // Return the cleaned movie data
};
const addUserToViewdBy = async (movieId, userId) => {
    const movie = await getMovieById(movieId);  // Fetch movie by ID
    if (!movie) {  
        return { error: 'user not found', status: 404 };  // Movie not found
    }
    if (!movie.viewedBy.includes(userId)) {  // If user is not already in the list
        movie.viewedBy.push(userId);  // Add user to the list
        await movie.save();  // Save the updated movie
    }
    return movie;  // Return updated movie
};
const getMovieIdByMdbId = async (id) => {
    const movie = await getMovieById(id);  // Fetch movie by ID
    if (movie.error) return movie;  // Return error if movie not found
    if (!movie) {  // Movie not found
        return { error: 'Movie not found', status: 404 };
    }
    return movie.movieId;  // Return the movie ID
};
const getMoviesByIds = async (movieIds) => {
    
    try {
        // Convert movie IDs to numbers
        const idsAsNumbers = movieIds.map((id) => Number(id));
        // Find movies by IDs
        const movies = await Movie.find({ movieId: { $in: idsAsNumbers } });
        // Clean movie data
        const cleanedMovies = await getCleanMoviesData(movies)
        return cleanedMovies;
    } catch (err) {
        throw err;
    }
};
// Function to search for movies based on a query
const searchMovie = async(query) => {
    try {
        // Search for movies in the database using the query
        // The $or operator allows matching on multiple fields
        // $regex enables partial and case-insensitive matching
        const movies = await Movie.find ({
            $or : [
                {name: {$regex : query, $options: 'i' }},       
                {description : {$regex: query, $options: 'i'}}, 
                {director : {$regex: query, $options: 'i'}},    
                {cast : {$regex: query, $options: 'i'}}         
            ]
        });

        // Clean up the movie data using a helper function
        const cleanMovies = await getCleanMoviesData(movies);

        // Return the cleaned movies data
        return cleanMovies;
    } catch (error) {
        throw error;
    }
};
const getCleanMoviesData = async (movies) => {
    // Process each movie asynchronously and clean its data
    const cleanedMovies = await Promise.all(movies.map(async (movie) => { 
        return await getCleanMovieData(movie); 
    }));

    // Return the cleaned movies array
    return cleanedMovies;
};
// Fetches movies from promoted categories for a user
const getMoviesFromPromotedCategories = async (userId) => {
    try {
        // Get promoted categories
        const { get20RandomMovies } = require('./category');
        const { findPromotedcategories } = require('./category');
        const categories = await findPromotedcategories();
        const movies = [];

        // Loop through categories and fetch 20 random movies per category
        if (categories && categories.length > 0) {
            for (let category of categories) {
                const result = await get20RandomMovies(category._id, userId);
                if (result) {
                    movies.push(result);  // Add results to movies array
                }
            }
        }
        return movies;  // Return the list of movies
    } catch (error) {
        // Return error if there's a problem fetching the movies
        return { error: error.message , status: 404 };
    }
};

// Fetches 20 random movies from a specific category that the user hasn't watched
const getMoviesByCategoryAndNotWatched = async (categoryId, watchedMovies) => {
    // Aggregate query to fetch movies from the category, excluding watched movies
    const movies = await Movie.aggregate([
        { $match: { categories: categoryId } },  // Filter by category
        { $match: { _id: { $nin: watchedMovies } } },  // Exclude watched movies
        { $sample: { size: 20 } }  // Randomly select 20 movies
    ]);
    return movies;  // Return selected movies
};

// Fetches the last 20 watched movies for a user
const getLastWatchedMovies = async (userId) => {
    const { getUserById } = require('./user');
    try {
        // Fetch user data by ID
        const user = await getUserById(userId);
        if (user.error) {
            return user;  // Return user error if user is not found
        }

        // If user has watched movies, fetch the last 20 watched
        if (user.watchedMovies && user.watchedMovies.length > 0) {
            const last20WatchedMovieIds = user.watchedMovies.slice(-20);

            // Aggregate query to fetch the last 20 watched movies by their IDs
            const watchedMovies = await Movie.aggregate([
                { $match: { '_id': { $in: last20WatchedMovieIds } } },  // Match movie IDs
                { $sample: { size: last20WatchedMovieIds.length } }  // Randomly sample the last 20
            ]);
             // Clean the final list of movies
            const cleanedMovies = await getCleanMoviesData(watchedMovies);
            return {category: "watched movies" ,movies: cleanedMovies };  // Return categroy name and the last 20 watched movies
        }
        return [];  // Return empty array if no watched movies
    } catch (error) {
        // Return error if there's an issue fetching the watched movies
        return { error: 'Error fetching last watched movies: ', status: 404 };
    }
};
const getMovies = async (userId) => {
    try {
        const allMovies = [];  // Initialize array to store all movie results

        // Fetch movies from promoted categories
        const moviesFromCategory = await getMoviesFromPromotedCategories(userId);
        if (moviesFromCategory.error) {
            return moviesFromCategory;  // Return error if movies from categories fail
        }
        allMovies.push(...moviesFromCategory);  // Add category movies to allMovies

        // Fetch the user's last watched movies
        const watchedMovies = await getLastWatchedMovies(userId);
        if (watchedMovies.error) {
            return watchedMovies;  // Return error if watched movies fetch fails
        }
        allMovies.push(watchedMovies);  // Add watched movies to allMovies

        //return the final list of movies
        return allMovies;

    } catch (error) {
        return { error: error.message, status: 500 };  // Return error if any issues occur
    }
};
const deleteMovie = async (id) => {
    const { deleteMovieFromCategory } = require('./category');  // Importing the category service
    const { deleteMovieFromWatchedMovies } = require('./user');  // Importing the user service

    const movie = await getMovieById(id);  // Get movie by its ID
    if (movie.error) {
        return movie;  // If movie not found, return error
    }

    const categories = movie.categories;  // Get the categories the movie belongs to
    for (let categoryId of categories) {
        await deleteMovieFromCategory(categoryId, id);  // Remove movie from each category
    }

    const users = movie.viewedBy;  // Get users who have viewed the movie
    for (let userId of users) {
        await deleteMovieFromWatchedMovies(userId, id);  // Remove movie from each user's watched list
    }
    await Movie.deleteOne({ _id: id });  // Delete the movie from the database
    return movie;  // Return the deleted movie
};
const deleteCategoryFromMovie = async (movieId, categoryId) => {
    // Fetch movie by ID
    const movie = await getMovieById(movieId);
    if (!movie) {
        return { error: "Movie not found", status: 404 }; 
    }
    // Check if category exists in movie
    const categoryIndex = movie.categories.indexOf(categoryId);
    if (categoryIndex === -1) {
        return { error: "Category not found in movie", status: 404 };
    }
    // Remove category from movie
    movie.categories.splice(categoryIndex, 1);
        try {
            // Save the updated movie
         if (movie.categories.length === 0) {
            // If no categories left, delete the movie
            await deleteMovie(movieId);
            return { message: "movie deleted", status:204};
        }
        else {
            // Save the updated movie
             await movie.save();
             return { movie, status: 204 };
           }
     } catch (error) {
         return { error: error.message, status: 500 };
    }
};
const getMoviesDetails = async (movieIds) => {
    try {
        // Find movies by IDs
        const movies = await Movie.find({
            '_id': { $in: movieIds }
        }).select('name _id mainImage'); // Select only the name, _id, and mainImage fields

        // Return error if no movies are found
        if (!movies.length) {
            return [];
        }

        // Return the cleaned movie data
        return movies.map(movie => ({
            _id: movie._id,  // Rename _id to id
            name: movie.name,
            mainImage: movie.mainImage
        }));
    } catch (error) {
        return { error: "Invalid movie ids format", status: 400 };
    }
};

// Export functions
module.exports = { createMovie, updateMovie ,getCleanMovieById,getMovieById,addUserToViewdBy, getMovieIdByMdbId,getMoviesByIds 
    ,searchMovie , getCleanMoviesData ,getMoviesByCategoryAndNotWatched,getMovies,deleteMovie,deleteCategoryFromMovie,getMoviesDetails
};