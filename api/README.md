# Netflix-part-3-MongoDB
## Table of Contents
- [Overview](#Overview)
- [docker and explanations](#Commands-for-Running-the-Project)
- [Running samples](#RESTful-API-Endpoints)

## Overview - New Node.js Server Integration

In this part of the project, we built the entire backend functionality that supports the Netflix-like application. The new **Node.js server** serves as the backbone for managing users, movies, categories, and recommendations, creating a seamless experience for interacting with the system. 

The Node.js server acts as an intermediary between the client-facing application and the **recommendation system** (implemented in C++). It provides all the core logic and RESTful API endpoints needed to handle user requests efficiently, ensuring proper data management and smooth communication with the recommendation system.

This integration enables:
1. **User Management**: Allows creating, retrieving, and managing user profiles.
2. **Movie Management**: Supports adding, updating, and retrieving movies.
3. **Category Management**: Enables categorizing movies for better organization.
4. **Recommendations**: Facilitates fetching recommendations from the C++ server and managing user viewing data to enhance the recommendation system.
5. **query**: Enables searching movie.

All interactions between the client application and the recommendation system are routed through the Node.js server, ensuring a unified and secure backend infrastructure.
.

---

## Architecture Overview

### Node.js Server

- Built using **Express** and **MongoDB** for efficient and scalable backend services.
- Provides RESTful API endpoints for CRUD operations on users, movies, categories and searching.
- Communicates with the recommendation system (C++ server) via socket connections to fetch recommendations or update recommendation data.

### Recommendation System (C++ Server)

- Manages recommendation data and processes requests from the Node.js server.
- Runs as a standalone service and listens on a specific port.
- using threadpoll

---

## RESTful API Endpoints

### User Endpoints

1. **Create a User**

   POST /api/users
   
   - Request Body:
     
       {
         "email": "string", (must be in formt /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.(com|co\.il)$/ )
         "password": "string",
         "profilePicture": "string (optional)"
       }
       ![alt Text](<samples/postuser.png>)
   
2. **Get a User**

   GET /api/users/:id

   ![alt Text](<samples/getuser.png>)

### token Endpoints

POST /api/tokens

 - Request Body:
    {
         "email": "string", (must be in formt /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.(com|co\.il)$/ )
         "password": "string"
       }
       returnd the id of the user (this is the log-in)
   ![alt Text](<samples/posttokens.png>)

## All this commands require that the user is connect (by header id)
### category Endpoints
1. **Create a category**

   POST /api/categories
   
   - Request Body:
     
       {
         "name": "string",
         "promoted":bollean
       }
       ![alt Text](<samples/postcategory.png>)

2. **Get categories**

    GET /api/categories
   ![alt Text](<samples/getcategories.png>)
4. **get category by id**

    GET /api/categories/:id

    ![alt Text](<samples/getidcategory.png>)

5. **Patch category**

    PATCH /api/categories/:id
    - Request Body:
     
       {
         "name": "string",(optional)
         "promoted": "string" (optional)
       }
    ![alt Text](<samples/patchc.png>)

6. **Delete category**

    DELETE /api/categories/:id

    ![alt Text](<samples/deletecat.png>)



### Movie Endpoints

1. **Create a Movie**

   POST /api/movies
   
   - Request Body:
     
       {
         "name": "string",
         "minutes": : "Number"
         "description": "string",
         "releaseYearr: "Number"
         "category": "[string]",
         "mainImage": "String",
         "images": [string],
         "director": "string",
         "cast" : "string",
         "trailer" : "string"

       }
         ![alt Text](<samples/postmovie.png>)
  
     
2. **Get a Movie  by id**

    GET /api/movies/:id
   
     ![alt Text](<samples/getidmov.png>)

3. **Get novie**

    GET /api/movies

     ![alt Text](<samples/getmovies.png>)

4. **PUT movie**  

    PUT /api/movies/:id

     ![alt Text](<samples/putmov.png>)

5. **Delete movie**

    DELETE /api/movies/:id

     ![alt Text](<samples/delemovie.png>)

### Recommendation Endpoints

1. **Get Recommendations**

   GET /api/movies/:id/recommend
   
   ![alt Text](<samples/getrec.png>)

2. **Post Watch Data**

   POST /api/movies/:id/recommend

- Notifies the recommendation system that a user has watched a movie.

   ![alt Text](<samples/postrecommend.png>)
   
### query 

**Search movie** 

GET /api/movies/search/:query 

   ![alt Text](<samples/query.png>)

---

## Repository Clone

   https://github.com/omergal11/Netflix-part-3-MongoDB.git
---

## Commands for Running the Project
Create a configuration file: Inside the config directory, create a file named .env.local with the following structure:
```bash
APP_PORT=3001  # The port on the host machine where the Node.js server will be accessible.(your choice)
CONTAINER_PORT=3000  # The port inside the container where the Node.js server listens.(your choice)
RECOMMENDATION_PORT=12345  # The port on which the recommendation server (C++) will run.(your choice)
CONNECTION_STRING=mongodb://host.docker.internal:27017  # Database connection string.(exactly like its here)
```
## sample:

   ![alt Text](<samples/config.png>)

2. **Explanation of the variables:**

- **APP_PORT**: Defines the port on your local machine (host) to access the Node.js server.This can be customized as needed during setup.
- **CONTAINER_PORT**: Defines the port inside the container where the Node.js server runs.This can be customized as needed during setup.
- **RECCOMENDATION_PORT**: Defines the port for the recommendation server. This can be customized as needed during setup.
- **CONNECTION_STRING**: Specifies the connection string for your MongoDB database.

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
## stop the containers :
1. Stop the Recommendation System (C++ Server):
```bash
docker stop recommender_server
 ```
 2. Stop the Node.js Server:
```bash
docker stop app_server
 ``` 




