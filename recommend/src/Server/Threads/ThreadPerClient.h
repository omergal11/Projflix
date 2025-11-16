#ifndef THREADPERCLIENT_H
#define THREADPERCLIENT_H

#include "IThread.h"
#include "../ClientHandlers/ClientHandler.h"
#include <thread>

// ThreadPerClient class implements the IThread interface to handle each client in a separate thread.
class ThreadPerClient : public IThread {
private:
    int clientSocket;           // Socket to communicate with the client
    IClientHandler* clientHandler;  // Handler to process client requests
    std::thread thread;         // A thread that will run the client handler

    // Function that runs the client handler in a separate thread
    void run();

public:
    // Constructor that accepts a client socket and a client handler
    explicit ThreadPerClient(int socket, IClientHandler* clientHandler);

    // Destructor to clean up resources (join the thread if necessary)
    ~ThreadPerClient() override;

    // Start the thread to handle the client
    void start() override;

    // Wait for the thread to finish execution
    void join() override;

    // Get the reference to the internal thread object
    std::thread& getThread() override;
};

#endif // THREADPERCLIENT_H
