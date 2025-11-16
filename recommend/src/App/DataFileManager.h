#ifndef DATAFILEMANAGER_H
#define DATAFILEMANAGER_H

#include "IDataManager.h"
#include <fstream>
#include <sstream>
#include <unordered_map>
#include <unordered_set>

class DataFileManager : public IDataManager {
private:
    std::string fileName;  // Stores the file name where user data is saved.
    std::unordered_map<unsigned long int, std::unordered_set<unsigned long int>> userMovies;  // Map of user IDs to sets of movie IDs.

    // Helper function to extract user ID from the stream.
    unsigned long int FirstIdFromStream(std::istringstream& ss);
    
    // Helper function to extract a set of movie IDs from the stream.
    std::unordered_set<unsigned long int> VectorFromStream(std::istringstream& ss);

public:
    // Constructor: Initializes DataFileManager with a given file name and loads data.
    DataFileManager(const std::string& fileName);

    // Checks if the specified user has all the given movies.
    bool foundMoviesInUser(unsigned long int userId, const std::vector<unsigned long int>& movies) override;

    // Reads data from the file and returns a map of user IDs to movie sets.
    std::unordered_map<unsigned long int, std::unordered_set<unsigned long int>> readData() override;

    // Checks if the user exists in the data.
    bool foundUser(unsigned long int userId) override;

    // Deletes specified movies from the user's data and updates the file.
    void deleteMovies(unsigned long int userId, const std::vector<unsigned long int>& movies) override;

    // Writes the updated user movie data back to the file.
    void writeData(const std::unordered_map<unsigned long int, std::unordered_set<unsigned long int>>& userMovies) override;

    // Adds movies to the user's movie set and updates the file.
    void addMovies(unsigned long int userId, const std::vector<unsigned long int>& movies) override;
};

#endif // DATAFILEMANAGER_H