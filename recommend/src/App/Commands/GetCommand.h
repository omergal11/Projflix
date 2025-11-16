#ifndef GETCOMMAND_H
#define GETCOMMAND_H

#include "ICommand.h"
#include "../IDataManager.h"
#include "Recommend.h"
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <map>
#include <vector>

// The GetCommand class inherits from the ICommand base class
class GetCommand: public ICommand {
private:
    IDataManager& dataManager;  // Reference to the data manager, used for checking user existence and fetching data
    Recommend* recommend;       // Pointer to the Recommend object, used to generate movie recommendations

public:
    // Constructor: Initializes the GetCommand with a reference to a data manager and a recommend object
    GetCommand(IDataManager& dataManager, Recommend* recommend);

    // execute method: This method implements the logic of the GET command
    // It takes an output stream and a vector of arguments (user ID and movie ID)
    void execute(std::ostream& output, std::vector<unsigned long int>& args) override;

    // getDescription method: Returns a string describing the GET command and its arguments
    std::string getDescription() const override;
};

#endif