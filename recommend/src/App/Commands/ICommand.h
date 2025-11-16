#ifndef ICOMMAND_H
#define ICOMMAND_H

#include <vector>
#include <string>
#include <sstream>
#include <iostream>

//interface class for Command Classes
class ICommand 
{
public:
    // Pure virtual function to be implemented by derived classes
    virtual void execute(std::ostream& output,std::vector<unsigned long int>& args) = 0;
    // Pure virtual function will return a description of the command's functionality.
    virtual std::string getDescription() const = 0;
};
#endif