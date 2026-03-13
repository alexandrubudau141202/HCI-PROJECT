const express = require('express');
const axios   = require('axios');
const router  = express.Router();
const BASE    = process.env.SPRING_BOOT_URL;

router.get('/search', (req, res) => forward(res, axios.get(`${BASE}/api/people/search`, { params: req.query }), 'Spring Boot'));
router.get('/top',    (req, res) => forward(res, axios.get(`${BASE}/api/people/top`, { params: req.query }), 'Spring Boot'));
router.get('/:id',    (req, res) => forward(res, axios.get(`${BASE}/api/people/${req.params.id}`), 'Spring Boot'));

module.exports = router;