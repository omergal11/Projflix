# projflix

## Table of Contents
- [Overview](#Overview)
- [docker and explanations](#Commands-for-Running-the-Project)
- [Running the Android Application](#running-the-android-application)

## Overview - Completion of the Project

In this phase, we have **completed the entire project**, including both the **frontend and backend**. We have successfully developed a **fully functional website and mobile application** that deliver a seamless user experience for browsing, managing, and recommending movies.

The system integrates a **React-based frontend** with a **Node.js backend**, allowing users to **search, add, edit, and delete movies and categories** while ensuring smooth interaction with the **recommendation system** implemented in C++.

For detailed explanations on each feature, component and pages, as well as usage examples, please refer to the **Wiki directory**.

---

## Repository Clone
  ```bash
   https://github.com/Odeddidi/projflix.git
   ```

## Commands for Running the Project
Create a configuration file: Inside the config directory, create a file named .env.local with the following structure:
```bash
APP_PORT=3001  # The port on the host machine where the Node.js server will be accessible.(your choice)
CONTAINER_PORT=4000  # The port inside the container where the Node.js server listens.(your choice)
RECOMMENDATION_PORT=12345  # The port on which the recommendation server (C++) will run.(your choice)
CONNECTION_STRING=mongodb://host.docker.internal:27017  # Database connection string.(exactly like its here)
JWT_SECRET="Vj4@7sF!9K#pLz^D2o7uN13X6A9Q5" # secret string for decodate token
REACT_APP_API_URL=http://localhost:3001 # a way to get to the api server(must be : http://localhost:{APP_PORT}} )
REACT_PORT=3000 #the port that the web running on 

```
## sample:

   ![alt Text](<samples/config.png>)

2. **Explanation of the variables:**

- **APP_PORT**: Defines the port on your local machine (host) to access the Node.js server.This can be customized as needed during setup.
- **CONTAINER_PORT**: Defines the port inside the container where the Node.js server runs.This can be customized as needed during setup.
- **RECCOMENDATION_PORT**: Defines the port for the recommendation server. This can be customized as needed during setup.
- **CONNECTION_STRING**: Specifies the connection string for your MongoDB database.
- **JWT_SECRET**: Defines the secret data to decodate token
- **REACT_APP_API_URL**: Defines the way to get the api server(must be : http://localhost:{APP_PORT} )
- **REACT_PORT**: Defines the port inside and ouside the container where the React  runs.This can be customized as needed during setup.
### Build the Docker Images
 ```bash
 docker-compose --env-file ./config/.env.local up --no-start
 ```
## start the containers: 
1. Start the Recommendation System (C++ Server):
```bash
docker start recommender_server
 ```
 2. Start the Node.js Server:
```bash
docker start app_server
 ```
 3. Start React: 
 ```bash
docker start react_app
 ```

## stop the containers :
1. Stop the Recommendation System (C++ Server):
```bash
docker stop recommender_server
 ```
 2. Stop the Node.js Server:
```bash
docker stop app_server
 ``` 
 3. Stop the React Server:
```bash
docker stop react_app
 ```  
## after build and run the containers 
### open the web: 
please refer to this address on your browser:
```bash
   http://localhost:3000
   ```
![alt Text](<samples/path.png>)

**the port should be the same one like you choosed on the config**

## Running the Android Application

### Prerequisites:
1. **Install Android Studio**:
   - Download and install [Android Studio](https://developer.android.com/studio) if you haven’t already.

2. **Open the Android Project**:
   - Open the **`Netflix-android`** directory in Android Studio.

### Configuration Steps:
1. **Create `config.properties`**:
   - Inside the project folder `Netflix-part-4-Web-Android\Netflix android\app\src\main\res`, create **`raw`** directory and inside the **`config.properties`** file.
   - 
   ![alt Text](<samples/loc_config_properties.png>)

    You need to write the **ip_address** , **port** and **jwt_secter** as follows:
 
- **ip_address**: Follow the instructions below

  - **Android Emulator**: If you are using the **Android Emulator**, use the IP `10.0.2.2` to refer to your host machine.
 
  - **Local machine**: If you’re running the application on your local machine, use your local IP address
 
  - To find your **IP address**, follow these steps: 
  - **Windows**: Open **Command Prompt** (`Win + R`, type `cmd`, press Enter), then run the command `ipconfig`. Look for the **"IPv4 Address"** under your network adapter (e.g., "Wi-Fi" or "Ethernet"). The           output will be something like `IPv4 Address. . . . . . . . . . . : 192.168.1.242`.
    ![alt Text](<samples/ipinfo.png>)
 
  - **macOS**: Open **Terminal**, then run `ifconfig`. Look for the `inet` address under the network interface (typically `en0` for Wi-Fi). The output will look like `inet 192.168.1.242 netmask 0xffffff00            broadcast 192.168.1.255`.
  
  - **Linux**: Open **Terminal**, then run `ifconfig` (or `ip a` for newer systems). Find the `inet` address under your network interface (e.g., `wlan0` for Wi-Fi). The output will look like `inet 192.168.1.242      netmask 255.255.255.0 broadcast 192.168.1.255`.

 
 - **port**: Use the same port as defined in your `.env.local` file for `APP_PORT`.

 - **jwt_secter** Use the same port as defined in your `.env.local` file for `JWT_SECRET`.

   Example:
   
```bash
ip_address=10.0.2.2
port=3001
jwt_secret=Vj4@7sF!9K#pLz^D2o7uN13X6A9Q5
```   

 **Update `network_security_config.xml`**:
   - After updating the `config.properties` file, you need to add the correct **IP address** or **Emulator address** to your `network_security_config.xml` file for network security permissions.
   - This file is located under the `res/xml` directory of your Android project
  
  ![alt Text](<samples/loc_network_sec.png>)

   - Add the following **IP addresses** to the `network_security_config.xml`:
     - **For local machine**: Use your local IP address (e.g., `192.168.x.x`).
     - **For Android Emulator**: Use `10.0.2.2` (this points to the host machine when running on the Android Emulator).

### Example:
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <network-security-config>
       <domain-config cleartextTrafficPermitted="true">
           <domain includeSubdomains="true">192.168.1.239</domain>  <!--If you use in local machine replace with your local machine IP -->
           <domain includeSubdomains="true">10.0.2.2</domain>  <!--If you use in Android Emulator -->
       </domain-config>
   </network-security-config>
   ```
   **make sure that all the servers are running first** [docker build and run](#Commands-for-Running-the-Project) (the recommend and app)

   **Running the Android Application**:
   - After starting the Docker containers, open **Android Studio**.
   - Open the `Netflix-Android` project folder.
   - Make sure you've completed the configuration steps mentioned earlier (updating `config.properties` and `network_security_config.xml`).
   - Once everything is set up, **run the Android app** on either an emulator or a physical device.
   
   The Android app should now connect to the backend API running inside the Docker containers, and you should be able to interact with the 
   system seamlessly.

   **Note**: Make sure the ports and IP addresses in the `config.properties` and `network_security_config.xml` files are correctly set to match 
   the values used in your Docker containers and the Android app.

   Once the app is launched and everything is set up, you're good to go! You should be able to browse, manage, and interact with the movies and 
   recommendation features seamlessly.
