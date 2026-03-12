require('dotenv').config();
const express   = require('express');
const cors      = require('cors');
const connectDB = require('./config/db');

const app = express();
app.use(cors());
app.use(express.json());

connectDB();

app.use('/api/ratings', require('./routes/ratings'));
app.use('/api/favs',    require('./routes/favs'));

app.get('/health', (req, res) => res.json({ status: 'ok', port: process.env.PORT }));

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => console.log(`Express MongoDB server running on port ${PORT}`));