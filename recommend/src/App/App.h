#ifndef APP_H
#define APP_H

#include <iostream>
#include <map>
#include <string>
#include <vector>
#include "Commands/ICommand.h"
#include "Commands/AddCommand.h"
#include "Commands/Recommend.h"
#include "Commands/HelpCommand.h"
#include "IDataManager.h"
#include "Menus/IMenu.h"
#include "DataFileManager.h"
#include "App.h"
#include "Parser.h"
#include "Commands/PostCommand.h"
#include "Commands/PatchCommand.h"
#include "Commands/DeleteCommand.h"
#include "Commands/GetCommand.h"
#include <mutex>

class App {
private:
    // Map to store commands associated with their names.
    std::map<std::string, ICommand*> Commands;

    // List of command names in the order they should be executed.
    std::vector<std::string> commandsOrder;

    // Mutex for thread safety.
    std::mutex appMutex;

    // Pointer to IDataManager for managing data.
    IDataManager* dataManager;

public:
    // Constructor: Initializes the app, creates commands, and sets up the menu.
    App();

    // run: Processes input commands and executes them, sending responses to the client.
    void run(std::pair<std::string, std::vector<unsigned long int>> pair, std::ostream& clientStream);
};

#endif // APP_H