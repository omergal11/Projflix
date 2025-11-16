#ifndef POSTCOMMAND_h
#define POSTCOMMAND_h

#include "ICommand.h"
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <map>
#include <vector>
#include "../IDataManager.h"
#include "AddCommand.h"
using namespace std;
using namespace std;

class PostCommand : public ICommand {  // Inherits ICommand to define the POST command behavior
private:
    IDataManager& dataManager;  // Reference to IDataManager to interact with data
    AddCommand* addCommand;     // Pointer to AddCommand to delegate logic

public:
    // Constructor: Initializes PostCommand with IDataManager and AddCommand, throws error if addCommand is null
    PostCommand(IDataManager& dataManager, AddCommand* addCommand);

    // execute: Executes the POST logic, validates arguments, checks user, and calls AddCommand
    void execute(std::ostream& output, std::vector<unsigned long int>& args) override;

    // getDescription: Returns description of POST command functionality
    std::string getDescription() const override;
};


#endif