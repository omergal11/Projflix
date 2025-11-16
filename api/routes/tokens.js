const express = require('express');
var router = express.Router();
const userController = require('../controllers/user');  
// Routes for user
router.route('/').post(userController.signInUser);

module.exports = router;