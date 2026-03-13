const express = require('express');
const axios   = require('axios');
const router  = express.Router();
const BASE    = process.env.SPRING_BOOT_URL;

router.get('/',               (req, res) => forward(res, axios.get(`${BASE}/api/anime`, { params: req.query }), 'Spring Boot'));
router.get('/search',         (req, res) => forward(res, axios.get(`${BASE}/api/anime/search`, { params: req.query }), 'Spring Boot'));
router.get('/top/score',      (req, res) => forward(res, axios.get(`${BASE}/api/anime/top/score`, { params: req.query }), 'Spring Boot'));
router.get('/top/popularity', (req, res) => forward(res, axios.get(`${BASE}/api/anime/top/popularity`, { params: req.query }), 'Spring Boot'));
router.get('/type/:type',     (req, res) => forward(res, axios.get(`${BASE}/api/anime/type/${req.params.type}`, { params: req.query }), 'Spring Boot'));
router.get('/year/:year',     (req, res) => forward(res, axios.get(`${BASE}/api/anime/year/${req.params.year}`, { params: req.query }), 'Spring Boot'));
router.get('/season',         (req, res) => forward(res, axios.get(`${BASE}/api/anime/season`, { params: req.query }), 'Spring Boot'));
router.get('/genre/:genre',   (req, res) => forward(res, axios.get(`${BASE}/api/anime/genre/${req.params.genre}`, { params: req.query }), 'Spring Boot'));
router.get('/studio/:studio', (req, res) => forward(res, axios.get(`${BASE}/api/anime/studio/${req.params.studio}`, { params: req.query }), 'Spring Boot'));
router.get('/:malId',         (req, res) => forward(res, axios.get(`${BASE}/api/anime/${req.params.malId}`), 'Spring Boot'));

module.exports = router;