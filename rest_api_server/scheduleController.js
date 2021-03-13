Schedule = require('./scheduleModel');
Token = require('./config/token.json');

exports.index = function (req, res) {
	if(req.query.token != Token.schedules){
		res.json({
			message: 'Wrong token'
		});
		return;
	}
	if(req.query.type == "0" && req.query.login == "0" && req.query._id == "0"){
		Schedule.get(function (err, schedules) {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			}
			res.json({
				status: "success",
				message: "Schedules retrieved successfully",
				data: schedules
			});
			return;
		});		
	}
	else if(req.query.type == "0" && req.query.login == "0" && req.query._id != "0"){
		Schedule.find({'_id' : req.query._id}, function (err, schedule) {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			}
			res.json({
				status: "success",
				message: "Schedule retrieved successfully",
				data: schedule
			});
		});
		return;	
	}
	else if(req.query.type == "Parent" && req.query.login != "0" && req.query._id == "0"){
		Schedule.find({'Parent' : req.query.type}, function (err, schedule) {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			}
			res.json({
				status: "success",
				message: "Schedule retrieved successfully",
				data: schedule
			});
		});
		return;	
	}
	else if(req.query.type == "Child" && req.query.login != "0" && req.query._id == "0"){
		Schedule.find({'Child' : req.query.type}, function (err, schedule) {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			}
			res.json({
				status: "success",
				message: "Schedule retrieved successfully",
				data: schedule
			});
		});
		return;	
	}
};


exports.new = function (req, res) {
	if(req.query.token != Token.schedules){
		res.json({
			message: 'Wrong token'
		});
		return;
	}
	Schedule.find({'_id' : req.body._id}).exec((err, count) => {
		if (err) {
			res.json({
				status: "error",
				message: err,
			});
			return;
		};	
		var schedule = new Schedule();
		schedule.child = req.body.child;
		schedule.parent = req.body.parent;
		schedule.start_date = req.body.start_date;	
		schedule.start_time = req.body.start_time;
		schedule.stop_date = req.body.stop_date;
		schedule.stop_time = req.body.stop_time;
		schedule.longitude = req.body.longitude;
		schedule.latitude = req.body.latitude;	
		schedule.radius = req.body.radius;		
		schedule.description = req.body.description;		
		schedule.save(function (err) {
			if (err)
				res.json({
					status: "error",
					message: err
				});
			else
				res.json({
					status: "success",
					message: "Schedule registered successfully"
				});
		});
	});	
};



exports.update = function (req, res) {
	if(req.body.token != Token.schedules){
		res.json({
			message: 'Wrong token'
		});
		return;
	}	
    Schedule.find({'_id': req.body._id}, function (err, schedule) {
		if (err) {
			res.json({
				status: "error",
				message: err,
			});
			return;
		};
		var schedule = new Schedule();
		schedule.child = req.body.child;
		schedule.parent = req.body.parent;
		schedule.start_date = req.body.start_date;	
		schedule.start_time = req.body.start_time;
		schedule.stop_date = req.body.stop_date;
		schedule.stop_time = req.body.stop_time;
		schedule.longitude = req.body.longitude;
		schedule.latitude = req.body.latitude;	
		schedule.radius = req.body.radius;		
		schedule.description = req.body.description;			
        schedule.save(function (err) {
            if (err)
				res.json({
					status: "error",
					message: err
				});
            res.json({
                message: 'Schedule Info updated',
                data: schedule
            });
        });
    });
};


exports.delete = function (req, res) {
	if(req.body.token != Token.schedules){
		res.json({
			message: 'Wrong token'
		});
		return;
	}
	else{	
		Schedule.remove({ _id: req.body._id }, function (err, schedule) {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			};
			res.json({
				status: "success",
				message: 'Schedule deleted'
			});
		});	
	}
};