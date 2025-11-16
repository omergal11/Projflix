#include "HelpCommand.h"
#include <vector>

// Constructor: Initializes HelpCommand with the map of commands and the vector defining their order.
HelpCommand::HelpCommand(const std::map<std::string, ICommand*>& commands, const std::vector<std::string>& commandOrder)
    : commands(commands), commandOrder(commandOrder) {}

// execute: Displays the help message. Checks for invalid arguments and lists commands in the specified order.
void HelpCommand::execute(std::ostream& output, std::vector<unsigned long int>& args) {
    // If arguments are invalid (not empty), return a "400 Bad Request" error.
    if (args.size() != 0) {
        output << "400 Bad Request\n";
        return;
    }

    // Display the "200 Ok" status for valid requests.
    output << "200 Ok\n\n";

    // Iterate through commandOrder to print descriptions of commands.
    for (unsigned long int i = 0; i < commandOrder.size(); i++) {
        // Find the command and print its description.
        std::map<std::string, ICommand*>::const_iterator it = commands.find(commandOrder[i]);
        if (it->second != nullptr) {
            output << it->second->getDescription() << "\n";
        }
    }
}

// getDescription: Returns a description of the HelpCommand, i.e., "help".
std::string HelpCommand::getDescription() const {
    return "help"; // Describes the HelpCommand functionality
}