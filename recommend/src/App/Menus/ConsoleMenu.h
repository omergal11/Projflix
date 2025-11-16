#ifndef CONSOLEMENU_H
#define CONSOLEMENU_H

#include "IMenu.h"
#include <vector>
#include <string>
#include <sstream>
#include <iostream>
#include <algorithm>

// ConsoleMenu is a derived class of IMenu that represents a menu system in the console.
// It provides functionality to retrieve the next command input by the user.
// ConsoleMenu is a derived class of IMenu representing a menu system in the console.
class ConsoleMenu : public IMenu {
public:
    // Constructor: Initializes the ConsoleMenu, sets up initial state/configurations.
    ConsoleMenu();

    // nextCommand: Retrieves the next command input from the user as a string.
    // Returns the next command the user wants to execute.
    std::string nextCommand() override;

    // sendResponse: Sends a response (e.g., a message) to the user via console output.
    void sendResponse(const std::string& response) override;
};

#endif
