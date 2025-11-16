#include "ConsoleMenu.h"

// Constructor: Initializes ConsoleMenu (no specific setup in this case).
ConsoleMenu::ConsoleMenu() {}

// nextCommand: Retrieves user input from the console, returns it as a string.
std::string ConsoleMenu::nextCommand() {
    std::string input;
    std::getline(std::cin, input);  // Reads a line from standard input
    return input;
}

// sendResponse: Outputs the response to the console.
void ConsoleMenu::sendResponse(const std::string& response) {
    std::cout << response;  // Prints the response to the console
}