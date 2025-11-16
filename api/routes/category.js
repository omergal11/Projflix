const express = require('express');
var router = express.Router();
const categoryController = require('../controllers/category');
// Routes for category
router.post('/', categoryController.createCategory); 
router.get('/', categoryController.getAllCategories);
router.patch('/:id', categoryController.updateCategory); 
router.get('/:id', categoryController.getCategory); 
router.delete('/:id', categoryController.deleteCategory);

module.exports = router;
