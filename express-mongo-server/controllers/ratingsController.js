const Rating = require('../models/Rating');

// GET /api/ratings/anime/:animeId
exports.getByAnime = async (req, res) => {
    try {
        const { animeId } = req.params;
        const page  = parseInt(req.query.page)  || 0;
        const size  = parseInt(req.query.size)  || 20;

        const [data, total] = await Promise.all([
            Rating.find({ anime_id: parseInt(animeId) })
                  .skip(page * size)
                  .limit(size)
                  .lean(),
            Rating.countDocuments({ anime_id: parseInt(animeId) })
        ]);

        res.json({ data, total, page, size, totalPages: Math.ceil(total / size) });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

// GET /api/ratings/user/:username
exports.getByUser = async (req, res) => {
    try {
        const { username } = req.params;
        const page = parseInt(req.query.page) || 0;
        const size = parseInt(req.query.size) || 20;

        const [data, total] = await Promise.all([
            Rating.find({ username })
                  .skip(page * size)
                  .limit(size)
                  .lean(),
            Rating.countDocuments({ username })
        ]);

        res.json({ data, total, page, size, totalPages: Math.ceil(total / size) });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

// GET /api/ratings/anime/:animeId/stats
exports.getAnimeStats = async (req, res) => {
    try {
        const animeId = parseInt(req.params.animeId);

        const result = await Rating.aggregate([
            { $match: { anime_id: animeId, score: { $gt: 0 } } },
            { $group: {
                _id: '$anime_id',
                avgScore:     { $avg: '$score' },
                totalRatings: { $sum: 1 }
            }}
        ]).hint({ anime_id: 1, score: 1 });

        res.json(result[0] || { animeId, avgScore: 0, totalRatings: 0 });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};