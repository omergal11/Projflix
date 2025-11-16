#include "Recommend.h"
#include <ostream>
#include <unordered_map>
#include <unordered_set>
#include <map>
#include <vector>
#include <algorithm>

// Constructor initializes the Recommend object with a reference to an IDataManager instance.
// `dataManager` is used to access user and movie data for making recommendations.
Recommend::Recommend(IDataManager& dataManager)
    : dataManager(dataManager) {}

// The execute function performs the recommendation process and prints the recommended movies to the output stream.
void Recommend::execute(std::ostream& output, std::vector<unsigned long int>& extractArgs) { 
    
    // If arguments are invalid or the size of arguments is not equal to 2, return without doing anything.
    if (extractArgs.empty() || extractArgs.size() != 2) {
        return;
    }

    // Extract userId and movieId from the arguments.
    unsigned long int userId = (extractArgs)[0];
    unsigned long int movieId = (extractArgs)[1];

    // Read the data for all users and the movies they have watched from the data manager.
    std::unordered_map<unsigned long int, std::unordered_set<unsigned long int>> userMovies = dataManager.readData();

    // If the userId is not found in the data, return without doing anything.
    if (userMovies.find(userId) == userMovies.end()) {
        return;
    }

    // Get the list of movies the user has watched.
    const std::unordered_set<unsigned long int>& userWatched = userMovies[userId];

    // Calculate similarity scores for the user based on movies they have watched.
    std::unordered_map<unsigned long int, unsigned long int> similarityScores = similarityCheck(userWatched, userMovies, userId);

    // Find users who have watched the movie we're recommending and store them in a set.
    std::unordered_set<unsigned long int> relevantUsers; 
    for (const std::pair<const unsigned long int, std::unordered_set<unsigned long int>>& userData : userMovies) {
        if (userData.second.count(movieId)) {
            relevantUsers.insert(userData.first);
        }
    }
    if(relevantUsers.size() == 0){
            return;
    }

    // Store the scores for each movie based on recommendations.
    std::map<unsigned long int, unsigned long int> movieScores; 
    for (unsigned long int relevantUserId : relevantUsers) {
        const std::unordered_set<unsigned long int>& otherMovies = userMovies[relevantUserId];

        // For each movie that a relevant user has watched, update its score if the current user has not watched it
        // and it's not the movie we are recommending.
        for (unsigned long int movie : otherMovies) {
            if (!userWatched.count(movie) && movie != movieId) { 
                movieScores[movie] += similarityScores[relevantUserId];
            }
        }
    }

    // Sort the movie scores in descending order and prepare them for printing.
    std::vector<std::pair<unsigned long int, unsigned long int>> sortedToPrint = sortMovies(movieScores);

    // Print the sorted movie recommendations to the output stream.
    printRecommendation(sortedToPrint, output);
}

// This method returns a description of the Recommend command and how to use it.
std::string Recommend::getDescription() const {
    return "recommend [userid] [movieid]";
}

// similarityCheck compares the movies watched by the target user (userId) with other users' movie lists.
// It returns a map of user IDs to similarity scores (how many movies they have in common).
std::unordered_map<unsigned long int, unsigned long int> Recommend::similarityCheck(
    const std::unordered_set<unsigned long int>& userWatched,
    const std::unordered_map<unsigned long int, std::unordered_set<unsigned long int>>& userMovies,
    unsigned long int userId) {

    std::unordered_map<unsigned long int, unsigned long int> similarityScores;

    // Iterate over all users and calculate the similarity based on common movies.
    for (const std::pair<const unsigned long int, std::unordered_set<unsigned long int>>& userData : userMovies) {
        if (userData.first == userId) continue; // Skip the target user.

        unsigned long int commonMovies = 0;

        // Count the number of common movies between the current user and the target user.
        for (unsigned long int movie : userData.second) {
            if (userWatched.count(movie)) {
                commonMovies++;
            }
        }

        // If there are common movies, store the similarity score for the user.
        if (commonMovies > 0) {
            similarityScores[userData.first] = commonMovies;
        }
    }

    return similarityScores;
}

// This method formats and prints the movie recommendations to the output stream.
// It limits the number of recommendations to 10.
void Recommend::printRecommendation(const std::vector<std::pair<unsigned long int, unsigned long int>>& movieScores, std::ostream& output) {
    unsigned long int counter = 0;

    // Iterate over the sorted movie recommendations and print the movie IDs.
    for (std::vector<std::pair<unsigned long int, unsigned long int>>::const_iterator it = movieScores.begin();
         it != movieScores.end() && counter < 10; ++it, ++counter) {
        if (counter > 0) {
            output << ","; // Print a comma between movie IDs.
        }
        output << it->first; // Print the movie ID.
    }

    // Print a newline after the list of recommendations.
    output << "\n"; 
}

// This method sorts the movie recommendations by their scores in descending order.
// If two movies have the same score, they are sorted by their movie ID in ascending order.
std::vector<std::pair<unsigned long int, unsigned long int>> Recommend::sortMovies(const std::map<unsigned long int, unsigned long int>& movieScores) {
    std::vector<std::pair<unsigned long int, unsigned long int>> sorted;

    // Copy all the movie scores into a vector for sorting.
    for (std::map<unsigned long int, unsigned long int>::const_iterator i = movieScores.begin(); i != movieScores.end(); ++i) {
        sorted.push_back(*i); 
    }

    // Sort the vector based on the movie scores and movie IDs.
    for (unsigned long int i = 0; i < sorted.size(); i++) {
        for (unsigned long int j = i + 1; j < sorted.size(); j++) {
            if (sorted[j].second > sorted[i].second || 
                (sorted[j].second == sorted[i].second && sorted[j].first < sorted[i].first)) {
                std::pair<unsigned long int, unsigned long int> temp = sorted[i];
                sorted[i] = sorted[j];
                sorted[j] = temp;
            }   
        }
    }

    return sorted;
}