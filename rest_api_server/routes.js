let router = require('express').Router();
router.get('/', function (req, res) {
    res.json({
        status: 'Server is running',
        message: 'Welcome to Bezpieczne Dziecko REST-API',
    });
});

	
var parentController = require('./parentController');
router.route('/parents')
    .get(parentController.index)
    .post(parentController.new)
    .patch(parentController.update)
    .put(parentController.update)
    .delete(parentController.delete);

var childController = require('./childController');	
router.route('/children')
    .get(childController.index)
    .post(childController.new)
    .patch(childController.update)
    .put(childController.update)
    .delete(childController.delete);	
	
var scheduleController = require('./scheduleController');	
router.route('/schedules')
    .get(scheduleController.index)
    .post(scheduleController.new)
    .patch(scheduleController.update)
    .put(scheduleController.update)
    .delete(scheduleController.delete);
	
var emailController = require('./emailController');	
router.route('/email')
    .post(emailController.new);
	
var smsController = require('./smsController');	
router.route('/sms')
    .post(smsController.new);	
	

module.exports = router;	