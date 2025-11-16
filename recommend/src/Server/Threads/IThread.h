#ifndef ITHREAD_H
#define ITHREAD_H
#include <thread>

class IThread {
public:
    // Virtual destructor to ensure proper cleanup of derived classes.
    virtual ~IThread() = default;

    // Pure virtual method to start the thread.
    virtual void start() = 0;

    // Pure virtual method to join the thread (wait for it to finish).
    virtual void join() = 0;

    // Pure virtual method to get access to the thread object.
    virtual std::thread& getThread() = 0;
};

#endif // ITHREAD_H
