#include "ThreadPoolManager.h"
#include <unistd.h> 
#include <iostream>

ThreadPoolManager::ThreadPoolManager(size_t threadCount) : stop(false) {
    // Create the specified number of threads
    for (size_t i = 0; i < threadCount; ++i) {
        workers.emplace_back([this] { this->worker(); });
    }
}

ThreadPoolManager::~ThreadPoolManager() {
    // Signal all threads to stop
    stop = true;
    condition.notify_all();

    // Join all threads
    for (std::thread& worker : workers) {
        if (worker.joinable()) {
            worker.join();
        }
    }
}
IThread* ThreadPoolManager::createThread(int clientSocket, IClientHandler* clientHandler) {
    // Add the client handling task to the thread pool
    enqueueTask([clientSocket, clientHandler]() {
        clientHandler->handleClient(); // Handle the client
        close(clientSocket);           // Close the client socket
        delete clientHandler;          // Clean up the client handler
    });

    // Return nullptr since threads are managed internally
    return nullptr;
}

void ThreadPoolManager::enqueueTask(std::function<void()> task) {
    {
        std::unique_lock<std::mutex> lock(queueMutex);
        tasks.push(task); // Add the task to the queue
    }
    condition.notify_one(); // Notify one thread to wake up
}
void ThreadPoolManager::worker() {
    while (true) {
        std::function<void()> task;
        {
            std::unique_lock<std::mutex> lock(queueMutex);
            condition.wait(lock, [this] { return stop || !tasks.empty(); });

            if (stop && tasks.empty()) {
                return; // Exit the thread if stopping and no tasks remain
            }

            task = std::move(tasks.front());
            tasks.pop();
        }

        try {
            task(); // Execute the task
        } catch (const std::exception& e) {
            // Log the error and continue
            std::cerr << "Exception in thread: " << e.what() << std::endl;
        } catch (...) {
            // Catch-all for unknown exceptions
            std::cerr << "Unknown exception in thread." << std::endl;
        }
    }
}

