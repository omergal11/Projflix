const categoryService = require('../services/category');
const createCategory = async (req, res) => {
    try {
        const category = await categoryService.createCategory(req.body.name, req.body.promoted);
        if (category.error) {
            return res.status(400).json({ error: category.error });
        }
         res.status(201).location(`/api/categories/${category._id}`).send();
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const getAllCategories = async (req, res) => {
    // Get all categories
    try {
        const categories = await categoryService.getAllCategories();
        res.status(200).json(categories);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};
// 
const updateCategory = async (req, res) => {
    try {
        // Validate the request body
        const { name, promoted } = req.body;
        // Update the category
        const category = await categoryService.updateCategory(req.params.id, name, promoted);
        if (category.error)
            return res.status(category.status).json({ error: category.error });
        res.status(204).send();
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const getCategory = async (req, res) => {
    // Get the category by id
    try {
        const category = await categoryService.getCategoryById(req.params.id);
        if (category.error) {
            return res.status(404).json({ error: category.error });
        }
        res.status(200).json(category);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};
const deleteCategory = async (req, res) => {
    try {
        const category = await categoryService.deleteCategory(req.params.id);
        if (category.error) 
            return res.status(404).json({ error: category.error });
        res.status(204).send();
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

module.exports = {
    createCategory,
    getAllCategories,
    updateCategory,
    getCategory,
    deleteCategory
};