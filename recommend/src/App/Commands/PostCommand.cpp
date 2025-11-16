#include "PostCommand.h"
using namespace std;

// Constructor initializes with IDataManager and AddCommand, throws error if addCommand is null.
PostCommand::PostCommand(IDataManager& dataManager, AddCommand* addCommand): 
dataManager(dataManager), addCommand(addCommand) {
    if (!addCommand) {
        throw std::invalid_argument("addCommand cannot be null");
    }
}

// execute: Validates arguments, checks if the user exists, and delegates to AddCommand.
void PostCommand::execute(std::ostream& output, std::vector<unsigned long int>& args) {
    // Checks if there are enough arguments
    if (args.size() < 2) {  
        output << "400 Bad Request\n";
        return;
    }   
    // Checks if the user is found
    if (dataManager.foundUser(args[0])) { 
        output << "404 Not Found\n";
        return;
    }

    // Executes AddCommand if validation passes, then sends success response.
    addCommand->execute(output, args);
    output << "201 Created\n";
}

// getDescription: Returns the description of the POST command and its arguments.
string PostCommand::getDescription() const {
    return "POST, arguments: [userid] [movieid1] [movieid2] ...";
}