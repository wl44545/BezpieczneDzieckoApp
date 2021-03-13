Child = require('./childModel');


//rodzic pobiera tablicę wszystkich swoich dzieci
exports.parentgetchildren = function (req, res) {
    Child.get({'parent': req.params.parent}, function (err, schedules) {
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


//rodzic dodaje nowe dziecko
exports.parentaddchild = function (req, res) {
	Child.countDocuments({'login' : req.body.login}).exec((err, count) => {
		if (err) {
			res.send(err);
			return;
		};
		if (count != 0){
			res.json(2);
			return;
		}
		var child = new Child();
		child.login = req.body.login;
		child.password = req.body.password;
		child.salt = req.body.salt;
		child.first_name = req.body.first_name;
		child.last_name = req.body.last_name;
		child.parent = req.body.parent;
		child.save(function (err) {
			if (err)
				res.json(err);
			else
				res.json({
					message: 'New child created!',
					data: child
				});
		});
	});	
};


//dziecko się loguje
exports.login = function (req, res) {
	Child.countDocuments({'login' : req.body.login}).exec((err, count) => {
		if (err) {
			res.send(err);
			return;
		};
		if (count == 0){
			res.json(2);
			return;
		}
		Child.find({'login' : req.body.login}, function (err, child) {
			if (err)
				res.send(err);
			var password = child[0]["password"];
			var mypassword = req.body.password;
			if (password == mypassword)
				res.json(0);
			else
				res.json(1);
		});
	});
};







/*
exports.login = function (req, res) {
	Child.countDocuments({'login' : req.body.login}).exec((err, count) => {
		if (err) {
			res.send(err);
			return;
		};
		if (count == 0){
			res.json(2);
			return;
		}
		Child.find({'login' : req.body.login}, function (err, child) {
			if (err)
				res.send(err);
			var password = child[0]["password"];
			var mypassword = req.body.password;
			if (password == mypassword)
				res.json(0);
			else
				res.json(1);
		});
	});
};

exports.index = function (req, res) {
    Child.get(function (err, children) {
        if (err) {
            res.json({
                status: "error",
                message: err,
            });
        }
        res.json({
            status: "success",
            message: "Children retrieved successfully",
            data: children
        });
    });
};

exports.new = function (req, res) {
    var child = new Child();
    child.login = req.body.login;
    child.password = req.body.password;
	child.salt = req.body.salt;
    child.first_name = req.body.first_name;
    child.last_name = req.body.last_name;
	child.parent = req.body.parent;
    child.save(function (err) {
        if (err)
            res.json(err);
        else
            res.json({
                message: 'New child created!',
                data: child
            });
    });
};

exports.view = function (req, res) {
    Child.findById(req.params.contact_id, function (err, child) {
        if (err)
            res.send(err);
        res.json({
            message: 'Child details loading..',
            data: child
        });
    });
};

exports.update = function (req, res) {
    Child.findById(req.params.child_id, function (err, child) {
        if (err)
            res.send(err);
		child.login = req.body.login;
		child.password = req.body.password;
		child.salt = req.body.salt;
		child.first_name = req.body.first_name;
		child.last_name = req.body.last_name;
		child.parent = req.body.parent;			
        child.save(function (err) {
            if (err)
                res.json(err);
            res.json({
                message: 'Child Info updated',
                data: child
            });
        });
    });
};

exports.delete = function (req, res) {
    Child.remove({
        _id: req.params.child_id
    }, function (err, child) {
        if (err)
            res.send(err);
        res.json({
            status: "success",
            message: 'Child deleted'
        });
    });
};
*/