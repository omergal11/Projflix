const movieService = require('../services/movie');
const fs = require('fs');
const path = require('path');
const Movie = require('../models/movie');


const createMovie = async (req, res) => {
    // Define temp files
    const tempFiles = {
        mainImage: req.files?.mainImage?.[0]?.path || '',
        movieTrailer: req.files?.trailer?.[0]?.path || '',
        movieFile: req.files?.movieFile?.[0]?.path || ''
    };
    try {
        const parsedCategories = typeof req.body.categories === 'string' 
        ? JSON.parse(req.body.categories)
        : req.body.categories;

        const parsedCast = typeof req.body.cast === 'string'
            ? JSON.parse(req.body.cast)
            : req.body.cast;

        
        // Create movie with empty paths initially
        const movie = await movieService.createMovie(
            req.body.name,
            req.body.minutes,
            req.body.description,
            req.body.releaseYear,
            parsedCategories,
            tempFiles.mainImage,
            req.body.director,
            parsedCast,
            tempFiles.movieTrailer,
            tempFiles.movieFile
        );
        if (movie.error) {
            Object.values(tempFiles).flat().forEach(file => {
                if (file && fs.existsSync(file)) {
                    fs.unlinkSync(file);
                }
            });
            return res.status(movie.status).json({ error: movie.error });
        }

        // Handle main image
        if (tempFiles.mainImage) {
            const ext = path.extname(tempFiles.mainImage);
            const finalPath = `uploads/moviesMainImages/${movie._id}${ext}`;
            fs.renameSync(tempFiles.mainImage, finalPath);
            movie.mainImage = finalPath;
        }

        // Handle movie trailer
        if (tempFiles.movieTrailer) {
            const finalPath = `uploads/trailers/${movie._id}.mp4`;
            fs.renameSync(tempFiles.movieTrailer, finalPath);
            movie.trailer = finalPath;
        }

        // Handle movie file
        if (tempFiles.movieFile) {
            const finalPath = `uploads/movies/${movie._id}.mp4`;
            fs.renameSync(tempFiles.movieFile, finalPath);
            movie.movieFile = finalPath;
        }
        // Save updated paths
        await movie.save();
        
        res.status(201).location(`/api/movies/${movie._id}`).json(movie);
    } catch (error) {
        // Clean up temp files if they exist
        Object.values(tempFiles).flat().forEach(file => {
            if (file && fs.existsSync(file)) {
                fs.unlinkSync(file);
            }
        });
        
        res.status(500).json({ error: error.message });
    }
};
const updateMovie = async (req, res) => {
    const tempFiles = {
        mainImage: req.files?.mainImage?.[0]?.path,
        movieTrailer: req.files?.trailer?.[0]?.path,
        movieFile: req.files?.movieFile?.[0]?.path
    };

    const cleanupTempFiles = () => {
        Object.values(tempFiles).flat().forEach(file => {
            if (file && fs.existsSync(file)) {
                fs.unlinkSync(file);
            }
        });
    };

    try {
        // Get existing movie and paths before update
        const existingMovie = await Movie.findById(req.params.id);
        const originalPaths = {
            mainImage: existingMovie.mainImage,
            trailer: existingMovie.trailer,
            movieFile: existingMovie.movieFile,
        };

        const parsedCategories = typeof req.body.categories === 'string' 
            ? JSON.parse(req.body.categories)
            : req.body.categories;

        const parsedCast = typeof req.body.cast === 'string'
            ? JSON.parse(req.body.cast)
            : req.body.cast;

        // Try to update movie in DB
        const movie = await movieService.updateMovie(
            req.body.name, 
            req.params.id, 
            req.body.minutes, 
            req.body.description, 
            req.body.releaseYear, 
            parsedCategories,
            tempFiles.mainImage,
            req.body.director,
            parsedCast,
            tempFiles.movieTrailer, 
            tempFiles.movieFile
        );

        if (movie.error) {
            cleanupTempFiles();
            return res.status(movie.status).json({ error: movie.error });
        }

        // Update was successful, now handle files
        const updatedMovie = await Movie.findById(req.params.id);

        // Handle main image
        if (tempFiles.mainImage) {
            const newPath = `uploads/moviesMainImages/${updatedMovie._id}${path.extname(tempFiles.mainImage)}`;
            if (fs.existsSync(originalPaths.mainImage)) {
                fs.unlinkSync(originalPaths.mainImage);
            }
            fs.renameSync(tempFiles.mainImage, newPath);
            updatedMovie.mainImage = newPath;
        }
        
        // Handle trailer
        if (tempFiles.movieTrailer) {
            const newPath = `uploads/trailers/${updatedMovie._id}.mp4`;
            if (fs.existsSync(originalPaths.trailer)) {
                fs.unlinkSync(originalPaths.trailer);
            }
            fs.renameSync(tempFiles.movieTrailer, newPath);
            updatedMovie.trailer = newPath;
        }
        
        // Handle movie file
        if (tempFiles.movieFile) {
            const newPath = `uploads/movies/${updatedMovie._id}.mp4`;
            if (fs.existsSync(originalPaths.movieFile)) {
                fs.unlinkSync(originalPaths.movieFile);
            }
            fs.renameSync(tempFiles.movieFile, newPath);
            updatedMovie.movieFile = newPath;
        }
        
        // Save updated paths
        await updatedMovie.save();
        res.status(204).send();
    } catch (error) {
        cleanupTempFiles();
        res.status(500).json({ error: error.message });
    }
};
const getMovie = async (req, res) => {
    try {
        // Fetch and clean movie data by ID
        const movie = await movieService.getCleanMovieById(req.params.id);
        if (movie.error) return res.status(movie.status).json({ error: movie.error });  // Return error if movie not found
        
        res.status(200).json(movie);  // Return the cleaned movie data
    } catch (error) {
        res.status(500).json({ error: error.message });  // Return server error if an exception occurs
    }
};

const searchMovie = async(req, res) => {
    const query = req.params.query;
    if (!query)
        res.status(400).json({error: "query is required"});
    try {
        // Get movies by search 
        const search = await movieService.searchMovie(query);
        res.status(200).json(search);
    }catch (error){
        res.status(500).json({error:error.message});
    }
};
const getMovies = async (req, res) => {
    try {
        const userId = req.user.userId;
        // Get movies 
        const movies = await movieService.getMovies(userId);
        res.status(200).json(movies);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};
const deleteMovie = async (req, res) => {
    try {
        // Get movie paths before deletion
        const existingMovie = await Movie.findById(req.params.id);
        if (!existingMovie) {
            return res.status(404).json({ error: 'Movie not found' });
        }

        const filePaths = {
            mainImage: existingMovie.mainImage,
            trailer: existingMovie.trailer,
            movieFile: existingMovie.movieFile
        };

        // Delete movie from DB
        const movie = await movieService.deleteMovie(req.params.id);
        if (movie.error) {
            return res.status(movie.status).json({ error: movie.error });
        }

        // Delete associated files
        if (fs.existsSync(filePaths.mainImage)) {
            fs.unlinkSync(filePaths.mainImage);
        }
        
        if (fs.existsSync(filePaths.trailer)) {
            fs.unlinkSync(filePaths.trailer);
        }
        
        if (fs.existsSync(filePaths.movieFile)) {
            fs.unlinkSync(filePaths.movieFile);
        }
        
        
        res.status(204).send();
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};
module.exports = {createMovie,updateMovie,getMovie,searchMovie,getMovies,deleteMovie};