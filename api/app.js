// Import required modules
const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const mongoose = require('mongoose');
const api = require('./routes/api');
if (!process.env.CONNECTION_STRING) {
  require('custom-env').env(process.env.NODE_ENV, '../config');
}
const path = require('path');

// Connect to MongoDB using connection string from environment variables
mongoose.connect(process.env.CONNECTION_STRING);

// Create an instance of the Express app
var app = express();

// Use middleware for handling CORS requests
app.use(cors());

// Configure body parsers with increased limits
app.use(express.json({ limit: '200mb' }));
app.use(express.urlencoded({ limit: '200mb', extended: true }));
app.use(bodyParser.json({ limit: '200mb' }));
app.use(bodyParser.urlencoded({ limit: '200mb', extended: true }));

// Define the '/api' endpoint
app.use('/api', api);

// Serve static files
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

// Error handling for large payloads
app.use((err, req, res, next) => {
  if (err.type === 'entity.too.large') {
    res.status(413).json({ error: 'File too large' });
    return;
  }
  next(err);
});

// Start the server
app.listen(process.env.PORT);