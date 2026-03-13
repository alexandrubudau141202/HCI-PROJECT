const express = require('express');
const axios   = require('axios');
const router  = express.Router();
const BASE    = process.env.SPRING_BOOT_URL;

router.get('/search',         (req, res) => forward(res, axios.get(`${BASE}/api/characters/search`, { params: req.query }), 'Spring Boot'));
router.get('/top',            (req, res) => forward(res, axios.get(`${BASE}/api/characters/top`, { params: req.query }), 'Spring Boot'));
router.get('/anime/:animeId', (req, res) => forward(res, axios.get(`${BASE}/api/characters/anime/${req.params.animeId}`, { params: req.query }), 'Spring Boot'));
router.get('/:id',            (req, res) => forward(res, axios.get(`${BASE}/api/characters/${req.params.id}`), 'Spring Boot'));

module.exports = router;