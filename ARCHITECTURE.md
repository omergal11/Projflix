# 🏗️ System Architecture Documentation

## Table of Contents
- [Overview](#overview)
- [Architecture Diagram](#architecture-diagram)
- [Components](#components)
- [Data Flow](#data-flow)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Deployment](#deployment)

---

## Overview

ProjFlix is a **microservices-based** application architecture designed for scalability, maintainability, and performance. The system separates concerns across multiple services, each handling specific responsibilities.

### Architecture Principles
- **Microservices**: Each component is a separate, independently deployable service
- **Containerization**: Docker ensures consistency across development and production
- **Separation of Concerns**: Frontend, API, Database, and Recommendation Engine are decoupled
- **Scalability**: Services can be scaled independently based on demand
- **Stateless API**: Enables horizontal scaling without session management issues

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                          CLIENT LAYER                                │
├──────────────────────────┬──────────────────────┬───────────────────┤
│   React Web App          │   Android App        │   Admin Portal    │
│   (React)                │   (Native)           │   (React)         │
│   localhost:3000         │   10.0.2.2 (emu)     │   localhost:3000  │
└──────────────────────────┴──────────────────────┴───────────────────┘
                      ↓ HTTP/REST API
┌─────────────────────────────────────────────────────────────────────┐
│                      API GATEWAY LAYER                               │
├─────────────────────────────────────────────────────────────────────┤
│   Node.js Express Server (port 3001) - Docker Container             │
│   ├─ Authentication (JWT)                                           │
│   ├─ Route Handlers                                                 │
│   ├─ Error Handling Middleware                                      │
│   ├─ File Upload Handling (Multer)                                  │
│   └─ CORS Configuration                                             │
└─────────────────────────────────────────────────────────────────────┘
       ↓                          ↓                         ↓
┌──────────────────┐  ┌──────────────────┐  ┌────────────────────────┐
│   DATABASE       │  │ RECOMMENDATION   │  │ FILE STORAGE           │
│   MongoDB        │  │ ENGINE           │  │ Local Filesystem       │
│   (port 27017)   │  │ C++ Server       │  │ (Multer uploads)       │
│                  │  │ (port 5555)      │  │ /api/uploads/          │
│   Collections:   │  │                  │  │ Movies, Posters,       │
│   - users        │  │ TCP Socket       │  │ Trailers, Files        │
│   - movies       │  │ Communication    │  │                        │
│   - categories   │  │                  │  │ Persistent Volume      │
│   - roles        │  │ Model: Trained   │  │ (Docker)               │
│                  │  │ Data Patterns    │  │                        │
└──────────────────┘  └──────────────────┘  └────────────────────────┘
```

---

## 🧩 Components

### 1. Frontend Layer

#### React Web Application
- **Location**: `netflix/`
- **Port**: 3000
- **Technology**: React, JavaScript, CSS3
- **Responsibilities**:
  - User interface rendering
  - Client-side routing (React Router)
  - State management
  - HTTP requests to API

#### Android Application
- **Location**: `Netflix android/`
- **Technology**: Kotlin/Java, Android SDK
- **Responsibilities**:
  - Native mobile interface
  - Local storage (SharedPreferences)
  - Camera/file access
  - Push notifications (optional)

---

### 2. API Layer

#### Node.js Express Server
- **Location**: `api/`
- **Port**: 3001
- **Container**: Docker (app_server)

**Responsibilities**:
- HTTP request routing
- User authentication/authorization
- Movie CRUD operations
- Category management
- File upload handling
- Data validation
- Error handling

**Key Middleware**:
```
Request → CORS → BodyParser → Authentication → Route Handler → Response
```

**Main Routes**:
- `/api/users` - User management
- `/api/movies` - Movie operations
- `/api/categories` - Category operations
- `/api/recommendations` - Recommendation requests
- `/api/tokens` - Token management

---

### 3. Database Layer

#### MongoDB
- **Port**: 27017
- **Connection String**: `mongodb://host.docker.internal:27017`
- **Container**: mongo

**Collections**:

##### Users Collection
```json
{
  "_id": ObjectId,
  "email": "user@example.com",
  "password": "hashed_password",
  "username": "username",
  "profilePicture": "url",
  "role": "user|admin",
  "createdAt": ISODate
}
```

##### Movies Collection
```json
{
  "_id": ObjectId,
  "title": "Movie Title",
  "description": "...",
  "duration": 120,
  "director": "Director Name",
  "cast": ["Actor 1", "Actor 2"],
  "releaseYear": 2024,
  "categories": [ObjectId],
  "posterImage": "url",
  "trailer": "url",
  "movieFile": "url",
  "rating": 8.5,
  "views": 1000,
  "createdAt": ISODate
}
```

##### Categories Collection
```json
{
  "_id": ObjectId,
  "name": "Action",
  "description": "...",
  "isPromoted": true,
  "createdAt": ISODate
}
```

---

### 4. Recommendation Engine

#### C++ Server
- **Location**: `recommend/`
- **Port**: 5555
- **Container**: Docker (recommender_server)

**Functionality**:
- Analyzes user viewing patterns
- Computes movie similarity
- Generates personalized recommendations
- Uses collaborative filtering

**Communication**:
- TCP socket communication with Node.js API
- Request format: JSON over TCP
- Response: Movie IDs and similarity scores

---

### 5. File Storage

#### Local Filesystem
- **Path**: `api/uploads/`
- **Types**:
  - User profile pictures: `api/uploads/usersImages/`
  - Movie posters: `api/uploads/moviePosters/`
  - Trailers: `api/uploads/trailers/`
  - Full movies: `api/uploads/movies/`

**Docker Volume**:
```yaml
volumes:
  - uploads_data:/usr/src/app/uploads
```

---

## 📊 Data Flow

### User Authentication Flow
```
1. User enters credentials
   ↓
2. Frontend sends POST /api/users/login
   ↓
3. API validates credentials against MongoDB
   ↓
4. Password verified (bcrypt)
   ↓
5. JWT token generated and signed
   ↓
6. Token returned to frontend
   ↓
7. Frontend stores token in localStorage
   ↓
8. Subsequent requests include token in header
   ↓
9. API middleware verifies JWT
```

### Movie Search & Recommendation Flow
```
1. User searches for movie
   ↓
2. Frontend queries API /api/movies/search
   ↓
3. API searches MongoDB movies collection
   ↓
4. Results returned with relevance ranking
   ↓
5. User clicks on movie
   ↓
6. Frontend requests similar movies /api/recommendations/{movieId}
   ↓
7. API forwards request to C++ recommendation engine
   ↓
8. C++ engine calculates similarity scores
   ↓
9. Top recommendations returned to frontend
   ↓
10. Frontend displays recommendations
```

### File Upload Flow
```
1. User selects file
   ↓
2. Frontend creates FormData with file
   ↓
3. POST request to /api/movies/upload
   ↓
4. Multer middleware validates file type
   ↓
5. File saved to /api/uploads/{category}/
   ↓
6. URL returned to frontend
   ↓
7. URL stored in MongoDB
   ↓
8. Frontend can access via GET request
```

---

## 🗄️ Database Schema

### Relationships
```
Users (1) ──→ (many) Movies (created by admin)
         ├──→ (many) ViewHistory
         └──→ (many) Recommendations

Movies (many) ←──→ (many) Categories
      ├──→ (1) Director
      └──→ (many) Cast Members
```

### Indexes
```
Users:
  - _id (primary)
  - email (unique)
  - username (unique)

Movies:
  - _id (primary)
  - title (indexed for search)
  - categories (indexed)
  - releaseYear (indexed)

Categories:
  - _id (primary)
  - name (unique)
  - isPromoted (indexed)
```

---

## 🔌 API Endpoints

### Authentication
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/users/register` | No | Register new user |
| POST | `/api/users/login` | No | User login |
| GET | `/api/users/profile` | Yes | Get user profile |
| PUT | `/api/users/profile` | Yes | Update profile |
| GET | `/api/tokens/refresh` | Yes | Refresh JWT token |

### Movies
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/movies` | No | List all movies |
| GET | `/api/movies/:id` | No | Get movie details |
| POST | `/api/movies` | Yes (Admin) | Create movie |
| PUT | `/api/movies/:id` | Yes (Admin) | Update movie |
| DELETE | `/api/movies/:id` | Yes (Admin) | Delete movie |
| GET | `/api/movies/search?q=` | No | Search movies |

### Categories
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/categories` | No | List all categories |
| POST | `/api/categories` | Yes (Admin) | Create category |
| PUT | `/api/categories/:id` | Yes (Admin) | Update category |
| DELETE | `/api/categories/:id` | Yes (Admin) | Delete category |

### Recommendations
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/recommendations/:movieId` | No | Get recommendations |
| POST | `/api/recommendations` | Yes | Update recommendations |

### File Upload
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/movies/upload` | Yes (Admin) | Upload movie files |
| GET | `/uploads/:path` | No | Download/view file |

---

## 🐳 Docker Compose Configuration

```yaml
services:
  mongo:
    image: mongo
    ports: ["27017:27017"]
    volumes: [mongo_data:/data/db]

  app:
    build: ./api
    ports: ["3001:3001"]
    depends_on: [mongo, recommender]
    environment:
      - CONNECTION_STRING=mongodb://mongo:27017
      - JWT_SECRET=${JWT_SECRET}

  react:
    build: ./netflix
    ports: ["3000:3000"]
    environment:
      - REACT_APP_API_URL=http://localhost:3001

  recommender:
    build: ./recommend
    ports: ["5555:5555"]

volumes:
  uploads_data: {}
  mongo_data: {}

networks:
  app_network:
    driver: bridge
```

---

## 🚀 Deployment

### Development Environment
- Local machine with Docker
- MongoDB running locally
- Hot module reloading for React
- Debug mode for Node.js

### Production Environment
```
Recommended Setup:
- Container orchestration (Kubernetes)
- Cloud provider (AWS/GCP/Azure)
- Load balancer
- CDN for static assets
- Database replica sets
- Monitoring & logging (ELK stack)
```

### Environment Variables
```
APP_PORT=3001
CONTAINER_PORT=3001
RECOMMENDATION_PORT=5555
CONNECTION_STRING=mongodb://mongo:27017
JWT_SECRET=your-secret-key
REACT_APP_API_URL=http://localhost:3001
NODE_ENV=production
```

---

## 🔒 Security Considerations

### Authentication
- JWT tokens with expiration
- Bcrypt for password hashing
- Refresh token rotation

### Authorization
- Role-based access control (RBAC)
- Admin-only endpoints
- User data isolation

### API Security
- CORS configuration
- Input validation
- Rate limiting (recommended)
- SQL injection prevention
- XSS protection

### File Upload Security
- File type validation
- File size limits
- Virus scanning (recommended)
- Secure file storage

---

## 📈 Scalability Considerations

### Horizontal Scaling
- Stateless API servers
- Load balancer distribution
- MongoDB replica sets

### Vertical Scaling
- Increase container resources
- Database optimization
- Caching layer (Redis)

### Performance Optimization
- Database indexing
- Query optimization
- Frontend code splitting
- Image optimization
- Lazy loading

---

## 🔍 Monitoring & Logging

### Recommended Tools
- **Monitoring**: Prometheus, Grafana
- **Logging**: ELK Stack (Elasticsearch, Logstash, Kibana)
- **APM**: New Relic, Datadog
- **Error Tracking**: Sentry

### Metrics to Track
- API response time
- Database query performance
- Error rates
- User engagement
- System resource usage

---

## 🔗 Related Documentation
- [Main README](README.md)
- [Web App](wiki/web.md)
- [Android App](wiki/android.md)

---

**Last Updated**: February 2026
