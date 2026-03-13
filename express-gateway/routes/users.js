const express = require('express');
const axios   = require('axios');
const router  = express.Router();
const BASE    = process.env.SPRING_BOOT_URL;

router.get('/',                   (req, res) => forward(res, axios.get(`${BASE}/api/users`, { params: req.query }), 'Spring Boot'));
router.get('/location/:location', (req, res) => forward(res, axios.get(`${BASE}/api/users/location/${req.params.location}`, { params: req.query }), 'Spring Boot'));
router.get('/joined/:year',       (req, res) => forward(res, axios.get(`${BASE}/api/users/joined/${req.params.year}`, { params: req.query }), 'Spring Boot'));
router.get('/:username',          (req, res) => forward(res, axios.get(`${BASE}/api/users/${req.params.username}`), 'Spring Boot'));

module.exports = router;