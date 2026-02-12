# Android Application Documentation
## Table of Contents
- [Overview](#Overview)
- [User Activities](#user-activities)
- [Admin Activities](#admin-activities)
- [admin-instructions](#instructions-for-admin)

## Overview
This is a **Android** frontend for a **Movie Management System** that integrates with a **Node.js backend**. The system allows users to **browse, search, add, edit, and delete movies and categories** while maintaining user authentication and admin roles.

This android app availble in two languges, English and Spanish, depends on your phone settings.

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
