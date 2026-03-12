const express = require('express');
const router  = express.Router();
const ctrl    = require('../controllers/favsController');

router.get('/user/:username', ctrl.getByUser);
router.get('/anime/:animeId', ctrl.getByAnime);
router.get('/top',            ctrl.getTop);

module.exports = router;