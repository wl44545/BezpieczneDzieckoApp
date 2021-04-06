Location = require('./locationModel');
Token = require('./config/token.json');

exports.index = function (req, res) {
		
	if(req.query.token != Token.locations){
		res.json({
			message: 'Wrong token'
		});
		return;
	}
	
	
	Location.findOne({'child' : req.query.child}, function (err, location) {
		if (err) {
			res.json({
				status: "error",
				message: err,
			});
			return;
		}
		res.json(location);
	}).sort({date:-1});
	return;
	
	
};


exports.new = function (req, res) {
	/*if(req.query.token != Token.locations){
		res.json({
			message: 'Wrong token'
		});
		return;
	}*/
	
	Location.find({'_id' : req.body._id}).exec((err, count) => {
		if (err) {
			res.json({
				status: "error",
				message: err,
			});
			return;
		};	
		var location = new Location();
		
		location.child = req.body.child;
		location.longitude = req.body.longitude;
		location.latitude = req.body.latitude;	
		location.status = req.body.status;		
		location.alarm = req.body.alarm;	
		
		location.save(function (err) {
			if (err)
				res.json({
					code: "-1",
					status: "error",
					message: err
				});
			else
				res.json({
					code: "0",
					status: "success",
					message: "Location registered successfully"
				});
		});
	});	
};
