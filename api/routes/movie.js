const express = require('express');
const router = express.Router();
const movieController = require('../controllers/movie');
const recommendController = require('../controllers/recommend');
const upload = require('../middlewares/upload');

// Configure upload fields
const movieUploadFields = [
    { name: 'mainImage', maxCount: 1 },
    { name: 'trailer', maxCount: 1 },
    { name: 'movieFile', maxCount: 1 }
];

// Main movie routes
router.route('/')
    .get(movieController.getMovies)
    .post(upload.fields(movieUploadFields), movieController.createMovie);

// Single movie routes
router.route('/:id')
    .get(movieController.getMovie)
    .put(upload.fields(movieUploadFields), movieController.updateMovie)
    .delete(movieController.deleteMovie);

// Recommendation routes
router.route('/:id/recommend')
    .get(recommendController.getRecommendations)
    .post(recommendController.addRecommendation);

// Search route
router.get('/search/:query', movieController.searchMovie);

module.exports = router;