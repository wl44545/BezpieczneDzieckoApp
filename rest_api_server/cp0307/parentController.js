Parent = require('./parentModel');


//dodanie nowego rodzica
exports.addparent = function (req, res) {
	Parent.countDocuments({'login' : req.body.login}).exec((err, count) => {
		if (err) {
			res.send(err);
			return;
		};
		if (count != 0){
			res.json(2);
			return;
		}
		var parent = new Parent();
		parent.login = req.body.login;
		parent.password = req.body.password;
		parent.salt = req.body.salt;	
		parent.first_name = req.body.first_name;
		parent.last_name = req.body.last_name;
		parent.email = req.body.email;
		parent.phone_number = req.body.phone_number;
		parent.save(function (err) {
			if (err)
				res.json(err);
			else
				res.json({
					message: 'New parent created!',
					data: parent
				});
		});
	});	
};


//rodzic się loguje
exports.login = function (req, res) {
	Parent.countDocuments({'login' : req.body.login}).exec((err, count) => {
		if (err) {
			res.send(err);
			return;
		};
		if (count == 0){
			res.json(2);
			return;
		}
		Parent.find({'login' : req.body.login}, function (err, parent) {
			if (err)
				res.send(err);
			var password = parent[0]["password"];
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
	Parent.countDocuments({'login' : req.body.login}).exec((err, count) => {
		if (err) {
			res.send(err);
			return;
		};
		if (count == 0){
			res.json(2);
			return;
		}
		Parent.find({'login' : req.body.login}, function (err, parent) {
			if (err)
				res.send(err);
			var password = parent[0]["password"];
			var mypassword = req.body.password;
			if (password == mypassword)
				res.json(0);
			else
				res.json(1);
		});
	});
};

exports.index = function (req, res) {
    Parent.get(function (err, parents) {
        if (err) {
            res.json({
                status: "error",
                message: err,
            });
        }
        res.json({
            status: "success",
            message: "Parents retrieved successfully",
            data: parents
        });
    });
};

exports.new = function (req, res) {
    var parent = new Parent();
    parent.login = req.body.login;
    parent.password = req.body.password;
    parent.salt = req.body.salt;	
    parent.first_name = req.body.first_name;
    parent.last_name = req.body.last_name;
	parent.email = req.body.email;
	parent.phone_number = req.body.phone_number;
    parent.save(function (err) {
        if (err)
            res.json(err);
        else
            res.json({
                message: 'New parent created!',
                data: parent
            });
    });
};
   
exports.view = function (req, res) {
	Parent.find({'login' : req.params.login}, function (err, parent) {
        if (err)
            res.send(err);
        res.json({
            message: 'Parent details loading..',
            data: parent
        });
    });
};

exports.update = function (req, res) {
    Parent.findById(req.params.parent_id, function (err, parent) {
        if (err)
            res.send(err);
		parent.login = req.body.login;
		parent.password = req.body.password;
		parent.salt = req.body.salt;			
		parent.first_name = req.body.first_name;
		parent.last_name = req.body.last_name;
		parent.email = req.body.email;
		parent.phone_number = req.body.phone_number;			
        parent.save(function (err) {
            if (err)
                res.json(err);
            res.json({
                message: 'Parent Info updated',
                data: parent
            });
        });
    });
};

exports.delete = function (req, res) {
    Parent.remove({
        _id: req.params.parent_id
    }, function (err, parent) {
        if (err)
            res.send(err);
        res.json({
            status: "success",
            message: 'Parent deleted'
        });
    });
};
*/