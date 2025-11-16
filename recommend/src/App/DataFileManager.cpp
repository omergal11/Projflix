#include "DataFileManager.h"
#include <fstream>
#include <sstream>
#include <set>

// Constructor initializes the fileName and reads the data from file.
DataFileManager::DataFileManager(const std::string& fileName) : fileName(fileName) {
    userMovies = readData();  // Reads data from the file into userMovies map.
}

// Checks if the user has all the given movies.
bool DataFileManager::foundMoviesInUser(unsigned long int userId, const std::vector<unsigned long int>& movies) {
    if (!foundUser(userId)) {
        return false;  // User not found.
    }
    if (movies.empty()) {
        return true;  // If no movies specified, assume they are all found.
    }

    const std::unordered_set<unsigned long int>& userMovieSet = userMovies[userId];
    for (unsigned long int movie : movies) {
        if (userMovieSet.find(movie) == userMovieSet.end()) {
            return false;  // Movie not found in user's set.
        }
    }
    return true;  // All movies found.
}

// Checks if the user exists in the data.
bool DataFileManager::foundUser(unsigned long int userId) {
    if (userMovies.empty()) {
        return false;  // No data available.
    }
    return (userMovies.find(userId) != userMovies.end());  // Return true if user exists.
}

// Extracts the user ID from a line in the file.
unsigned long int DataFileManager::FirstIdFromStream(std::istringstream& ss) {
    unsigned long int id;
    char colon;
    ss >> id >> colon;   // Read ID and colon (e.g., "123: ")
    return id;
}

// Converts the remaining line of text into a set of movie IDs.
std::unordered_set<unsigned long int> DataFileManager::VectorFromStream(std::istringstream& ss) {
    std::unordered_set<unsigned long int> movies;
    unsigned long int movie;

    // Reads movie IDs and stores them in a set.
    while (ss >> movie) {
        movies.insert(movie);  
        if (ss.peek() == ',') ss.ignore();  // Ignore commas separating movie IDs.
    }
    return movies;  // Return the set of movie IDs.
}

// Reads user data from the file and returns a map of user IDs and their movie sets.
std::unordered_map<unsigned long int, std::unordered_set<unsigned long int>> DataFileManager::readData() {
    std::unordered_map<unsigned long int, std::unordered_set<unsigned long int>> userMovies;
    std::ifstream inputFile(fileName);  // Open file for reading.
    
    if (!inputFile.is_open()) {
        return userMovies;  // Return empty map if file cannot be opened.
    }

    std::string line;
    while (std::getline(inputFile, line)) {
        std::istringstream ss(line);
        unsigned long int id = FirstIdFromStream(ss);  // Extract user ID.
        std::unordered_set<unsigned long int> movies = VectorFromStream(ss);  // Extract movie IDs.
        userMovies[id] = movies;  // Add user and movie data to the map.
    }

    inputFile.close();  // Close the file after reading.
    return userMovies;   // Return the populated map.
}

// Writes the user movie data back to the file.
void DataFileManager::writeData(const std::unordered_map<unsigned long int, std::unordered_set<unsigned long int>>& userMovies) {
    std::ofstream outputFile(fileName);  // Open the file for writing.
    if (userMovies.empty()) {
        outputFile.close();  // Close if no data to write.
        return;
    }

    // Iterate over user data and write it to the file.
    for (const auto& [id, movieSet] : userMovies) {
        outputFile << id << ": ";  // Write user ID and colon.
        for (auto it = movieSet.begin(); it != movieSet.end(); ++it) {
            outputFile << *it;  // Write movie ID.
            if (std::next(it) != movieSet.end()) {
                outputFile << ",";  // Add a comma if more movies exist.
            }
        }
        outputFile << "\n";  // End the line for this user.
    }

    outputFile.close();  // Close the file after writing.
}

// Adds movies to the user's movie set and updates the file.
void DataFileManager::addMovies(unsigned long int userId, const std::vector<unsigned long int>& movies) {
    auto& movieSet = userMovies[userId];  // Get reference to user's movie set.
    
    for (unsigned long int movie : movies) {
        movieSet.insert(movie);  // Add each movie to the set (duplicates are ignored).
    }
    
    writeData(userMovies);  // Write updated data back to file.
}

// Deletes movies from the user's movie set and updates the file.
void DataFileManager::deleteMovies(unsigned long int userId, const std::vector<unsigned long int>& movies) {
    if (foundUser(userId)) {
        std::unordered_set<unsigned long int>& userMovieSet = userMovies[userId];

        for (unsigned long int movie : movies) {
            if (userMovieSet.find(movie) != userMovieSet.end()) {
                userMovieSet.erase(movie);  // Remove movie from the user's set.
            }
        }

        if (userMovieSet.empty()) {
            userMovies.erase(userId);  // Remove user from map if no movies remain.
        }

        writeData(userMovies);  // Write updated data back to file.
    }
}
