const Category = require('../models/category');
// Validate category data
const validateCategoryData = async (name, promoted) => {
    if (!name) 
        return { error: 'name is requiered', status: 400 };
    // Check if promoted is a boolean
    if (promoted !== undefined && typeof promoted !== 'boolean') 
        return { error: 'promoted must be boolean', status:400 };

    // Check if the category name already exists
    const existingCategory = await Category.findOne({ 
        name: { $regex: new RegExp('^' + name + '$', 'i') }  // i for case-insensitive search
    });
    console.log(existingCategory);
    if (existingCategory) {
        return { error: 'name already exists',status:400 };
    }

    return null;  // Return null if all validations passed
};
const validateForUpdate = async (name, promoted) => {

    if (!name && promoted === undefined) {
        return { error: 'at least one change is required', status: 400 };
    }
    if (promoted !== undefined && typeof promoted !== 'boolean') {
        return { error: 'promoted must be boolean', status: 400 };
    }
        // Check if the category name already exists
    if (name) {
        const existingCategory = await Category.findOne({ 
            name: { $regex: new RegExp('^' + name + '$', 'i') }  // i for case-insensitive search
        });
        console.log(existingCategory);
        if (existingCategory) {
            return { error: 'name already exists',status:400 };
        }
    }
    return null;
};
// Create a new category
const createCategory = async (name, promoted) => {
    // Validate user data
    const validationError = await validateCategoryData(name, promoted);
    if (validationError) return validationError;
    const category = new Category({ name : name, promoted: promoted });
    return await category.save();
};
// Get all categories
const getAllCategories = async () => {
    try {
        const categories = await Category.find();

        const cleanedCategories = await Promise.all (categories.map(category => getFullCategoryData(category)));
        return cleanedCategories;
    } catch (error) {
        return { error: "An error occurred while fetching categories", status: 500 };
    }
};
const getCategoryById = async (id) => {
    try {
        // get the category by id
        const category = await Category.findById(id);
        if (!category) {
            return { error: "Category not found", status: 404 };
        }
        return category;
    } catch (error ) {
        return {error: "Category id is not in the right format" , status: 400};
    }
   
};
const getFullCategoryDataById = async (id) => {
    try {
        // get the category by id
        const category = await getCategoryById(id);
        if (category.error) {
            return category;
        }
        const cleanCategory = await getFullCategoryData(category);
        return cleanCategory;
        // Catch the error if the id is not in the right format
    } catch (error ) {
        return {error: "Category id is not in the right format" , status: 400};
    }
   
};
const getFullCategoryData = async (category) => {
    const { getMoviesDetails } = require('./movie'); 
    if (category) {
        const categoryData = category.toObject ? category.toObject() : category;  // Convert to object if not already
        const movies = await getMoviesDetails(categoryData.movies);
        categoryData.movies = movies;
        return categoryData;  // Return cleaned movie data
    }
    return {};  // Return empty object if no movie is found
};

const getCategoriesNames = async (categoryIds) => {
    try {
        // חיפוש ב-db לפי המזהים
        const categories = await Category.find({
            '_id': { $in: categoryIds }
        }).select('name _id');  // בחר רק את השדות name ו-_id

        // אם לא נמצאו קטגוריות
        if (!categories.length) {
            return { error: "Categories not found", status: 404 };
        }

        // מחזיר מערך של אובייקטים עם id ו-name
        return categories.map(category => ({
            _id: category._id,  // ה-_id יהיה השדה id
            name: category.name
        }));
    } catch (error) {
        return { error: "Invalid category ids format", status: 400 };
    }
};

const updateCategory = async (id, name, promoted) => {
    // Validate the id
    if (!id) 
            return {  error: "id is required",  status: 400 };
        // Get the category by id
        const lastCategory= await getCategoryById(id);
        if(lastCategory.error) return lastCategory;
        // Validate the request body
        const validationError = await validateForUpdate(name, promoted);
        console.log(validationError);
        if (validationError) {
            if (validationError.error !== 'name already exists' || 
                (validationError.error === 'name already exists' && lastCategory.name.toLowerCase() !== name.toLowerCase())) {
                return validationError;  // Return validation error if needed
            }
        }
        const oldName = lastCategory.name;
        const oldPromoted = lastCategory.promoted;
        lastCategory.name = name;
        lastCategory.promoted = promoted;

        if (!name) {
            lastCategory.name = oldName;
        }
        if (promoted=== undefined) {
            lastCategory.promoted = oldPromoted;
        }
        await lastCategory.save();
        return lastCategory;
    };


const replaceNameToId = async (categoryNames) => {
    const categoryIds = [];
    // Get the category id by name
    for (let categoryName of categoryNames) {
        const category = await Category.findOne({ 
            name: { $regex: new RegExp(`^${categoryName}$`, 'i') } 
        });;
            categoryIds.push(category._id);
    }
    return categoryIds; 
};
const replaceIDtoName = async (categoryIds) => {
    const categoryNames = [];
    // Get the category name by id
    for (let categoryId of categoryIds) {
        try {
            const category = await getCategoryById(categoryId); 
            if (category) {
                categoryNames.push(category.name);
            }
        } catch (error) {
            categoryNames.push("Error");
        }
    }
    return categoryNames;
};
const addMovieToCategory = async (id, movieId) => {
    // Get the category name by id
    const category = await getCategoryById(id);
    if (!category) {
        return {error : 'Category not found' , status : 404};
    }
    //Add movie to the category
    category.movies.push(movieId);
    console.log(category);
    await category.save();
    return category;
};
const deleteMovieFromCategory = async (id, movieId) => {
    // Fetch category by its ID
    const category = await getCategoryById(id);
    if (!category) {
        return { error: 'Category not found', status: 404 };  // If category doesn't exist
    }

    // Find movie in category's movies array
    const movieIndex = category.movies.indexOf(movieId);
    if (movieIndex === -1) {
        return { error: 'Movie not found in category', status: 404 };  // If movie is not found
    }

    // Remove movie from the category
    category.movies.splice(movieIndex, 1);  
    await category.save();  // Save the updated category

    return category;  // Return updated category
};
const findCategoryByName = async (categoryName) => {
    // Find and return the category by name
    const category = await Category.findOne({ 
        name: { $regex: new RegExp(`^${categoryName}$`, 'i') } 
    });
    return category;  // Return the found category or null if not found
};    
const findPromotedcategories = async () => {
    // Retrieve categories where the 'promoted' field is true
    const categories = await Category.find({ promoted: true });
    return categories;  // Return the list of promoted categories
};

// Fetches 20 random movies from a specific category that a user hasn't watched
const get20RandomMovies = async (categoryId, userId) => {
    const { getUserById } = require('./user');
    const { getMoviesByCategoryAndNotWatched } = require('./movie');
    const { getCleanMoviesData } = require('./movie');
    try {
        // Fetch category by ID
        const category = await Category.findById(categoryId);
        if (!category) {
            return { error: "Category not found", status: 404 };  // Return error if category not found
        }

        // Fetch user data by ID
        const user = await getUserById(userId);
        if (user.error) {
            return user;  // Return error if user is not found
        }

        // Get the list of watched movies for the user
        const watchedMovies = user.watchedMovies; 

        // Fetch movies from the specified category excluding watched ones
        const movies = await getMoviesByCategoryAndNotWatched(categoryId, watchedMovies);
         // Clean the final list of movies
        const cleanedMovies = await getCleanMoviesData(movies)
        return{ category : category.name ,movies : cleanedMovies };  // Return the category name and list of random movies

    } catch (error) {
        // Return error message in case of failure
        return { error: error.message, status: 500 };
    }
};
const deleteCategory = async (id) => {
    // Fetch category by its ID
    const { deleteCategoryFromMovie } = require('./movie');
    const category = await getCategoryById(id);
    if (category.error == 'Category not found') {
        return category;
    }
    // Delete category from all movies in the category
    const moviesInCategory = category.movies
    for (let movie of moviesInCategory) {
        const result = await deleteCategoryFromMovie(movie, id);
        if (result.error) {
            return result;
        }
    }
    try {
        // Delete the category
        await Category.deleteOne({ _id: id });
        return { message: "Category deleted successfully", status: 204 };
    } catch (error) {
        return { error: error.message, status: 500 }; 
    }
};

    
// Export the functions
module.exports = {
    createCategory,
    getAllCategories,
    updateCategory,
    getCategoryById,
    replaceNameToId,
    replaceIDtoName,
    deleteMovieFromCategory,
    addMovieToCategory,
    findCategoryByName,
    findPromotedcategories,
    get20RandomMovies,
    deleteMovieFromCategory,
    deleteCategory,
    getCategoriesNames,
    getFullCategoryDataById
};