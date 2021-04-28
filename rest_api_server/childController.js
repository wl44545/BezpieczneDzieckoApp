Child = require('./childModel');
Token = require('./config/token.json');

exports.index = function (req, res) {
	if(req.query.token != Token.children){
		res.json({
			message: 'Wrong token'
		});
		return;
	}
	if(req.query.login == "0" && req.query.parent == "0"){
		Child.get(function (err, children) {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			}
			res.json({
				status: "success",
				message: "Children retrieved successfully",
				data: children
			});
			return;
		});		
	}
	if(req.query.login != "0" && req.query.parent == "0"){
		Child.find({'login' : req.query.login}, function (err, child) {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			}
			res.json(child);
		});
		return;	
	}	
	if(req.query.login == "0" && req.query.parent != "0"){
		Child.find({'parent' : req.query.parent}, function (err, child) {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			}	
			res.json({
				status: "success",
				message: "Child retrieved successfully",
				data: child
			});
		});
		return;	
	}		
};


exports.new = function (req, res) {
	if(req.body.token != Token.children){
		res.json({
			message: 'Wrong token'
		});
		return;
	}
	Child.countDocuments({'login' : req.body.login}).exec((err, count) => {
		if (err) {
			res.json({
				code: "-1",
				status: "error",
				message: err,
			});
			return;
		};
		if (count != 0){
			res.json({
				code: "1",
				status: "error",
				message: "Child already exists"
			});
			return;
		}
		var child = new Child();
		child.login = req.body.login;
		child.password = req.body.password;
		child.salt = req.body.salt;	
		child.parent = req.body.parent;
		child.first_name = req.body.first_name;
		child.last_name = req.body.last_name;
		child.email = req.body.email;
		child.phone_number = req.body.phone_number;
		child.pesel = req.body.pesel;
		child.gender = req.body.gender;
		child.save(function (err) {
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
					message: "Child registered successfully"
				});
		});
	});	
};



exports.update = function (req, res) {
	if(req.query.token != Token.children){
		res.json({
			message: 'Wrong token'
		});
		return;
	}
	Child.deleteOne({'login': req.query.login}, function (err, child) {
		if (err)
			res.send(err);
		res.json({
			status: "success",
			message: 'Child deleted'
		});
	});	
	Child.countDocuments({'login' : req.body.login}).exec((err, count) => {
		if (err) {
			res.json({
				code: "-1",
				status: "error",
				message: err,
			});
			return;
		};
		if (count != 0){
			res.json({
				code: "1",
				status: "error",
				message: "Child already exists"
			});
			return;
		}
		var child = new Child();
		child.login = req.body.login;
		child.password = req.body.password;
		child.salt = req.body.salt;	
		child.parent = req.body.parent;
		child.first_name = req.body.first_name;
		child.last_name = req.body.last_name;
		child.email = req.body.email;
		child.phone_number = req.body.phone_number;
		child.pesel = req.body.pesel;
		child.gender = req.body.gender;
		child.save(function (err) {
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
					message: "Child registered successfully"
				});
		});
	});	


	
	/*Child.updateOne({'login': req.query.login}, { $set: { first_name: req.query.first_name} }, function (err, child) {
		if (err) {
			res.json({
				status: "error",
				message: err,
			});
			return;
		};
		res.json(child);
		console.log(child);
		
		
	});*/
   /*Child.find({'login': req.query.login}, function (err, child) {
		if (err) {
			res.json({
				status: "error",
				message: err,
			});
			return;
		};
		var child = new Child();
		child.login = req.query.login;
		child.password = req.query.password;
		child.salt = req.query.salt;	
		child.parent = req.query.parent;
		child.first_name = req.query.first_name;
		child.last_name = req.query.last_name;
		child.email = req.query.email;
		child.phone_number = req.query.phone_number;
		child.pesel = req.query.pesel;
		child.gender = req.query.gender;		
        child.save(function (err) {
            if (err)
				res.json({
					status: "error",
					message: err
				});
            res.json({
                message: 'Child Info updated',
                data: child
            });
        });
    });*/
};


exports.delete = async function (req, res) {
	if(req.query.token != Token.children){
		res.json({
			message: 'Wrong token'
		});
		return;
	}
	Child.deleteOne({'login': req.query.login}, function (err, child) {
		if (err)
			res.send(err);
		res.json({
			status: "success",
			message: 'Child deleted'
		});
	});	

};