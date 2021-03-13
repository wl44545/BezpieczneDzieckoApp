let config = require('./config/email.json');
let nodemailer = require('nodemailer');

exports.new = function (req, res) {
	
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
	let mailOptions = {
		from: config.sender,
		to: req.body.recipient,
		subject: req.body.subject,
		text: req.body.message
	};
	transporter.sendMail(mailOptions, (error, info) => {
		if (error) {
			return console.log(error.message);
		}
		res.json('success');
	});
		
};