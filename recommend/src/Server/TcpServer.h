#ifndef TCPSERVER_H
#define TCPSERVER_H

#include "IServer.h"
#include <iostream>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <string.h>
#include <thread>
#include <vector>
#include <mutex>
#include "Threads/IThreadManager.h"
#include "Threads/ThreadPerClient.h"
#include "../App/App.h"
#include "ClientHandlers/ClientHandlerFactory.h"
#include "ClientHandlers/IClientHandlerFactory.h"
#include "Threads/ThreadManager.h"
#include "ClientHandlers/ClientHandler.h"

// TcpServer class inherits from IServer and manages TCP socket communication with clients.
class TcpServer : public IServer {
private:
    int sock;                       // Socket for communication
    int port;                       // Port number for the server to listen on
    bool running;                   // Flag to check if the server is running
    IClientHandlerFactory* clientHandlerFactory;  // Factory to create client handlers
    IThreadManager* threadManager;  // Thread manager to manage client connections
    App* app;                       // Application object to provide business logic or data

public:
    // Constructor that initializes the server with port, thread manager, client handler factory, and application
    TcpServer(int port, IThreadManager* threadManager, IClientHandlerFactory* clientHandlerFactory, App* app);
    
    // Destructor to clean up resources
    ~TcpServer() override;

    // Set the port number for the server
    void setPort(int port) override;

    // Start the server, bind the socket, listen for client connections, and handle them
    void start() override;

    // Stop the server by shutting down the socket and stopping any client handling
    void stop() override;
};


#endif