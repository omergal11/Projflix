#include "DeleteCommand.h"
#include <sstream>
#include <iostream>
#include <vector>
#include <unordered_set>
// Constructor for DeleteCommand
DeleteCommand::DeleteCommand(IDataManager& dataManager) : dataManager(dataManager) {}

// execute method: Performs the "delete" operation
void DeleteCommand::execute(std::ostream& output, std::vector<unsigned long int>& args) { 
    if (args.size() < 2)
    {
        output << "400 Bad Request\n";
    }
    else {
        unsigned long int userId = args[0];
         std::vector<unsigned long int> movies(args.begin() + 1, args.end());
        // if the user has not watched the movie or does not exist
        if (dataManager.foundMoviesInUser(userId, movies))
        {
            dataManager.deleteMovies(userId, movies);
            output << "204 No Content\n";
        }
        else {
            output << "404 Not Found\n";
        }
    }
}

// getDescription method: Returns the description of the delete command
std::string DeleteCommand::getDescription() const {
    return "DELETE, arguments: [userid] [movieid1] [movieid2] ...";
}