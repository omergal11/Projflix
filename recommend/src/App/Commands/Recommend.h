#ifndef RECOMMENDCOMMAND_H
#define RECOMMENDCOMMAND_H

#include "ICommand.h"
#include "../IDataManager.h"
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <map>
#include <vector>

// The Recommend class is a concrete implementation of the ICommand interface.
// It is responsible for recommending movies to a user based on the movies watched by other similar users.
class Recommend : public ICommand {
private:
    // A reference to an IDataManager object, which provides access to the underlying data.
    IDataManager& dataManager;

    // similarityCheck compares the movies watched by a given user (userId) to those watched by other users.
    // It returns a map of user IDs to similarity scores, where the score is a measure of how similar another user is to the target user.
    std::unordered_map<unsigned long int, unsigned long int> similarityCheck(
        const std::unordered_set<unsigned long int>& userWatched,
        const std::unordered_map<unsigned long int, std::unordered_set<unsigned long int>>& userMovies, unsigned long int userId);

    // printRecommendation formats and prints the movie recommendations to the output stream.
    void printRecommendation(const std::vector<std::pair<unsigned long int, unsigned long int>>& movieScores, std::ostream& output);

    // sortMovies sorts the movie recommendations by their scores in descending order.
    std::vector<std::pair<unsigned long int, unsigned long int>> sortMovies(const std::map<unsigned long int, unsigned long int>& movieScores);

public:
    // Constructor initializes the Recommend object with a reference to an IDataManager instance.
    // `dataManager` is used to access user and movie data for making recommendations.
    Recommend(IDataManager& dataManager);

    // Executes the recommendation process and prints the recommended movies to the output stream.
    void execute(std::ostream& output, std::vector<unsigned long int>& args) override;

    // Returns a description of the Recommend command, e.g., "Recommends movies based on user preferences".
    std::string getDescription() const override;
};

#endif