#ifndef CLIENTHANDLERFACTORY_H
#define CLIENTHANDLERFACTORY_H

#include "IClientHandlerFactory.h"
#include "ClientHandler.h"

class ClientHandlerFactory : public IClientHandlerFactory {
public:
    // Creates and returns a new ClientHandler for the given client socket and app
    IClientHandler* createClientHandler(int clientSocket, App* app) override;

    // Default destructor
    virtual ~ClientHandlerFactory() = default;
};


#endif // CLIENTHANDLERFACTORY_H