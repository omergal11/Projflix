#include "PatchCommand.h"

using namespace std;

PatchCommand::PatchCommand(IDataManager& dataManager, AddCommand* addCommand): 
dataManager(dataManager),addCommand(addCommand) {}

void PatchCommand::execute(std::ostream& output, std::vector<unsigned long int>& args) {

 if (args.size() < 2) {
    output << "400 Bad Request\n";
    return;
 }   
 if (!dataManager.foundUser(args[0])) {
    output << "404 Not Found\n";
    return;
 }
 addCommand->execute(output, args);
 output << "204 No Content\n";

}

string PatchCommand::getDescription() const {
    return "PATCH, arguments: [userid] [movieid1] [movieid2] ...";

}