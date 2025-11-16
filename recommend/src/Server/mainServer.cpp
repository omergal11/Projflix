#include "TcpServer.h"
#include "Threads/ThreadPoolManager.h"

int main(int argc, char* argv[]) {
    // Check if the user provided a port number as an argument
    if (argc < 2) {
        std::cerr << "Error: Port number is missing!" << std::endl;
        return 1;  // Exit if no port is provided
    }

    // Convert the port number from string to integer
    int port = std::atoi(argv[1]);

    // Validate the port number (should not be 0)
    if (port == 0) {
        std::cerr << "Error: Invalid port number!" << std::endl;
        return 1;  // Exit if the port number is invalid
    }

    // Create the thread manager and client handler factory for handling connections
    IThreadManager* threadManager = new ThreadPoolManager(25);
    IClientHandlerFactory* clientHandlerFactory = new ClientHandlerFactory();

    // Initialize the app and the TCP server
    App* app = new App();
    TcpServer server(port, threadManager, clientHandlerFactory, app);

    // Start the server to listen for incoming client connections
    server.start();

    return 0;  // Exit the program successfully
}