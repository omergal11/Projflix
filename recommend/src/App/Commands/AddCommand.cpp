#include "AddCommand.h"
#include <iostream>

// Constructor for AddCommand class
// Takes a reference to an IDataManager object to interact with the data
AddCommand::AddCommand(IDataManager& dataManager) 
    : dataManager(dataManager) { }

// The execute function is called when the "add" command is invoked
// It processes the input arguments and adds movies for the specified user
void AddCommand::execute(std::ostream& output, std::vector<unsigned long int>& args) {
    // If the arguments are invalid or too few (less than 2), return without doing anything
    if (args.empty() || args.size() < 2) {
        return;  // Invalid input, or not enough arguments (we need at least userId and one movieId)
    }

    // The first argument is the user ID (userId), which is the target of the operation
    unsigned long int userId = args[0];

    // Vector to hold the movie IDs
    std::vector<unsigned long int> movies(args.begin() + 1, args.end());
    // Call the addMovies function on dataManager to add movies for the given user
    dataManager.addMovies(userId, movies);
}
// This is used to inform the user of the expected format for the "add" command
std::string AddCommand::getDescription() const {
    return "add [userid] [movieid1] [movieid2] ...";  // Expected command format
}