#include "SocketMenu.h"
#include <sys/socket.h>   // For socket-related functions (socket, connect, etc.)
#include <arpa/inet.h>    // For inet_addr() and other IP address manipulations
#include <unistd.h>       // For close() function
#include <cstring>        // For memset() if needed
#include <stdexcept>      // For throwing exceptions

// Constructor for SocketMenu that takes an existing client socket and initializes the SocketStream
SocketMenu::SocketMenu(int clientSocket) : clientSocket(clientSocket) {
    if (clientSocket < 0) {
        throw std::runtime_error("Invalid client socket");
    }
}

// Function to read the next command from the socket
std::string SocketMenu::nextCommand() {
    char buffer[4096];  // Buffer to store the incoming data

    // Read data from the socket, storing up to 1023 bytes (reserve space for null terminator)
    ssize_t bytesRead = read(clientSocket, buffer, sizeof(buffer) - 1);

    // Handle errors or end of connection
    if (bytesRead < 0) {
        throw std::runtime_error("Error reading from socket");
    }

    if (bytesRead == 0) {
        throw std::runtime_error("Error reading from socket");
    }

    // Null-terminate the received data to make it a proper C-string
    buffer[bytesRead] = '\0';

    // Convert the received data to a std::string and return it
    return std::string(buffer);
}

void SocketMenu::sendResponse(const std::string& response) {
    ssize_t bytesSent = send(clientSocket, response.c_str(), response.size(), 0);  // Sends data to the client socket

    // If sending fails, throw a runtime error with a descriptive message.
    if (bytesSent < 0) {
        throw std::runtime_error("Error sending response to client");
    }
}