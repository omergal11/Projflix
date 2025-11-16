
#include "ThreadManager.h"

// Creates a new thread to handle a specific client connection
IThread* ThreadManager::createThread(int clientSocket, IClientHandler* clientHandler) {
    // Create and return a new ThreadPerClient object
    return new ThreadPerClient(clientSocket, clientHandler); 
}