var mongoose = require('mongoose');
var locationSchema = mongoose.Schema({
	date: {
		type: Date,
		default: Date.now
    },
    child: {
        type: String,
        required: true
    },
	longitude: {
		type: String,
		required: true
    },
	latitude: {
		type: String,
		required: true
    },
	status: {
		type: String,
		required: true
    },
	alarm: {
		type: String,
		required: true
    }
});
var Location = module.exports = mongoose.model('locations', locationSchema);
module.exports.get = function (callback, limit) {
    Location.find(callback).limit(limit);
}