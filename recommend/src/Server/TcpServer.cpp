#include "TcpServer.h"

// Constructor initializes server components, including the port, thread manager, client handler factory, and app instance.
TcpServer::TcpServer(int port, IThreadManager* threadManager, IClientHandlerFactory* clientHandlerFactory, App* app) : 
    port(port), threadManager(threadManager), clientHandlerFactory(clientHandlerFactory), app(app), running(false) {}

// Destructor ensures proper cleanup by stopping the server if it's running.
TcpServer::~TcpServer() {
    stop();
}

// Method to set or update the server's port.
void TcpServer::setPort(int port) {
    this->port = port;
}

// Method to start the server: 
// 1. Create socket and set up server configurations (bind and listen).
// 2. Accept incoming client connections in a loop.
void TcpServer::start() {
    running = true;
    sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("Error creating socket");
        return;
    }

    struct sockaddr_in sin;
    memset(&sin, 0, sizeof(sin));
    sin.sin_family = AF_INET;
    sin.sin_addr.s_addr = INADDR_ANY;
    sin.sin_port = htons(port);

    // Binding the socket to the specified port.
    if (::bind(sock, (struct sockaddr*)&sin, sizeof(sin)) < 0) {
        perror("Error binding socket");
        close(sock);
        return;
    }

    // Start listening for incoming connections with a backlog of 5.
    if (listen(sock, 5) < 0) {
        perror("Error listening to socket");
        close(sock);
        return;
    }

    struct sockaddr_in client_sin;
    unsigned int addr_len = sizeof(client_sin);

    // Main server loop to accept and handle clients.
    while (running) {
        int client_sock = accept(sock, (struct sockaddr*)&client_sin, &addr_len);
        if (client_sock < 0) {
            if (running) {
                perror("Error accepting client");
            }
            continue;
        }

        // Create a client handler and delegate handling the client connection.
        IClientHandler* clientHandler = clientHandlerFactory->createClientHandler(client_sock, app);

        // Create a new thread to handle the client connection asynchronously.
        IThread* thread = threadManager->createThread(client_sock, clientHandler);
    }

    // Close the socket when done.
    close(sock);
}

// Stop method to gracefully shut down the server, stopping the loop and closing the socket.
void TcpServer::stop() {
    running = false;
    shutdown(sock, SHUT_RDWR);  // Shutdown socket communication.
    close(sock);                // Close the socket.
    sock = -1;                  // Reset socket handle.
}