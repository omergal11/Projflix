#ifndef ADDCOMAND_h
#define ADDCOMAND_h

#include "ICommand.h"
#include "../IDataManager.h"
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <map>
#include <vector>

/// The AddCommand class is derived from the ICommand interface. It represents the command 
/// for adding movies to a user's profile in the data system.
class AddCommand: public ICommand {
private:
    IDataManager& dataManager;  // A reference to the dataManager to interact with the data layer

public:
    // Constructor: Initializes AddCommand with a reference to an IDataManager object
    // The reference is stored so that AddCommand can use it to modify data.
    AddCommand(IDataManager& dataManager);

    // The execute method is used to perform the actual "add" operation
    // output: an output stream to print any results (if needed)
    // args: a string containing the command's arguments (e.g., userId and movieIds)
    void execute(std::ostream& output, std::vector<unsigned long int>& args) override;

    // The getDescription method returns a string that describes the format of the command
    // This is useful for providing the user with a guide on how to use the "add" command
    std::string getDescription() const override;
};
#endif