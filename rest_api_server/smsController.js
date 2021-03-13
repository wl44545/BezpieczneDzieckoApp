config = require('./config/sms.json');
let open = require('open');


exports.new = function (req, res) {
	
	var recipient = req.body.recipient;
	var message = req.body.message;		
	var login = config.login;
	var password = config.password;
	var sender = config.sender;
	var msg_type = config.msg_type;
	
	var url = 'https://api.gsmservice.pl/v5/send.php?login='+login+'&pass='+password+'&recipient='+recipient+'&message='+message+'&sender='+sender+'&msg_type='+msg_type;
	
	open(url);
	
	res.json(0);
	
};