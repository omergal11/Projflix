#include "ClientHandler.h"
#include <iostream>
#include <vector>
#include <ostream>
using namespace std;

ClientHandler::ClientHandler(int clientSocket, App* app):
clientSocket(clientSocket), app(app) {}

void ClientHandler::handleClient() {
    //initiakize socketMenu
    IMenu* menu = new SocketMenu(clientSocket);
    try{
    while (true){
        //read the command from client
       string input = menu->nextCommand();
       std::pair<std::string, std::vector<unsigned long int>> parsedInput = Parser::checkInput(input);
       std::stringstream response;
       app->run(parsedInput,response);
       menu->sendResponse(response.str());
    }
   } catch (const std::runtime_error& e) {
        // Handle socket errors or disconnections
        close(clientSocket);
    } catch (const std::exception& e) {
        // Handle any other standard exceptions
        close(clientSocket);
    } catch (...) {
        // Handle unknown errors
        close(clientSocket);
    }
    delete menu;

}