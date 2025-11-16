#ifndef ITHREADMANAGER_H
#define ITHREADMANAGER_H

#include "IThread.h"
#include "../ClientHandlers/IClientHandler.h"
class IThreadManager {
public:
    // Virtual method to create a new thread for handling a client.
    virtual IThread* createThread(int clientSocket, IClientHandler* clientHandler) = 0;
};
#endif