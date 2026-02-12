# 🎬 ProjFlix - Complete Movie Management System

A full-stack movie management application with web and mobile interfaces, featuring an intelligent recommendation engine built in C++.

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [System Architecture](#system-architecture)
- [Prerequisites](#prerequisites---installation-requirements)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [Documentation](#documentation)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

ProjFlix is a **full-stack movie management system** that provides seamless browsing, searching, and management of movies across web and mobile platforms. The application features:

- ✅ **Multi-platform support**: React web app + Android native app
- ✅ **User authentication**: JWT-based secure authentication
- ✅ **Admin dashboard**: Manage movies, categories, and users
- ✅ **Smart recommendations**: AI-powered C++ recommendation engine
- ✅ **Microservices architecture**: Dockerized, scalable, maintainable
- ✅ **Professional design**: Dark-themed, responsive UI

---

## ⚡ Features

### For Users
- 🔐 **Authentication**: Sign up and log in with email/password
- 🎥 **Browse Movies**: View movies by categories with trailers
- 🔍 **Search & Filter**: Find movies by title, category, or rating
- 👤 **Profile Management**: Update profile picture and personal info
- ⭐ **Smart Recommendations**: Get personalized movie recommendations
- 📱 **Cross-platform**: Access via web or mobile (Android)

### For Admins
- 📝 **Content Management**: Add, edit, delete movies and categories
- 👥 **User Management**: Monitor and manage users
- 📊 **Analytics**: Track system usage and recommendations
- 🎬 **Media Upload**: Upload movie trailers, posters, and files

---

## 🛠 Technology Stack

| Component | Technology | Purpose |
|-----------|-----------|---------|
| **Frontend (Web)** | React, JavaScript, CSS | User interface for web platform |
| **Frontend (Mobile)** | Android (Kotlin/Java), Gradle | Native Android application |
| **Backend API** | Node.js, Express, Multer | REST API and file handling |
| **Database** | MongoDB | Movie, user, and category data |
| **Recommendation Engine** | C++ | Machine learning-based recommendations |
| **Containerization** | Docker, Docker Compose | Microservices orchestration |
| **Authentication** | JWT, bcrypt | Secure user authentication |
| **File Storage** | Local filesystem, Multer | Media file management |

---

## 🏗 System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      Client Layer                            │
├──────────────────────┬──────────────────────┬────────────────┤
│   React Web App      │  Android App         │  Admin Panel   │
│  (localhost:3000)    │  (Native)            │  (React)       │
└──────────────────────┴──────────────────────┴────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                    API Layer (Docker)                        │
├──────────────────────────────────────────────────────────────┤
│  Node.js Express Server (port 3001)                          │
│  - User authentication (JWT)                                 │
│  - Movie CRUD operations                                     │
│  - Category management                                       │
│  - File uploads (Multer)                                     │
└──────────────────────────────────────────────────────────────┘
         ↓                              ↓
┌──────────────────────┐    ┌──────────────────────┐
│  MongoDB Database    │    │ C++ Recommendation   │
│  (port 27017)        │    │ Engine (port 5555)   │
└──────────────────────┘    └──────────────────────┘
```

For detailed architecture information, see [ARCHITECTURE.md](ARCHITECTURE.md)

---

## 🚀 Quick Start

### 1. Prerequisites - Installation Requirements

Before running the project, you **must install the following**:

#### Docker
- Download [Docker Desktop](https://www.docker.com/products/docker-desktop)
- Verify: `docker --version && docker-compose --version`

#### MongoDB
- Download [MongoDB Community Edition](https://www.mongodb.com/try/download/community)
- Verify: `mongosh` (opens MongoDB shell)
- **Alternative**: Run in Docker: `docker run -d --name mongo -p 27017:27017 mongo`

#### Android Studio (Optional - for mobile app)
- Download [Android Studio](https://developer.android.com/studio)

### 2. Clone & Setup

```bash
# Clone repository
git clone https://github.com/omergal11/Projflix.git
cd projflix

# Create .env.local in config directory
cat > config/.env.local << EOF
APP_PORT=3001
CONTAINER_PORT=3001
RECOMMENDATION_PORT=5555
CONNECTION_STRING=mongodb://host.docker.internal:27017
JWT_SECRET="Vj4@7sF!9K#pLz^D2o7uN13X6A9Q5"
REACT_APP_API_URL=http://localhost:3001
REACT_PORT=3000
EOF
```

### 3. Build & Run

```bash
# Build Docker images
docker-compose --env-file ./config/.env.local up --no-start

# Start services
docker-compose --env-file ./config/.env.local up -d

# Verify services are running
docker ps
```

### 4. Access Application

- **Web App**: [http://localhost:3000](http://localhost:3000)
- **API**: [http://localhost:3001/api](http://localhost:3001/api)
- **MongoDB**: localhost:27017

---

## 📁 Project Structure

```
projflix/
├── api/                          # Node.js Backend
│   ├── controllers/              # Request handlers
│   ├── models/                   # MongoDB schemas
│   ├── routes/                   # API endpoints
│   ├── services/                 # Business logic
│   ├── middlewares/              # Auth, file upload
│   ├── app.js
│   ├── Dockerfile.server
│   └── package.json
├── netflix/                      # React Web App
│   ├── src/
│   │   ├── components/           # Reusable components
│   │   ├── pages/                # Page components
│   │   ├── services/             # API calls
│   │   └── App.js
│   ├── Dockerfile.react
│   └── package.json
├── Netflix android/              # Android App
│   ├── app/
│   │   ├── src/main/             # Source code
│   │   └── build.gradle.kts
│   └── gradle/
├── recommend/                    # C++ Recommendation Engine
│   ├── src/
│   │   ├── App/                  # Core logic
│   │   └── Server/               # TCP Server
│   ├── Dockerfile.recommender
│   └── CMakeLists.txt
├── config/                       # Configuration files
│   └── .env.local                # Environment variables
├── wiki/                         # Documentation
│   ├── web.md                    # Web app docs
│   ├── android.md                # Android app docs
│   └── webDoc/, androidDoc/      # Screenshots
├── docker-compose.yml            # Microservices orchestration
└── README.md                     # This file
```

---

## 📚 Documentation

- **[Web Application](wiki/web.md)** - React frontend features and components
- **[Android Application](wiki/android.md)** - Android app setup and usage
- **[System Architecture](ARCHITECTURE.md)** - Detailed system design

---

## 🤖 Running the Android Application

### Setup Steps

1. **Install Android Studio**:
   - Download from [Android Studio](https://developer.android.com/studio)
   - Import the `Netflix android` directory

2. **Configure `config.properties`**:
   - Navigate to: `Netflix android/app/src/main/res/raw/config.properties`
   - Set your local IP and ports:
   ```properties
   ip_address=10.0.2.2          # For emulator; use your IP for physical device
   port=3001                    # Match APP_PORT from .env.local
   jwt_secret=Vj4@7sF!9K#pLz^D2o7uN13X6A9Q5
   ```

3. **Update Network Security**:
   - Edit: `Netflix android/app/src/main/res/xml/network_security_config.xml`
   - Add your IP address for cleartext traffic permission

4. **Run the App**:
   - Click **"Run"** in Android Studio
   - Select emulator or device

---

## 📝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -m 'Add your feature'`
4. Push to branch: `git push origin feature/your-feature`
5. Open a Pull Request

---

## 👨‍💻 Author

**Omer Gal**  
- GitHub: [@Omergal11](https://github.com/omergal11)

---

## 🙏 Acknowledgments

- MongoDB for the database
- Express.js and Node.js for backend
- React for frontend
- Android development community
- C++ for recommendation engine

---