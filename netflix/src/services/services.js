import { jwtDecode } from 'jwt-decode';
const API_URL= process.env.REACT_APP_API_URL;


// services.jss
const BASE_URL = `${API_URL}/api`;
// Auth Helper
const getAuthHeaders = () => {
  const token = localStorage.getItem('jwtToken');
  return {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  };
};

// Movies Services
export const movieServices = {
  getAllMovies: async () => {
    try {
      const response = await fetch(`${BASE_URL}/movies`, {
        headers: getAuthHeaders()
      });
      return await response.json();
    } catch (error) {
      console.error('Error fetching movies:', error);
      throw error;
    }
  },

  getMovieById: async (id) => {
    try {
      const response = await fetch(`${BASE_URL}/movies/${id}`, {
        headers: getAuthHeaders()
      });
      return await response.json();
    } catch (error) {
      console.error('Error fetching movie:', error);
      throw error;
    }
  },

  addMovie: async (movieData) => {
    try {
        const token = localStorage.getItem("jwtToken");
      const response = await fetch(`${BASE_URL}/movies`, {
        method: 'POST',
        headers: {'Authorization': `Bearer ${token}`
    },
        body: movieData
      });
      return response;
    } catch (error) {
      console.error('Error adding movie:', error);
      throw error;
    }
  },

  updateMovie: async (id, movieData) => {
    try {
        const token = localStorage.getItem("jwtToken");
      const response = await fetch(`${BASE_URL}/movies/${id}`, {
        method: 'PUT',
        headers: {'Authorization': `Bearer ${token}`
    },
        body: movieData
      });
      return  response;
    } catch (error) {
      console.error('Error updating movie:', error);
      throw error;
    }
  },
  searchMovies: async (query) => {
    try {
      const response = await fetch(`${BASE_URL}/movies/search/${query}`, {
        headers: getAuthHeaders()
      });
      return response;
    } catch (error) {
      console.error('Error searching movies:', error);
      throw error;
    }
  },

  deleteMovie: async (id) => {
    try {
      const response = await fetch(`${BASE_URL}/movies/${id}`, {
        method: 'DELETE',
        headers: getAuthHeaders()
      });
      return response;
    } catch (error) {
      console.error('Error deleting movie:', error);
      throw error;
    }
  }
};

// Categories Services
export const categoryServices = {
  getAllCategories: async () => {
    try {
      const response = await fetch(`${BASE_URL}/categories`, {
        headers: getAuthHeaders()
      });
      return await response.json();
    } catch (error) {
      console.error('Error fetching categories:', error);
      throw error;
    }
  },

  addCategory: async (categoryData) => {
    try {
      const response = await fetch(`${BASE_URL}/categories`, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify(categoryData)
      });
      return response;
    } catch (error) {
      console.error('Error adding category:', error);
      throw error;
    }
  },

  updateCategory: async (id, categoryData) => {
    try {
      const response = await fetch(`${BASE_URL}/categories/${id}`, {
        method: 'PATCH',
        headers: getAuthHeaders(),
        body: JSON.stringify(categoryData)
      });
      return response;
    } catch (error) {
      console.error('Error updating category:', error);
      throw error;
    }
  },

  deleteCategory: async (id) => {
    try {
      const response = await fetch(`${BASE_URL}/categories/${id}`, {
        method: 'DELETE',
        headers: getAuthHeaders()
      });
      return response;
    } catch (error) {
      console.error('Error deleting category:', error);
      throw error;
    }
  }
};

// Auth Services
export const authServices = {
  login: async (credentials) => {
    console.log(API_URL);
    try {
      const response = await fetch(`${BASE_URL}/tokens`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
      });
      return response;
    } catch (error) {
      console.error('Error logging in:', error);
      throw error;
    }
  },

  register: async (formData) => {
  try {
    // Add debug log
    console.log('FormData contents:', Object.fromEntries(formData.entries()));
    
    const response = await fetch(`${BASE_URL}/users`, {
      method: 'POST',
      // Remove any manual content-type setting - let browser handle it
      body: formData
    });
    return response;
  } catch (error) {
    console.error('Error registering:', error);
    throw error;
  }
},
getUser: async () => {
    try {
        const token = localStorage.getItem("jwtToken");
        const decoded = jwtDecode(token);
        const userId = decoded.userId;
        
        const response = await fetch(`${BASE_URL}/users/${userId}`, {
            method: 'GET',
            headers: getAuthHeaders()
        });

        if (!response.ok) {
            throw new Error('Failed to fetch user');
        }

        return await response.json();
    } catch (error) {
        throw error;
    }
}
};