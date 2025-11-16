const recommendationService = require('../services/recommendService');
const jwt = require('jsonwebtoken');

const addRecommendation = async (req, res) => {

    // get the user id from the header
    const MdbUserId = req.user.userId;
    // get the movie id from the request
    const MdbMovieId = req.params.id;
    try{
        // Call the recommendation service to add to the recommendation server
       const result = await recommendationService.addRecommendation(MdbUserId,MdbMovieId);
       if(result.error) return res.status(result.status).json({error: result.error});
        res.status(result.status).send();
    } catch (error) {
        res.status(500).json({ error: 'Failed to add recommendation' });
    }
};

const getRecommendations = async (req, res) => {
    // get the user id from the header
    const MdbUserId = req.user.userId;
    const MdbMovieId = req.params.id;
    try {
        // Call the recommendation service to get recommendations
        const recommendations = await recommendationService.getRecommend(MdbUserId, MdbMovieId);
        if (recommendations.error) {
         return res.status(recommendations.status).json({ error: recommendations.error });
        }
        return res.status(recommendations.status).json(recommendations.movies);
        
    } catch (error) {
        res.status(500).json({ error:error.message });
    }
};
module.exports = {addRecommendation, getRecommendations};