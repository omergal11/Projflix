#include "ThreadPerClient.h"

// Constructor initializes the socket and client handler.
ThreadPerClient::ThreadPerClient(int socket, IClientHandler* clientHandler) :
    clientSocket(socket), clientHandler(clientHandler) {}

// Destructor ensures the thread is joined before destruction, if it's joinable.
ThreadPerClient::~ThreadPerClient() {
    if (thread.joinable()) {
        thread.join();
    }
}

// This method is responsible for invoking the client handler's functionality.
void ThreadPerClient::run() {
    clientHandler->handleClient();
}

// Starts the thread by running the 'run' method.
void ThreadPerClient::start() {
    thread = std::thread(&ThreadPerClient::run, this);
}

// Joins the thread, blocking until it completes.
void ThreadPerClient::join() {
    if (thread.joinable()) {
        thread.join();
    }
}

// Returns a reference to the internal thread object, so it can be monitored if needed.
std::thread& ThreadPerClient::getThread() {
    return thread;
}