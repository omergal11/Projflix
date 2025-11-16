#ifndef IMENU_H
#define IMENU_H

#include <vector>
#include <string>
#include <sstream>
#include <iostream>
#include <algorithm>

class IMenu {
public:
    // Pure virtual function: Must be implemented by derived classes to return the next command to execute.
    virtual std::string nextCommand() = 0;

    // Pure virtual function: Must be implemented by derived classes to send a response (e.g., to a console or UI).
    virtual void sendResponse(const std::string& response) = 0;
};

#endif
