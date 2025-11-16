#ifndef THREADMANAGER_H
#define THREADMANAGER_H

#include "IThreadManager.h"
#include "ThreadPerClient.h"

// ThreadManager is responsible for creating threads to handle client requests
class ThreadManager : public IThreadManager {
public:
    // Creates a new ThreadPerClient to handle a specific client connection
    IThread* createThread(int clientSocket, IClientHandler* clientHandler) override;
};

#endif // THREADPERCLIENTFACTORY_H