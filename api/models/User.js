const mongoose = require('mongoose');

const Schema = mongoose.Schema;
const User = new Schema({
    username : {
        type: String,
        required: false,
    },
    email : {
        type: String,
        required: true,
        unique: true,
    },
    userId: {type:Number,
         unique: true},
    password: {
        type: String,
        required: true
    },
    profilePicture: {
        type: String, 
        required: false 
    },
    watchedMovies: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Movie' 
    }],
    role: {
        type: String,
        enum: ['user', 'admin'], // אפשרויות: 'user' או 'admin'
        default: 'user' // ברירת מחדל היא 'user'
    }
},{versionKey: false});

User.pre('save', async function (next) {
    if (this.isNew) {
        try {
            const lastUser = await this.constructor.findOne().sort({ userId: -1 });
            if (lastUser) {
                this.userId = lastUser.userId + 1;
            } else {
                this.userId = 1;
            }
        } catch (error) {
            return next(error);
        }
    }
    next();
});
module.exports = mongoose.model('User', User);