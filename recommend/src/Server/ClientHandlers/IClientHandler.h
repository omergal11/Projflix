#ifndef ICLIENT_HANDLER_H
#define ICLIENT_HANDLER_H
class IClientHandler {
public:
    // Virtual destructor to allow for proper cleanup of derived classes.
    virtual ~IClientHandler() = default;
    //Handles the communication and logic for a single client.
    virtual void handleClient() = 0;
};

#endif