#ifndef HELPCOMMAND_H
#define HELPCOMMAND_H

#include "ICommand.h"
#include <map>
#include <vector>

// The HelpCommand class displays help information for available commands.
class HelpCommand : public ICommand {
private:
    // Map storing command names (keys) and ICommand pointers (values)
    // Allows access to all available commands.
    const std::map<std::string, ICommand*>& commands;

    // Vector storing the order of command display in the help message.
    std::vector<std::string> commandOrder;

public:
    // Constructor: Initializes HelpCommand with the commands map and the order vector.
    HelpCommand(const std::map<std::string, ICommand*>& commands, const std::vector<std::string>& commandOrder);

    // execute: Generates and displays the help message by listing command descriptions.
    // It is called to print the help message to the provided output stream.
    void execute(std::ostream& output, std::vector<unsigned long int>& args) override;
    
    // getDescription: Returns a description of the HelpCommand's functionality (e.g., "Displays a list of available commands").
    std::string getDescription() const override;
};

#endif