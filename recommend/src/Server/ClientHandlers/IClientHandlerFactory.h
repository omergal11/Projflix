#ifndef ICLIENTHANDLERFACTORY_H
#define ICLIENTHANDLERFACTORY_H

#include "IClientHandler.h"
#include "../../App/App.h"

// Interface for creating IClientHandler objects
class IClientHandlerFactory {
public:
    // Pure virtual function to create a client handler object
    virtual IClientHandler* createClientHandler(int clientSocket, App* app) = 0;

    // Virtual destructor to allow proper cleanup of derived classes
    virtual ~IClientHandlerFactory() = default;
};

#endif // ICLIENTHANDLERFACTORY_H