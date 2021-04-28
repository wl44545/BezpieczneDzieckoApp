var mongoose = require('mongoose');
var childSchema = mongoose.Schema({
    login: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
    },
    salt: {
        type: String,
        required: true
    },
    parent: {
        type: String,
        required: true
    },
    first_name: {
        type: String,
        required: true
    },
	last_name: {
		type: String,
		required: true
    },
	email: {
		type: String,
		required: true
    },
	phone_number: {
		type: String,
		required: true
    },	
	pesel: {
		type: String,
		required: false
    },
	gender: {
		type: String,
		required: false
    },
    create_date: {
        type: Date,
        default: Date.now
    }
});
var Child = module.exports = mongoose.model('users.child', childSchema);
module.exports.get = function (callback, limit) {
    Child.find(callback).limit(limit);
}