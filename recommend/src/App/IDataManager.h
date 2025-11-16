#ifndef IDATAMANAGER_H
#define IDATAMANAGER_H

#include <unordered_map>
#include <unordered_set>
#include <vector>
#include <string>
#include <iostream>

class IDataManager {
public:
    // Checks if the specified user has all the given movies.
    virtual bool foundMoviesInUser(unsigned long int userId, const std::vector<unsigned long int>& movies) = 0;

    // Checks if the user exists in the data.
    virtual bool foundUser(unsigned long int userId) = 0;

    // Deletes specified movies from the user's data.
    virtual void deleteMovies(unsigned long int userId, const std::vector<unsigned long int>& movies) = 0;

    // Reads data from a source (e.g., file, network, etc.).
    virtual std::unordered_map<unsigned long int, std::unordered_set<unsigned long int>> readData() = 0;

    // Writes data to a source (e.g., file, network, etc.).
    virtual void writeData(const std::unordered_map<unsigned long int, std::unordered_set<unsigned long int>>& userMovies) = 0;

    // Adds movies for a given user.
    virtual void addMovies(unsigned long int userId, const std::vector<unsigned long int>& movies) = 0;
};


#endif // IDATAMANAGER_H