const User = require('../models/User');
const jwt = require('jsonwebtoken'); 
const validateUserData = async (email, password) => {
    if (!email) 
        return { error: 'email is requiered', status: 400 };
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.(com|co\.il|org|net|edu)$/;
    if (!emailRegex.test(email)) {
        return { error: 'Please provide a valid email address in the correct format', status: 400 };
    }
    // Check if password is a string and not empty
    if (!password ) 
        return { error: 'Password is requiered', status:400 };
     // Ensure the password is at least 8 characters long 
    if (password.length < 8) {
        return { error: 'Password must be at least 8 characters long', status: 400 };
    }
    // Ensure the password contains at least one uppercase letter, one lowercase letter, one number, and one special character
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (!passwordRegex.test(password)) {
        return { 
            error: 'Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character', 
            status: 400 
        };
    }
    
    // Check if email already exists
    const existingUser = await User.findOne({ 
        email: { $regex: new RegExp('^' + email + '$', 'i') }  // i for case-insensitive search
        });
    if (existingUser) {
        return { error: 'Email already exists',status:400 };
    }

    return null;  // Return null if all validations passed
};


const createnewUser = async (email, password, profilePicture,username) => {
    // Validate user data
    const validationError = await validateUserData(email, password);
    if (validationError) return validationError;
    // Create a new user
    try{
        const newUser = new User({
            email: email,
            password: password,
            profilePicture: profilePicture,
            username: username,
            watchedMovies: [],
        });
        await newUser.save();
        return newUser;  
    }
    catch(error){
        return {error : error.message , status : 400}
    }
};

const getUserById = async (_id) => { 
    // get the user by id
    try {
    const user = await User.findById(_id);
    // Check if the user exists
    if(!user){
        return { error: 'User not found' , status: 404};
    }
    return user;
} catch (error) {
    return { error: 'User id is not in the right format' , status: 400};
}
};
const getCleanUserData = (user) => {
    // Remove sensitive data (password) before sending the user object
    const { userId,password, ...cleanUserData } = user.toObject();
    return cleanUserData;
};
const signInUser = async (email, password) => {
    // Validate user data
    const valid = await validateUserData(email, password);
    if (valid) {
        if (valid.error != "Email already exists") {
            return valid;
        }
    }

    // Check if the user exists
    const existingUser = await User.findOne({ 
        email: { $regex: new RegExp('^' + email + '$', 'i') }  // i for case-insensitive search
    });
    if (!existingUser) {
        return { error: 'User not found', status: 404 };
    }

    // Check if the password is correct
    const pass = existingUser.password == password;
    if (!pass) {
        return { error: 'Invalid password', status: 400 };
    }

    // Create a token  
    const secretKey = process.env.JWT_SECRET;  // Secret key
    const token = jwt.sign(
        { userId: existingUser._id, email: existingUser.email , role: existingUser.role},
        secretKey,
    );

    // Return the token
    return { token };
};
const getUserIdByMdbId = async (mongoId) => {
    // Get the user by id
    const user = await getUserById(mongoId);
    if (user.error) {
        return user;
    }
    return user.userId;
};
// Check if the user is authorized
const checkUserAuthorization = async (_id) => {
    // Get the user by id
    const user = await getUserById(_id);
    if (user.error) {
        return {error: 'User not authenticated',status: 401};
    }
    return user;
};
const addMovieToWatchedMovies = async (id, movieId) => {
    const user = await getUserById(id);  // Get user by ID
    if (user.error) {
        return user;  // Return error if user not found
    }
    if (!user.watchedMovies.includes(movieId)) {  // Check if movie already in watched list
        user.watchedMovies.push(movieId);  // Add movie if not already present
        await user.save();  // Save updated user
    }
    return user;  // Return updated user
};
const deleteMovieFromWatchedMovies = async (MdbUserId, MdbMovieId) => {
    const { executeToRecommendServer } = require('./recommendService');
    const { getMovieIdByMdbId } = require('./movie');
    
    const user = await getUserById(MdbUserId);  // Get user by ID
    if (user.error) {
        return user;  // Return error if user not found
    }
    
    const movieIndex = user.watchedMovies.indexOf(MdbMovieId);  // Find movie index in watched list
    if (movieIndex === -1) {
        return { error: 'Movie not found in watched movie', status: 404 };  // Return error if not found
    }

    user.watchedMovies.splice(movieIndex, 1);  // Remove movie from watched list
    const movieId = await getMovieIdByMdbId(MdbMovieId);  // Get movie ID
    const userId = await getUserIdByMdbId(MdbUserId);  // Get user ID
    await executeToRecommendServer('DELETE', userId, movieId);  // Update recommendation server
    await user.save();  // Save updated user

    return user;  // Return updated user
};
module.exports = { createnewUser,
     getUserById,
      getCleanUserData,
       signInUser,
        getUserIdByMdbId,
        checkUserAuthorization,
        addMovieToWatchedMovies,deleteMovieFromWatchedMovies};
