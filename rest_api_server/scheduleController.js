Schedule = require('./scheduleModel');
Token = require('./config/token.json');

exports.index = function (req, res) {
	/*if(req.query.token != Token.schedules){
		res.json({
			message: 'Wrong token'
		});
		return;
	}*/
	if(req.query.type == "all"){
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
		}).sort({create_date:-1});		
	}
	else if(req.query.type == "detail"){
		Schedule.find({'_id' : req.query._id}, function (err, schedule) {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			}
			res.json(schedule);
		});
		return;	
	}
	else if(req.query.type == "current"){
		Schedule.findOne({'child' : req.query.child,'start':{$lt:new Date()},'stop':{ $gte:new Date()}}, function (err, schedule) {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			}
			res.json(schedule);
		}).sort({create_date:-1});
		return;	
	}
	else if(req.query.type == "child"){
		Schedule.find({'child' : req.query.child}, function (err, schedule) {
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
		}).sort({create_date:-1});
		return;	
	}
	else if(req.query.type == "parent"){
		Schedule.find({'parent' : req.query.parent}, function (err, schedule) {
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
		}).sort({create_date:-1});
		return;	
	}

};


exports.new = function (req, res) {
	/*if(req.query.token != Token.schedules){
		res.json({
			message: 'Wrong token'
		});
		return;
	}*/
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
		schedule.start= req.body.start;	
		schedule.stop = req.body.stop;
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
		schedule.start = req.body.start;	
		schedule.stop = req.body.stop;
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