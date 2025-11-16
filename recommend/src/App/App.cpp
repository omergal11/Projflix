   #include "App.h"
   
   // Constructor: Initializes DataManager, sets up commands, and populates command map.
App::App() {
    // Initializes DataManager with the user-movies data file.
    dataManager = new DataFileManager("/usr/src/data/user_movies.txt");

    // Adds and sorts command names to the commandsOrder list.
    commandsOrder.push_back("POST");
    commandsOrder.push_back("PATCH");
    commandsOrder.push_back("GET");
    commandsOrder.push_back("DELETE");
    std::sort(commandsOrder.begin(), commandsOrder.end());
    commandsOrder.push_back("help");

    // Creates command objects and adds them to the Commands map.
    AddCommand* add = new AddCommand(*dataManager);
    Recommend* rec = new Recommend(*dataManager);
    Commands["POST"] = new PostCommand(*dataManager, add);
    Commands["PATCH"] = new PatchCommand(*dataManager, add);
    Commands["GET"] = new GetCommand(*dataManager, rec);
    Commands["DELETE"] = new DeleteCommand(*dataManager);
    Commands["help"] = new HelpCommand(Commands, commandsOrder);
}

// run: Executes the appropriate command based on the input pair and sends response to the client.
void App::run(std::pair<std::string, std::vector<unsigned long int>> pair, std::ostream& clientStream) {
    std::lock_guard<std::mutex> lock(appMutex);  // Locks mutex to ensure thread safety

    try {
        std::string command = pair.first;  // Retrieves the command from the pair

        // If command is not found in the map, send "400 Bad Request".
        if (Commands.find(command) == Commands.end()) {
            clientStream << "400 Bad Request\n";
            return;
        }

        // Execute the command.
        Commands[command]->execute(clientStream, pair.second);
    }
    catch (const std::exception& e) {
        clientStream << "400 Bad Request\n";  // Sends error response if an exception is caught
    }
}
