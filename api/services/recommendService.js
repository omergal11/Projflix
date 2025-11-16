const net = require('net');
const userService = require('./user');
const movieService = require('./movie');
const parseServerResponse = require('../parse/parseServerResponse');

const executeToRecommendServer = async (command, userId, movieId) => {
    // Get the port and IP address of the recommendation server
    const port = process.env.RECOMMENDATION_PORT;
    const ip = process.env.RECOMMENDATION_IP;
    return new Promise((resolve, reject) => {
        const client = new net.Socket();

        // connect to server
        client.connect(port, ip, () => {
            const message = `${command} ${userId} ${movieId}`; 

            client.write(message);
        });
       // get data from server
        client.on('data', (data) => {
            resolve(data.toString().trim()); 
            client.destroy(); 
        });
        // handle errors
        client.on('error', (err) => {
            reject(err);
        });
        // close connection
        client.on('close', () => {
        });
    });
};


const addRecommendation = async (MdbUserId, MdbMovieId) => {
    // Get user and movie IDs
    const userId = await userService.getUserIdByMdbId(MdbUserId); 
    const movieId =await movieService.getMovieIdByMdbId(MdbMovieId);
    // Check if the user and movie IDs are valid
    if (!userId || !movieId) {
        return { error: 'Missing user-id or movie-id' , status: 400 };
    }
    const movie = await movieService.getMovieById(MdbMovieId);
     if (movie.error)
        return movie;
        const result = await executeToRecommendServer('POST', userId, movieId);
        if (result == "404 Not Found"){
            await executeToRecommendServer('PATCH',userId, movieId);
        }
        // Add movie to watched movies and user to viewedBy
        userService.addMovieToWatchedMovies(MdbUserId,MdbMovieId);
        movieService.addUserToViewdBy(MdbMovieId,MdbUserId);
        // Return status
        return {status:200};
}

const getRecommend = async (MdbUserId, MdbMovieId) => { 
    if (!MdbMovieId) {
        return { error: 'Missing user-id in header' };
    }
    if(!MdbUserId){
        return { error: 'Missing movie-id' }
    }
    // Get user and movie IDs
    const userId = await userService.getUserIdByMdbId(MdbUserId); 
    const movieId =await movieService.getMovieIdByMdbId(MdbMovieId);
     if (userId.error)
        return userId;
     if (movieId.error)
        return movieId;
     try {
        // Get recommendations from the recommendation server
        const recommendations = await executeToRecommendServer('GET',userId, movieId);
        if (recommendations.startsWith(404)) {
            return { status: 404 };
        }
        // Parse the server response
        const movieIds = parseServerResponse(recommendations);
        if(movieIds.length === 0) {
            return {movies:[], status: 200} ;
        }
        // Get the recommended movies
       const movies = await movieService.getMoviesByIds(movieIds);
       return {movies: movies, status:200 };
    } catch (error) {
        return { error: 'Failed to get recommendations', status: 500 };
    }
};


module.exports = {
    executeToRecommendServer,
     addRecommendation,
        getRecommend
      };