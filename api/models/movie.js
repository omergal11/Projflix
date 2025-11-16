const mongoose = require('mongoose');

// Define Movie schema
const Schema = mongoose.Schema;

// Movie schema definition
const Movie = new Schema({
    name: { type: String, required: true, unique: true },  // Movie name (unique)
    minutes: { type: String, required: true },  // Duration in minutes
    description: { type: String, required: true },  // Description of the movie
    releaseYear: { type: Number, required: true },  // Year of release
    cast: [{ type: String }],  // Array of cast members
    director: { type: String },  // Director name (optional)
    trailer: { type: String },  // Trailer URL (optional)
    mainImage: { type: String, required: true },  // Main image URL
    categories: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Category', required: true }],  // References to categories
    movieId: { type: Number, unique: true },  // Unique movie ID
    viewedBy: [{ type: mongoose.Schema.Types.ObjectId, ref: 'User' }],  // Users who viewed the movie
    movieFile: { type: String, required: true } // movie file
}, { versionKey: false });  // Disable versioning

// Pre-save hook to assign unique movieId
Movie.pre('save', async function (next) {
    if (this.isNew) {
        try {
            const lastMovie = await this.constructor.findOne().sort({ movieId: -1 });
            this.movieId = lastMovie ? lastMovie.movieId + 1 : 100;  // Increment or start at 100
        } catch (error) {
            return next(error);  // Pass error to next middleware
        }
    }
    next();  // Proceed with saving
});

// Export the model
module.exports = mongoose.model('Movie', Movie);