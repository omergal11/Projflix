const mongoose = require('mongoose');

const schema = mongoose.Schema;
const Category = new schema ({
    name: { type: String, required: true },
    promoted: { type: Boolean, default: false },
    movies:{ type: [mongoose.Schema.Types.ObjectId], ref: 'Movie' }
},{versionKey: false})

module.exports = mongoose.model('Category', Category);   