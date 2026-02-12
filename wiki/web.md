
# 🌐 Web Application Documentation

## Table of Contents
- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Pages](#pages)
- [Components](#components)
- [Admin Instructions](#instructions-for-admin)
- [Getting Started](#getting-started)

## Overview

The ProjFlix web application is a **React-based** frontend for a **Movie Management System** that integrates seamlessly with the **Node.js backend**. The system allows users to **browse, search, add, edit, and delete movies and categories** while maintaining user authentication and admin roles.

### Key Features
- 🎬 Browse movies by categories
- 🔍 Search and filter movies
- 👤 User authentication with JWT
- 🎥 Watch trailers and full movies
- ⭐ Get personalized recommendations
- 👨‍💼 Admin dashboard for content management
- 🌙 Light/Dark mode support

---

## 🛠 Tech Stack

| Technology | Purpose |
|-----------|---------|
| **React** | UI framework |
| **JavaScript (ES6+)** | Core language |
| **CSS3** | Styling |
| **Fetch API** | HTTP requests |
| **JWT** | Authentication |
| **Docker** | Containerization |

---

## 📖 Pages

### 1. Welcome Page
- Landing page of the application
- Displays hero banner with application overview
- Call-to-action buttons for signup/signin

![alt text](webDoc/welcome.png)

---

### 2. SignUp Page
- Allows new users to create an account
- Fields:
  - 📧 Email
  - 🔐 Password
  - 🔐 Confirm Password
  - 📸 Profile Picture

![alt text](webDoc/signup.png)

---

### 3. SignIn Page
- User login interface
- Fields:
  - 📧 Email
  - 🔐 Password
- JWT token stored in localStorage

![alt text](webDoc/login.png)

---

### 4. HomePage
- Displays movies organized by promoted categories
- Features:
  - **MedMovie components** with auto-playing trailers
  - Category sections
  - **HomeVideoPlayer** background video

![alt text](webDoc/homePage.png)

---

### 5. Movies Page
- Browse all available movies
- Features:
  - Random featured movie with trailer loop
  - Categorized movie display
  - MovieList component

![alt text](webDoc/movies.png)

---

### 6. Admin Page
Comprehensive admin dashboard for content management.

#### Admin Actions:

**Add Movie**
- Fill in movie details (title, description, duration, etc.)
- Upload: Main poster image, trailer (mp4), full movie file (mp4)
- Note: Categories and cast separated by commas (`,`)

**Edit Movie**
- Search for existing movie
- Update any field
- Re-upload media files if needed

**Delete Movie**
- Search for movie
- Delete from database

**Add Category**
- Create new movie categories
- Set as promoted/featured

**Edit Category**
- Modify category details

**Delete Category**
- Remove category

![alt text](webDoc/adminOptions.png)

---

### 7. Search Page
- Real-time movie search functionality
- Features:
  - **SearchBox component** with dynamic results
  - Filter by title, category, rating

![alt text](webDoc/search.png)

---

### 8. Profile Page
- User profile management
- Features:
  - Display user info (email, username)
  - Update profile picture
  - Change password
  - View personal preferences

![alt text](webDoc/profile.png)

---

### 9. Video Player Page
- Full-screen movie player
- Features:
  - Play/pause controls
  - Volume control
  - Fullscreen mode
  - Progress bar with seek

![alt text](webDoc/videoPlay.png)

---

### Navbar
Navigation bar available on all pages:
- 🏠 Home
- 🎬 Movies
- 👨‍💼 Admin Area (admin only)
- 🔍 Search
- 👤 Profile
- 🚪 Logout
- 🌙 Light/Dark Mode Toggle

![alt text](webDoc/nav.png)

---

### Footer
Site footer with links and credits

![alt text](webDoc/footer.png)

---

## 🧩 Components

### 1. MovieComponent
- Small movie card component
- Displays movie poster
- Click to view details

![alt text](webDoc/mov.png)

---

### 2. MedMovie
- Medium-sized movie card
- Features:
  - Auto-playing trailer preview
  - Release year
  - Categories

![alt text](webDoc/med.png)

---

### 3. MainMovie
- Large movie detail component
- Features:
  - Full trailer video
  - Complete movie information (title, description, duration, cast, etc.)
  - Recommended movies section

![alt text](webDoc/main1.png)
![alt text](webDoc/similar.png)
![alt text](webDoc/main2.png)

---

### 4. MovieList
- Dynamic list renderer
- Maps array of movies to components
- Handles pagination

---

### 5. NavBar
- Header navigation component
- Responsive design
- User authentication state
- Theme toggle

![alt text](webDoc/nav.png)

---

### 6. Footer
- Site footer with links
- Copyright information

![alt text](webDoc/footer.png)

---

### 7. SimilarMovies
- Displays recommended/similar movies
- AI-powered recommendations from C++ engine

![alt text](webDoc/similar.png)

---

### 8. Popup
- Modal component for detailed movie information
- Shows all movie details
- Action buttons (watch, add to favorites, etc.)

![alt text](webDoc/popup.png)

---

### 9. HomeVideoPlayer
- Background video player
- Plays on homepage header
- Auto-play featured movie trailer

![alt text](webDoc/cover.png)

---

### 10. SearchBox
- Real-time search component
- Filters movies as user types
- Displays results dynamically

---

## 🔐 Instructions for Admin

### Setting Up Admin Account

1. **Create a user account** via SignUp page

2. **Open MongoDB Compass**:
   - Connect to `localhost:27017`
   - Navigate to database → `users` collection

3. **Modify User Role**:
   - Find your newly created user
   - Change `role` field from `"user"` to `"admin"`

![alt text](webDoc/user.png)
![alt text](webDoc/admin.png)

4. **Log out and Log back in** for changes to take effect

5. **Access Admin Panel**:
   - Click "Admin Area" in navbar
   - You now have full content management access

### Important Notes
- ⚠️ Only admins can add/edit/delete movies and categories
- ⚠️ You must log out and log in again after role change
- ⚠️ Categories and cast entries should be comma-separated
- ⚠️ File uploads: Images (jpg/png) and Videos (mp4 only)

---

## 🚀 Getting Started

1. **Install Dependencies**:
   ```bash
   cd netflix
   npm install
   ```

2. **Start Development Server**:
   ```bash
   npm start
   ```
   - Opens on [http://localhost:3000](http://localhost:3000)

3. **Build for Production**:
   ```bash
   npm run build
   ```

4. **Run with Docker**:
   ```bash
   docker-compose --env-file ./config/.env.local up
   ```

---

## 🔗 Related Documentation
- [Main README](../README.md)
- [Android App](android.md)
- [Architecture](../ARCHITECTURE.md)

---

**Last Updated**: February 2026

## Pages

### 1. Welcome Page
- The landing page of the application.
- Displays a hero banner or introductory section.

  ![alt text](webDoc/welcome.png)


### 2. SignUp Page
- Allows new users to **create an account**.
- Contains fields for **email, password, confirm password, and profile picture**.

![alt text](webDoc/signup.png)

### 3. SignIn Page
- Enables existing users to **log in**.
- Requires **email and password**.

![alt text](webDoc/login.png)

### 4. HomePage
- Displays **movies categorized by promoted categories**.
- Each movie appears as a **medium-sized movie component** with trailer previews.

  ![alt text](webDoc/homePage.png)


### 5. Movies Page
- Display random movie that his trailer playing on loop
- Displays **all available movies** categorized properly.
- Uses a **movie list** and filterable movie display.

![alt text](webDoc/movies.png)

### 6. Admin Page

![alt text](webDoc/adminPage.png)

- Provides **administrative actions**:
  - Add Movie 
  **important:** categories and cast you write with ',' seperate between every string 
  - Edit Movie 
  **instructions**: search the movie you want to edit and then fill all
  - Delete Movie
  **instructions**: search the movie and delete him
  - Add Category
  - Edit Category
  - Edit Category 


 <p align="center">
  <img src="webDoc/adminOptions.png" width="600">
 </p>

### 7. Search Page
- Allows users to **search movies dynamically**.
- Uses the **SearchBox component** which fetches results in real-time.

![alt text](webDoc/search.png)

## On the navbar if you will open the optiond drop down: 

![alt text](webDoc/navBar.png)

### 8. Profile Area
- Displays user **profile details**.
- Allows updates to **profile settings, preferences, and password**.

![alt text](webDoc/profile.png)

### 9. Video player
the page where you can watch the full movie

![alt text](webDoc/videoPlay.png)


---

## Components

### 1. `MovieComponent`
- Small movie component displaying **only the movie poster**.

  ![alt text](webDoc/mov.png)


### 2. `MedMovie`
- Medium-sized movie component showing:
  - Movie trailer playing auttomaticly
  - Release year
  - Categories

  ![alt text](webDoc/med.png)



### 3. `MainMovie`
- Large movie component displaying:
  - Movie trailer
  - **All details** (title, description, duration, etc.)
  - **Recommended movies** based on selected movie

    ![alt text](webDoc/main1.png)
    ![alt text](webDoc/similar.png)
    ![alt text](webDoc/main2.png)

### 4. `MovieList`
- Component that **renders a list of movies** dynamically.

### 5. `NavBar`
- Navigation bar containing:
  - **home**
  - **movies**
  - **adnin area (for admins only)**
  - **Search**
  - **Profile area**
  - **Logout button**
  - **Light mode toggle**

  ![alt text](webDoc/nav.png)


### 6. `Footer`
- Footer section of the site with links and credits.
![alt text](webDoc/footer.png)


### 7. `SimilarTable`
- Displays **movies similar** to the currently viewed movie.
![alt text](webDoc/similar.png)

### 8. `Popup`
- Used for displaying detailed **movie information** in a modal.

  ![alt text](webDoc/popup.png)


### 9. `HomeVideoPlayer`
- A background video player appearing at the **top of the homepage**.
   ![alt text](webDoc/cover.png)


---
## instructions for admin 
after you create a user you shuld go to the data base **nongoDB compass** 
then you will see the new user under **users** 

![alt text](webDoc/user.png)

- change the role from **user** to **admin** 

![alt text](webDoc/admin.png)

**important**: to use the admin fetures you should log out and log in again. 
