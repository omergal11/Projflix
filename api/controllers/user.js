const userService = require('../services/user');
const fs = require('fs'); // Add this import
const path = require('path'); // Also needed for path operations
const DEFAULT_PROFILE_PICTURE = 'uploads/usersImages/default.jpg';

const createUser = async (req, res) => {
    try {
        const { email, password, username } = req.body;
        const tempFilePath = req.files?.profilePicture?.[0]?.path;

        // Create user with default or empty path
        const user = await userService.createnewUser(
            email,
            password, 
            tempFilePath ? '' : DEFAULT_PROFILE_PICTURE, // Use default if no upload
            username
        );

        if (user.error) {
            if (tempFilePath && fs.existsSync(tempFilePath)) {
                fs.unlinkSync(tempFilePath);
            }
            return res.status(user.status).json({ error: user.error });
        }

        // Handle uploaded image
        if (tempFilePath) {
            const finalFileName = `${user._id}${path.extname(tempFilePath)}`;
            const finalFilePath = path.join('uploads/usersImages', finalFileName);
            fs.renameSync(tempFilePath, finalFilePath);
            user.profilePicture = finalFilePath;
            await user.save();
        }
        
        res.status(201).location(`/api/users/${user._id}`).send();
    } catch (error) {
        // Clean up temp file if exists
        if (req.files?.profilePicture?.[0]?.path) {
            const tempFile = req.files.profilePicture[0].path;
            fs.existsSync(tempFile) && fs.unlinkSync(tempFile);
        }
        
        res.status(500).json({ message: 'Internal Server Error', error: error.message });
    }
};
const getUser = async (req, res) => {
    // Get the user by id
    try {
    const user = await userService.getUserById(req.params.id);
    // Check if there is an error
    if (user.error) {
        return res.status(user.status).json({ error: user.error });
    }
    // Remove sensitive data 
    const cleanUser = await userService.getCleanUserData(user)
    res.json(cleanUser);
    } catch (error) {
    res.status(500).json({ message: 'Internal Server Error', error: error.message });
    }
};

const signInUser = async (req, res) => {
    try {
        // Sign in the user
    const { email, password } = req.body;
    const result = await userService.signInUser(email, password);
    // Check if there is an error
    if (result.error) {
        return res.status(result.status).json({ error: result.error });
    }
    res.json(result);
    } catch (error) {
    res.status(500).json({ message: 'Internal Server Error', error: error.message });
    }
};
module.exports = {createUser, getUser, signInUser};