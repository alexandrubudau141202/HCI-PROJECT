const mongoose = require('mongoose');

const favSchema = new mongoose.Schema({
    username: { type: String, required: true },
    fav_type: { type: String, required: true },
    id:       { type: Number, required: true }
}, { collection: 'favs' });

favSchema.index({ username: 1 });
favSchema.index({ username: 1, fav_type: 1 });
favSchema.index({ username: 1, fav_type: 1, id: 1 }, { unique: true });

module.exports = mongoose.model('Fav', favSchema);