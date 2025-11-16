const jwt = require('jsonwebtoken');

const authenticateToken = (req, res, next) => {
    // Skip authorization for user creation and token generation
    if (req.originalUrl === '/api/users' || req.originalUrl === '/api/tokens') {
        return next();
    }
    // Check if the user-id header is present
    const token = req.header('Authorization')?.replace('Bearer ', '');

    if (!token) {
        return res.status(403).json({ error: 'Access denied' });
    }

    jwt.verify(token, process.env.JWT_SECRET, (err, user) => {
        if (err) {
            return res.status(403).json({ error: 'Invalid token' });
        }
        req.user = user; // store user information in the request object
        if(req.user.role == 'admin') {
            return next();
        }
        if ((req.originalUrl.startsWith('/api/categories')&& req.method === 'POST' || req.method === 'PATCH' || req.method === 'DELETE' )
        || (req.originalUrl.startsWith('/api/users')&& req.method === 'POST')
            ||   (req.originalUrl.startsWith('/api/movies') &&( req.method === 'PUT' || req.method === 'DELETE'))
         || ( req.originalUrl === '/api/movies' && req.method === 'POST'))
          {
                return res.status(403).json({ error: 'Access denied' });
        }
        if (req.originalUrl.startsWith('/api/users/') && req.method === 'GET') {
            const userIdFromUrl = req.originalUrl.split('/')[3];  
            const userIdFromSession = req.user.userId; 
        
            if (userIdFromUrl === userIdFromSession) {
                return next();  
            } else {
                return res.status(403).json({ error: 'Access denied' });
            }
        }
        next();
    });
};

module.exports = authenticateToken;