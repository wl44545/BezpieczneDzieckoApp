Location = require('./locationModel');
Token = require('./config/token.json');

exports.index = function (req, res) {
	
	/*if(req.query.token != Token.locations){
		res.json({
			message: 'Wrong token'
		});
		return;
	}*/
		
	if(req.query.type == 'last'){
		const agg = [
		  {
			'$sort': {
			  'child': -1, 
			  'date': -1
			}
		  }, {
			'$group': {
				'_id': '$child',
				'date': {
					'$first': '$date'
				}
			}
		  }
		];
		
		Location.aggregate(agg, (err, result) => {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			}
			console.log(result)
			res.json({
				status: "success",
				message: "results retrieved successfully",
				data: result
			});
			return;
		});
		
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

	var res_schedule;
	var allow_lon;
	var allow_lat;
	var allow_location;
	var allow_radius;
	
	Schedule.findOne({'child' : req.body.child,'start':{$lt:new Date()},'stop':{ $gte:new Date()}}, function (err, schedule) {
		if(schedule == null){
			allow_lon = req.body.longitude;
			allow_lat = req.body.latitude;	
			allow_location = '-1';
		}else{
			allow_lon = parseFloat(schedule.longitude);
			allow_lat = parseFloat(schedule.latitude);
			allow_radius = parseFloat(schedule.radius);	
			current_lon = parseFloat(req.body.longitude);
			current_lat = parseFloat(req.body.latitude);
		
			var earth_radius = 6371000;
			diff_lon = (current_lon - allow_lon)*(Math.PI/180);
			diff_lat = (current_lat - allow_lat)*(Math.PI/180);
			var a = Math.sin(diff_lat / 2) * Math.sin(diff_lat / 2) + Math.cos(allow_lat*(Math.PI/180)) * Math.cos(current_lat*(Math.PI/180)) * Math.sin(diff_lon / 2) * Math.sin(diff_lon / 2);
			var b = 2 * Math.asin(Math.sqrt(a));
			var distance = earth_radius * b;
			
			if(distance<=allow_radius)
				allow_location = '0';
			else
				allow_location = '1';
			
			console.log(allow_lat)
			console.log(allow_lon)
			console.log(current_lat)
			console.log(current_lon)
			console.log(distance)
			console.log(allow_location)
		}
	}).sort({create_date:-1});

	
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
		location.latitude = req.body.latitude;
		location.longitude = req.body.longitude;
		location.location = allow_location;
		location.status = req.body.status;		
		location.alarm = req.body.alarm;	
		
		location.save(function (err) {
			if (err){
				res.json({
					code: "-1",
					status: "error",
					message: err
				});
			}
			else{
				res.json({
					code: "0",
					status: "success",
					message: "Location registered successfully"
				});
			}
		});
	});	
};
