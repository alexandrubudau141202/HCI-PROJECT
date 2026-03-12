const mongoose = require('mongoose');

const ratingSchema = new mongoose.Schema({
    username:             { type: String, required: true },
    anime_id:             { type: Number, required: true },
    status:               { type: String },
    score:                { type: Number },
    is_rewatching:        { type: Boolean },
    num_watched_episodes: { type: Number }
}, { collection: 'ratings' });

ratingSchema.index({ username: 1 });
ratingSchema.index({ anime_id: 1 });
ratingSchema.index({ username: 1, anime_id: 1 }, { unique: true });

module.exports = mongoose.model('Rating', ratingSchema);