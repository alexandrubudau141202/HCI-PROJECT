const Fav = require('../models/Fav');

// GET /api/favs/user/:username
exports.getByUser = async (req, res) => {
    try {
        const { username } = req.params;
        const { type } = req.query;

        const filter = { username };
        if (type) filter.fav_type = type;

        const data = await Fav.find(filter).lean();
        res.json(data);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

// GET /api/favs/anime/:animeId
exports.getByAnime = async (req, res) => {
    try {
        const { animeId } = req.params;
        const page = parseInt(req.query.page) || 0;
        const size = parseInt(req.query.size) || 20;

        const [data, total] = await Promise.all([
            Fav.find({ fav_type: 'anime', id: parseInt(animeId) })
               .skip(page * size)
               .limit(size)
               .lean(),
            Fav.countDocuments({ fav_type: 'anime', id: parseInt(animeId) })
        ]);

        res.json({ data, total, page, size, totalPages: Math.ceil(total / size) });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

// GET /api/favs/top?type=anime&limit=20
exports.getTop = async (req, res) => {
    try {
        const type  = req.query.type  || 'anime';
        const limit = parseInt(req.query.limit) || 20;

        const data = await Fav.aggregate([
            { $match: { fav_type: type } },
            { $group: { _id: '$id', count: { $sum: 1 } } },
            { $sort: { count: -1 } },
            { $limit: limit },
            { $project: { _id: 0, id: '$_id', count: 1 } }
        ]);

        res.json(data);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};