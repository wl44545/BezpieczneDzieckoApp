Parent = require('./parentModel');
Token = require('./config/token.json');

exports.index = function (req, res) {
	if(req.query.token != Token.parents){
		res.json({
			message: 'Wrong token'
		});
		return;
	}
	if(req.query.login == "0"){
		Parent.get(function (err, parents) {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			}
			res.json({
				status: "success",
				message: "Parents retrieved successfully",
				data: parents
			});
			return;
		});		
	}
	else{
		Parent.find({'login' : req.query.login}, function (err, parent) {
			if (err) {
				res.json({
					status: "error",
					message: err,
				});
				return;
			}
			res.json(parent);			
			/*res.json({
				status: "success",
				message: "Parent retrieved successfully",
				data: parent
			});*/
		});
		return;	
	}
};


exports.new = function (req, res) {
	if(req.body.token != Token.parents){
		res.json({
			message: 'Wrong token'
		});
		return;
	}
	Parent.countDocuments({'login' : req.body.login}).exec((err, count) => {
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
				message: "Parent already exists"
			});
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
		parent.pesel = req.body.pesel;
		parent.gender = req.body.gender;
		parent.address = req.body.address;
		parent.postal_code = req.body.postal_code;
		parent.city = req.body.city;
		parent.country = req.body.country;
		parent.account_type = req.body.account_type;
		
		//sms
		config = require('./config/sms.json');
		let open = require('open');
		var recipient = req.body.phone_number;
		var message = "Witamy w aplikacji Bezpieczne Dziecko. Twoj login to "+req.body.login+". Zaloguj sie i poznaj mozliwosci aplikacji.";
		var login = config.login;
		var password = config.password;
		var sender = config.sender;
		var msg_type = config.msg_type;
		var url = 'https://api.gsmservice.pl/v5/send.php?login='+login+'&pass='+password+'&recipient='+recipient+'&message='+message+'&sender='+sender+'&msg_type='+msg_type;
		open(url);
		
		//mail
		config = require('./config/email.json');
		let nodemailer = require('nodemailer');	
		let transporter = nodemailer.createTransport({
			host: 'smtp.gmail.com',
			port: 465,
			secure: true,
			requireTLS: true,
			auth: {
				user: config.login,
				pass: config.password
			}
		});
		var message = "Witamy w aplikacji Bezpieczne Dziecko. Twoj login to "+req.body.login+". Zaloguj sie i poznaj mozliwosci aplikacji.";
		let mailOptions = {
			from: config.sender,
			to: req.body.email,
			subject: "Bezpieczne Dziecko - Rejestracja",
			text: message
		};
		transporter.sendMail(mailOptions, (error, info) => {
			if (error) {
				return console.log(error.message);
			}
		});
		
		
		parent.save(function (err) {
			if (err)
				res.json({
					code: "-1",
					status: "error",
					message: err
				});
			else{
				res.json({
					code: "0",
					status: "success",
					message: "Parent registered successfully"
				});
				

					
				
				
				
			}
		});
	});	
};



exports.update = function (req, res) {
	if(req.body.token != Token.parents){
		res.json({
			message: 'Wrong token'
		});
		return;
	}	
    Parent.find({'login': req.body.login}, function (err, parent) {
		if (err) {
			res.json({
				status: "error",
				message: err,
			});
			return;
		};
		var parent = new Parent();
		parent.login = req.body.login;
		parent.password = req.body.password;
		parent.salt = req.body.salt;	
		parent.first_name = req.body.first_name;
		parent.last_name = req.body.last_name;
		parent.email = req.body.email;
		parent.phone_number = req.body.phone_number;
		parent.pesel = req.body.pesel;
		parent.gender = req.body.gender;
		parent.address = req.body.address;
		parent.postal_code = req.body.postal_code;
		parent.city = req.body.city;
		parent.country = req.body.country;
		parent.account_type = req.body.account_type;			
        parent.save(function (err) {
            if (err)
				res.json({
					status: "error",
					message: err
				});
            res.json({
                message: 'Parent Info updated',
                data: parent
            });
        });
    });
};


exports.delete = function (req, res) {
	if(req.body.token != Token.parents){
		res.json({
			message: 'Wrong token'
		});
		return;
	}
	else{	
		Parent.remove({
			login: req.body.login
		}, function (err, parent) {
			if (err)
				res.send(err);
			res.json({
				status: "success",
				message: 'Parent deleted'
			});
		});	
	}
};