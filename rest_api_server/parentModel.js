var mongoose = require('mongoose');
var parentSchema = mongoose.Schema({
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
	address: {
		type: String,
		required: false
    },
	postal_code: {
		type: String,
		required: false
    },
	city: {
		type: String,
		required: false
    },
	country: {
		type: String,
		required: false
    },
	account_type: {
		type: String,
		required: true
    },
    create_date: {
        type: Date,
        default: Date.now
    }
});
var Parent = module.exports = mongoose.model('users.parents', parentSchema);
module.exports.get = function (callback, limit) {
    Parent.find(callback).limit(limit);
}