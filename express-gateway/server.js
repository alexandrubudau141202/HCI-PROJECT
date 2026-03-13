require('dotenv').config();
const express = require('express');
const cors    = require('cors');
const path = require('path');

const app = express();
app.use(cors());
app.use(express.json());
app.use(express.static('public'));

app.use(express.static(path.join(__dirname, '../frontend')));

app.use('/api/anime',           require('./routes/anime'));
app.use('/api/characters',      require('./routes/characters'));
app.use('/api/people',          require('./routes/people'));
app.use('/api/users',           require('./routes/users'));
app.use('/api/recommendations', require('./routes/recommendations'));
app.use('/api/ratings',         require('./routes/ratings'));
app.use('/api/favs',            require('./routes/favs'));

app.get('/health', (req, res) => res.json({ status: 'ok', port: process.env.PORT }));

global.forward = (res, promise, backend) => {
    promise
        .then(r => {
            res.set('X-Served-By', backend);
            res.json(r.data);
        })
        .catch(e => res.status(e.response?.status || 500).json({ error: e.message }));
};

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Gateway running on port ${PORT}`));