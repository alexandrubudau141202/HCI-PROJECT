const express = require('express');
const router  = express.Router();

router.get('/',                  (req, res) => res.render('index'));
router.get('/anime',             (req, res) => res.render('anime'));
router.get('/anime/:id',         (req, res) => res.render('anime-detail'));
router.get('/characters',        (req, res) => res.render('characters'));
router.get('/characters/:id',    (req, res) => res.render('character-detail'));
router.get('/people',            (req, res) => res.render('people'));
router.get('/people/:id',        (req, res) => res.render('person-detail'));
router.get('/users',             (req, res) => res.render('users'));
router.get('/users/:username',   (req, res) => res.render('user-detail'));

module.exports = router;