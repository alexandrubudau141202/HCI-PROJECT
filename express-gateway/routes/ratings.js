const express = require('express');
const axios   = require('axios');
const router  = express.Router();
const BASE    = process.env.MONGO_EXPRESS_URL;

router.get('/anime/:animeId/stats', (req, res) => forward(res, axios.get(`${BASE}/api/ratings/anime/${req.params.animeId}/stats`), 'Express MongoDB'));
router.get('/anime/:animeId',       (req, res) => forward(res, axios.get(`${BASE}/api/ratings/anime/${req.params.animeId}`, { params: req.query }), 'Express MongoDB'));
router.get('/user/:username',       (req, res) => forward(res, axios.get(`${BASE}/api/ratings/user/${req.params.username}`, { params: req.query }), 'Express MongoDB'));

module.exports = router;