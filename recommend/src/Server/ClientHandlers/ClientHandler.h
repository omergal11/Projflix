#ifndef CLIENT_HANDLER_H
#define CLIENT_HANDLER_H

#include "IClientHandler.h"  
#include "../../App/Menus/SocketMenu.h"      
#include "../../App/App.h"             
#include "../../App/Parser.h"          
#include <unistd.h>          

class ClientHandler : public IClientHandler {
public:
    //constructor
    ClientHandler(int clientSocket, App* app);

    //function to handle a client
    void handleClient() override;


private:
    int clientSocket;  // The socket used for communication with the client
    App* app;          // Pointer to the App instance for business logic
 
};

#endif // CLIENT_HANDLER_H
