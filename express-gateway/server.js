require('dotenv').config();
const express = require('express');
const cors    = require('cors');
const path    = require('path');
const { engine } = require('express-handlebars');

const app = express();
app.use(cors());
app.use(express.json());

// Handlebars
app.engine('hbs', engine({ extname: '.hbs', defaultLayout: 'main' }));
app.set('view engine', 'hbs');
app.set('views', path.join(__dirname, 'views'));

// Static files
app.use(express.static(path.join(__dirname, 'public')));

// forward helper
global.forward = (res, promise, backend) => {
    promise
        .then(r => {
            res.set('X-Served-By', backend);
            res.json(r.data);
        })
        .catch(e => res.status(e.response?.status || 500).json({ error: e.message }));
};

// Page routes
app.use('/', require('./routes/pages'));

// API routes
app.use('/api/anime',           require('./routes/anime'));
app.use('/api/characters',      require('./routes/characters'));
app.use('/api/people',          require('./routes/people'));
app.use('/api/users',           require('./routes/users'));
app.use('/api/recommendations', require('./routes/recommendations'));
app.use('/api/ratings',         require('./routes/ratings'));
app.use('/api/favs',            require('./routes/favs'));

app.get('/health', (req, res) => res.json({ status: 'ok' }));

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Gateway running on port ${PORT}`));