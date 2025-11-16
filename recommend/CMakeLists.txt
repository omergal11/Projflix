cmake_minimum_required(VERSION 3.10)

# Project name
project(MyProject)

# Set C++ standard
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Add the main application
add_executable(myapp
    src/Server/mainServer.cpp
    src/Server/TcpServer.cpp
    src/Server/Threads/ThreadPerClient.cpp
    src/Server/ClientHandlers/ClientHandler.cpp
    src/App/Menus/SocketMenu.cpp
    src/App/App.cpp
    src/App/Parser.cpp
    src/App/DataFileManager.cpp
    src/App/Commands/AddCommand.cpp
    src/App/Commands/Recommend.cpp
    src/App/Commands/HelpCommand.cpp
    src/App/Commands/PostCommand.cpp
    src/App/Commands/PatchCommand.cpp
    src/App/Commands/DeleteCommand.cpp
    src/App/Commands/GetCommand.cpp
    src/Server/Threads/ThreadManager.cpp
    src/Server/ClientHandlers/ClientHandlerFactory.cpp
    src/Server/Threads/ThreadPoolManager.cpp
)

# Include directories for the main application
target_include_directories(myapp PRIVATE src)