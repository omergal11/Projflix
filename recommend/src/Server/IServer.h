#ifndef ISERVER_H
#define ISERVER_H

#include <string>

class IServer {
public:
    virtual ~IServer() = default;  // Virtual destructor to ensure proper cleanup of derived classes.

    // Starts the server to listen for incoming client connections.
    virtual void start() = 0;

    // Stops the server, closing any active connections.
    virtual void stop() = 0;

    // Sets the port on which the server will listen for incoming connections.
    virtual void setPort(int port) = 0;
};


#endif 