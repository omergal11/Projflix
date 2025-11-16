const users = require('./user');
const categories = require('./category');
const tokens = require('./tokens');
const movies = require('./movie');
const express = require('express');
const authenticateToken = require('../middlewares/auth');  
var router = express.Router();

// Routes for api

// Apply authentication middleware globally (for protected routes)
router.use(authenticateToken);
router.use('/tokens', tokens);
router.use('/users', users);
router.use('/categories', categories);
router.use('/movies', movies);

module.exports = router;