var mongoose = require('mongoose');
var scheduleSchema = mongoose.Schema({
    child: {
        type: String,
        required: true
    },
    parent: {
        type: String,
        required: true
    },
	start_date: {
        type: String,
        required: true
    },
	start_time: {
		type: String,
		required: true
    },
    stop_date: {
        type: String,
        required: true
    },
	stop_time: {
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
	radius: {
		type: String,
		required: true
    },
	description: {
		type: String,
		required: false
    },
    create_date: {
        type: Date,
        default: Date.now
    }
});
var Schedule = module.exports = mongoose.model('schedules', scheduleSchema);
module.exports.get = function (callback, limit) {
    Schedule.find(callback).limit(limit);
}