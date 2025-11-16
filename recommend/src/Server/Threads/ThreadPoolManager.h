#ifndef THREADPOOLMANAGER_H
#define THREADPOOLMANAGER_H

#include "IThreadManager.h"
#include "../ClientHandlers/IClientHandler.h"
#include <vector>
#include <thread>
#include <queue>
#include <mutex>
#include <condition_variable>
#include <functional>
#include <atomic>

class ThreadPoolManager : public IThreadManager {
public:
    ThreadPoolManager(size_t threadCount); // Constructor
    ~ThreadPoolManager();                  // Destructor

    IThread* createThread(int clientSocket, IClientHandler* clientHandler) override; // Add client task to the pool

private:
    void enqueueTask(std::function<void()> task); // Add task to the queue
    void worker();                                // Worker function for threads

    std::vector<std::thread> workers;            // Vector of worker threads
    std::queue<std::function<void()>> tasks;     // Task queue

    std::mutex queueMutex;                       // Mutex for synchronizing access to the task queue
    std::condition_variable condition;           // Condition variable for thread synchronization
    std::atomic<bool> stop;                      // Flag to indicate if the pool is stopping
};

#endif // THREADPOOLMANAGER_H
