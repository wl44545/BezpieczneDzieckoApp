Schedule = require('./scheduleModel');


//rodzic pobiera tablicę wszystkich harmonogramów 
exports.parentgetschedules = function (req, res) {
    Schedule.get({'parent': req.params.parent}, function (err, schedules) {
        if (err) {
            res.json({
                status: "error",
                message: err,
            });
        }
        res.json({
            status: "success",
            message: "Schedules retrieved successfully",
            data: schedules
        });
    });
};


//dziecko pobiera tablicę wszystkich swoich harmonogramów
exports.parentgetschedules = function (req, res) {
    Schedule.get({'child': req.params.child}, function (err, schedules) {
        if (err) {
            res.json({
                status: "error",
                message: err,
            });
        }
        res.json({
            status: "success",
            message: "Schedules retrieved successfully",
            data: schedules
        });
    });
};


//rodzic dodaje nowy harmonogram dla swojego dziecka
exports.new = function (req, res) {
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
            res.json(err);
        else
            res.json({
                message: 'New schedule created!',
                data: schedule
            });
    });
};



/*
exports.index = function (req, res) {
    Schedule.get(function (err, schedules) {
        if (err) {
            res.json({
                status: "error",
                message: err,
            });
        }
        res.json({
            status: "success",
            message: "Schedules retrieved successfully",
            data: schedules
        });
    });
};

exports.new = function (req, res) {
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
            res.json(err);
        else
            res.json({
                message: 'New schedule created!',
                data: schedule
            });
    });
};

exports.view = function (req, res) {
    Schedule.findById(req.params.contact_id, function (err, schedule) {
        if (err)
            res.send(err);
        res.json({
            message: 'Schedule details loading..',
            data: schedule
        });
    });
};

exports.update = function (req, res) {
    Schedule.findById(req.params.schedule_id, function (err, schedule) {
        if (err)
            res.send(err);
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
                res.json(err);
            res.json({
                message: 'Schedule Info updated',
                data: schedule
            });
        });
    });
};

exports.delete = function (req, res) {
    Schedule.remove({
        _id: req.params.schedule_id
    }, function (err, schedule) {
        if (err)
            res.send(err);
        res.json({
            status: "success",
            message: 'Schedule deleted'
        });
    });
};
*/