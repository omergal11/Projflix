# 📱 Android Application Documentation

## Table of Contents
- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [User Features](#user-activities)
- [Admin Features](#admin-activities)
- [Admin Setup](#instructions-for-admin)
- [Getting Started](#getting-started)

## Overview

The ProjFlix Android application is a **native Android** frontend for the **Movie Management System** that integrates seamlessly with the **Node.js backend**. It provides a mobile-first experience for browsing, searching, and managing movies with full admin capabilities.

### Key Features
- 🎬 Browse movies by categories
- 🔍 Search movies in real-time
- 👤 User authentication with JWT
- 🎥 Watch trailers and full movies
- ⭐ Get personalized recommendations
- 👨‍💼 Full admin dashboard
- 🌙 Light/Dark mode support
- 🌍 Multi-language support (English & Spanish)

---

## 🛠 Tech Stack

| Technology | Purpose |
|-----------|---------|
| **Kotlin/Java** | Programming language |
| **Android SDK** | Native framework |
| **Gradle** | Build system |
| **RecyclerView** | List rendering |
| **Retrofit/Okhttp** | HTTP networking |
| **JWT** | Authentication |
| **Docker** | Containerization |

---

## 👤 User Activities

### 1. Welcome Activity
- Landing page with app introduction
- Call-to-action buttons for signup/login
- Available in both Dark and Light modes

<p align="center">
  <img src="androidDoc/landing_page.png" alt="Landing page" width="300"/>
</p>

**Light Mode:**
<p align="center">
  <img src="androidDoc/landing_page_light.png" alt="Landing page - Light Mode" width="300"/>
</p>

---

### 2. SignUp Activity
- Create new user account
- Fields:
  - 📧 Email
  - 🔐 Password
  - 🔐 Confirm Password
  - 📸 Profile Picture

<p align="center">
  <img src="androidDoc/sign_up_activity.png" alt="Sign Up Activity" width="300"/>
</p>

---

### 3. LogIn Activity
- User login interface
- Fields:
  - 📧 Email
  - 🔐 Password
- JWT token saved for session management

<p align="center">
  <img src="androidDoc/log_in_activity.png" alt="Log In Activity" width="300"/>
</p>

---

### 4. Home Activity
- Main feed displaying movies by categories
- Features:
  - Category sections using RecyclerView
  - Movie thumbnails with click navigation
  - Smooth scrolling experience

<p align="center">
  <img src="androidDoc/home_activity_user.png" alt="Home Activity" width="300"/>
</p>

---

### 5. Search Activity
- Real-time movie search
- Filter by title, category, rating
- Dynamic result updates

<p align="center">
  <img src="androidDoc/search_activity.png" alt="Search Activity" width="300"/>
</p>

**Light Mode:**
<p align="center">
  <img src="androidDoc/search_movie_light.png" alt="Search Activity - Light Mode" width="300"/>
</p>

---

### 6. Category Activity
- Browse movies by specific categories
- Uses RecyclerView for smooth scrolling
- Tap movie to view details

<p align="center">
  <img src="androidDoc/category_activity.png" alt="Category Activity" width="300"/>
</p>

---

### 7. Profile Activity
- User profile management
- Features:
  - Display profile information
  - Profile picture
  - Sign out button
  - Settings and preferences

<p align="center">
  <img src="androidDoc/user_details.png" alt="User Profile" width="300"/>
</p>

**Light Mode:**
<p align="center">
  <img src="androidDoc/user_details_light.png" alt="User Profile - Light Mode" width="300"/>
</p>

---

### 8. Movie Details Activity
- Comprehensive movie information
- Displays:
  - 🎬 Movie title and poster
  - ⏱️ Duration
  - 👨‍🎬 Director and cast
  - 📝 Description
  - ⭐ Rating
  - 🎯 Similar/recommended movies (RecyclerView)
- Play button to launch video player

<p align="center">
  <img src="androidDoc/movie_details_activity.png" alt="Movie Details Activity" width="300"/>
</p>

**Light Mode:**
<p align="center">
  <img src="androidDoc/movie_details_light.png" alt="Movie Details - Light Mode" width="300"/>
</p>

---

### 9. Video Player Activity
- Full-screen movie player
- Features:
  - Play/Pause controls
  - Progress bar with seek
  - Volume control
  - Fullscreen mode
  - Quality selection (if available)

<p align="center">
  <img src="androidDoc/video_player_activity.png" alt="Video Player" width="300"/>
</p>

---

## 👨‍💼 Admin Activities

### Home Activity (Admin)
- Same as user home activity
- Additional **"Admin"** button in action bar
- Quick access to admin features

<p align="center">
  <img src="androidDoc/home_activity_admin.png" alt="Admin Home Activity" width="300"/>
</p>

---

### Admin Menu Activity
- Central hub for all admin functions
- Two main sections:
  - 📁 **Categories Management**
  - 🎬 **Movies Management**

<p align="center">
  <img src="androidDoc/admin_categories.png" alt="Admin Category Activity" width="300"/>
</p>

---

### Add Category Activity
- Create new movie categories
- Fields:
  - 📝 Category name
  - 📝 Description

<p align="center">
  <img src="androidDoc/admin_add_category.png" alt="Add Category Activity" width="300"/>
</p>

**Example:**
<p align="center">
  <img src="androidDoc/admin_add_category_example.png" alt="Add Category Example" width="300"/>
</p>

---

### Admin Movies Activity
- List all movies with edit/delete options
- Features:
  - Search movies
  - Edit existing movies
  - Delete movies
  - Add new movies

<p align="center">
  <img src="androidDoc/movies_activity.png" alt="Admin Movies Activity" width="300"/>
</p>

---

### Add Movie Activity
- Create new movie entries
- Fields:
  - 🎬 Title
  - 📝 Description
  - ⏱️ Duration
  - 👨‍🎬 Director
  - 🎭 Cast (comma-separated)
  - 📅 Release year
  - 📸 Poster image
  - 🎥 Trailer (mp4)
  - 🎥 Full movie (mp4)

<p align="center">
  <img src="androidDoc/admin_add_movie.png" alt="Add Movie Activity" width="300"/>
</p>

**Example:**
<p align="center">
  <img src="androidDoc/admin_add_movie_example.png" alt="Add Movie Example" width="300"/>
</p>

---

### Edit Movie Activity
- Modify existing movie information
- Update any field
- Re-upload media files

<p align="center">
  <img src="androidDoc/admin_edit_movie.png" alt="Edit Movie Activity" width="300"/>
</p>

---

## 🔐 Instructions for Admin

### Step 1: Create User Account
1. Launch the app
2. Tap **"Sign Up"**
3. Fill in email, password, and profile picture
4. Create account

### Step 2: Change Role to Admin
1. Open **MongoDB Compass**
2. Connect to `localhost:27017`
3. Navigate to → **users** collection
4. Find your newly created user
5. Change `role` field from `"user"` to `"admin"`

<p align="center">
  <img src="webDoc/user.png" alt="User DB" width="300"/>
</p>

<p align="center">
  <img src="webDoc/admin.png" alt="Admin Role DB" width="340"/>
</p>

### Step 3: Activate Admin Features
1. **Logout** from the app
2. **Login** again with your updated account
3. **Admin button** now appears in home activity
4. Tap to access admin panel

### ⚠️ Important Notes
- Admin features only appear AFTER logging in post-role change
- Categories and cast entries: use comma (`,`) to separate
- File formats: Images (jpg/png), Videos (mp4 only)
- Keep servers running while using admin features

---

## 🚀 Getting Started

### Prerequisites
- Android Studio installed
- Android SDK 21+
- Emulator or physical device (Android 6.0+)
- Backend servers running

### Installation & Setup

1. **Open Android Studio**:
   - File → Open → Select `Netflix android` folder

2. **Configure Connection**:
   - Edit: `app/src/main/res/raw/config.properties`
   ```properties
   ip_address=10.0.2.2          # Emulator: 10.0.2.2, Device: your local IP
   port=3001                    # Backend API port
   jwt_secret=Vj4@7sF!9K#pLz^D2o7uN13X6A9Q5
   ```

3. **Configure Network Security**:
   - Edit: `app/src/main/res/xml/network_security_config.xml`
   - Add your IP address for cleartext traffic

4. **Run Application**:
   - Click **"Run"** button
   - Select emulator or device
   - App launches

### Debug

- Check logcat for connection errors
- Verify backend services are running (`docker ps`)
- Confirm firewall allows port 3001
- Validate IP address matches your network

---

## 🌍 Multi-Language Support

The app automatically switches language based on device settings:
- 🇬🇧 **English** - Default
- 🇪🇸 **Spanish** - If device language is Spanish

---

## 🔗 Related Documentation
- [Main README](../README.md)
- [Web Application](web.md)
- [Architecture](../ARCHITECTURE.md)

---

**Last Updated**: February 2026

## User Activities

### 1. Welcome Activity
- The landing page of the application.
- Displays a hero banner or introductory section.

<p align="center">
  <img src="androidDoc/landing_page.png" alt="Landing page" width="300"/>
</p>

- In light mode:

<p align="center">
  <img src="androidDoc/landing_page_light.png" alt="Landing page - light mode" width="300"/>
</p>


### 2. SignUp Activity
- Allows new users to **create an account**.
- Contains fields for **email, password, confirm password, and profile picture**.

<p align="center">
  <img src="androidDoc/sign_up_activity.png" alt="Sign Up Activity" width="300"/>
</p>

### 3. LogIn Activity
- Enables existing users to **log in**.
- Requires **email and password**.

<p align="center">
  <img src="androidDoc/log_in_activity.png" alt="Log In Activity" width="300"/>
</p>

### 4. Home Activity
- Displays **movies categorized by promoted categories**.
- Each category appears as a list of **small-sized movie component** using recaycler view.

<p align="center">
  <img src="androidDoc/home_activity_user.png" alt="Home Activity" width="300"/>
</p>

### 5. Search Activity
- Allows users to **search movies dynamically**.

<p align="center">
  <img src="androidDoc/search_activity.png" alt="Search Activity" width="300"/>
</p>

- Allows users to watch movies by category

<p align="center">
  <img src="androidDoc/category_activity.png" alt="Category Activity" width="300"/>
</p>

- In light mode:

<p align="center">
  <img src="androidDoc/search_movie_light.png" alt="Search Activity - Light Mode" width="300"/>
</p>

### 6. Profile activity
- Displays user **profile details**.
- Alows the user to sign out 

<p align="center">
  <img src="androidDoc/user_details.png" alt="User Profile" width="300"/>
</p>

- In light mode:

<p align="center">
  <img src="androidDoc/user_details_light.png" alt="User Profile - Light Mode" width="300"/>
</p>

### 7. Movie Details Activity

- The Movie Details Activity open after clicking on a movie and uniqe to every movie.
- This activity displayes all the info about the movie (name,duration,director...) and a play button. 
- Also displays similar movies using recaycler view and adapter.

<p align="center">
  <img src="androidDoc/movie_details_activity.png" alt="Movie Details Activity" width="300"/>
</p>

- In light mode:

<p align="center">
  <img src="androidDoc/movie_details_light.png" alt="Movie Details - Light Mode" width="300"/>
</p>

### 8. Video player
the page where you can watch the full movie

<p align="center">
  <img src="androidDoc/video_player_activity.png" alt="Video Player" width="300"/>
</p>

---

## Admin Activities

### Home Activity

- Same as the users Home Activity but with extra button for the admin area in the action bar. 

<p align="center">
  <img src="androidDoc/home_activity_admin.png" alt="Admin Home Activity" width="300"/>
</p>

### Admin Category activity 
- Clicking the admin button will open the admin activity
- The admin can choose between categories and movies 

<p align="center">
  <img src="androidDoc/admin_categories.png" alt="Admin Category Activity" width="300"/>
</p>

### Add Catgory Activity
- In the categories admin activity the admin can add a category:

<p align="center">
  <img src="androidDoc/admin_add_category.png" alt="Add Category Activity" width="300"/>
</p>

- For example:

<p align="center">
  <img src="androidDoc/admin_add_category_example.png" alt="Add Category Example" width="300"/>
</p>

### Admin Movies Activity
- The activity that alows the admin to manage all the movies 

<p align="center">
  <img src="androidDoc/movies_activity.png" alt="Admin Movies Activity" width="300"/>
</p>

### Add Movie Activity
- In the movies admin activity the admin can add a movie:

<p align="center">
  <img src="androidDoc/admin_add_movie.png" alt="Add Movie Activity" width="300"/>
</p>

- For example:

<p align="center">
  <img src="androidDoc/admin_add_movie_example.png" alt="Add Movie Example" width="300"/>
</p>

### Edit Movie Activity
- In the movies admin activity the admin can edit a movie:

<p align="center">
  <img src="androidDoc/admin_edit_movie.png" alt="Edit Movie Activity" width="300"/>
</p>

## instructions for admin 
after you create a user you shuld go to the data base **nongoDB compass** 
then you will see the new user under **users** 

<p align="center">
  <img src="webDoc/user.png" alt="User DB" width="300"/>
</p>

- change the role from **user** to **admin** 

<p align="center">
  <img src="webDoc/admin.png" alt="Admin Role DB" width="340"/>
</p>

**important**: to use the admin fetures you should log out and log in again.
