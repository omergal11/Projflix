#include "ClientHandlerFactory.h"

// ClientHandlerFactory class: Implements the factory method to create ClientHandler objects.
IClientHandler* ClientHandlerFactory::createClientHandler(int clientSocket, App* app) {
    // Creates a new ClientHandler object, passing the client socket and app instance for communication.
    return new ClientHandler(clientSocket, app);  
}