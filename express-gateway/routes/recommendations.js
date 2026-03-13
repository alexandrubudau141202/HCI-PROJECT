const express = require('express');
const axios   = require('axios');
const router  = express.Router();
const BASE    = process.env.SPRING_BOOT_URL;

router.get('/top',    (req, res) => forward(res, axios.get(`${BASE}/api/recommendations/top`, { params: req.query }), 'Spring Boot'));
router.get('/:malId', (req, res) => forward(res, axios.get(`${BASE}/api/recommendations/${req.params.malId}`), 'Spring Boot'));

module.exports = router;