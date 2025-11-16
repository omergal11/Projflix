#ifndef SOCKETMENU_H
#define SOCKETMENU_H
#include "IMenu.h"
#include <string>
#include <vector>

class SocketMenu : public IMenu {
private:
    int clientSocket;    // Client socket file descriptor

public:
    // Constructor that accepts an existing client socket and initializes SocketStream
    SocketMenu(int clientSocket);

    // Function to read the next command from the socket
    std::string nextCommand();
    //Sends the response to the client via the socket.
     void sendResponse(const std::string& response);

};

#endif // SOCKETMENU_H