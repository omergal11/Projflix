const express = require('express');
var router = express.Router();
const upload = require('../middlewares/upload');
const userController = require('../controllers/user');

// Routes for user
router.route('/')
    .post(upload.fields([
        { name: 'profilePicture', maxCount: 1 }, // תמונה ראשית (תמונה אחת)
    ]),userController.createUser);
router.route('/:id').get(userController.getUser);
    
module.exports = router;