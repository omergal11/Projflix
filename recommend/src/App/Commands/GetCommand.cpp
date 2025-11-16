#include <iostream>
#include <vector>
#include "GetCommand.h"
using namespace std;

// Constructor for GetCommand class
// Initializes the dataManager and recommend objects
GetCommand::GetCommand(IDataManager& dataManager, Recommend* recommend) :
dataManager(dataManager), recommend(recommend) {}

// Method that executes the GET command
void GetCommand::execute(ostream& output, vector<unsigned long int>& args) {
    // Check if the number of arguments is exactly 2 (user ID and movie ID)
    if (args.size() != 2) {
        output << "400 Bad Request\n"; // Invalid request due to incorrect arguments
        return;
    }

    // Check if the user exists in the data manager
    if (!dataManager.foundUser(args[0])) {
        output << "404 Not Found\n"; // User not found
        return;
    }

    // Create a stringstream to build the response
    std::stringstream response;

    // Set the HTTP status to 200 OK (success)
    response << "200 ok\n\n";

    // Execute the recommendation system to generate recommendations and add it to the response
    recommend->execute(response, args);

    // Send the final response to the output stream
    output << response.str();
    output.flush(); // Ensure the output is immediately sent
}

// Method that returns the description of the GET command
string GetCommand::getDescription() const {
    return "GET, arguments: [userid] [movieid]";
}