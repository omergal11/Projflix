#ifndef PATCHCOMMAND_h
#define PATCHCOMMAND_h

#include "ICommand.h"
#include "AddCommand.h"
#include "../IDataManager.h"
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <map>
#include <vector>
using namespace std;
class PatchCommand : public ICommand {
private:
    IDataManager& dataManager;  // Reference to the data manager
    AddCommand* addCommand;     // Pointer to AddCommand for delegation

public:
    // Constructor initializes with IDataManager and AddCommand
    PatchCommand(IDataManager& dataManager, AddCommand* addCommand);

    // Executes the command logic
    void execute(std::ostream& output, std::vector<unsigned long int>& args) override;

    // Returns description of the PatchCommand
    std::string getDescription() const override;
};

#endif