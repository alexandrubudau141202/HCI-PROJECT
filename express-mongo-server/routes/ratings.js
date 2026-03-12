const express = require('express');
const router  = express.Router();
const ctrl    = require('../controllers/ratingsController');

router.get('/anime/:animeId',       ctrl.getByAnime);
router.get('/anime/:animeId/stats', ctrl.getAnimeStats);
router.get('/user/:username',       ctrl.getByUser);

module.exports = router;