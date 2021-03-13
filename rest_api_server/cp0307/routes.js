let router = require('express').Router();
router.get('/', function (req, res) {
    res.json({
        status: 'Server is running',
        message: 'Welcome to Bezpieczne Dziecko REST-API',
    });
});

var childController = require('./childController');
router.route('/children')
    .get(childController.parentgetchildren)	//rodzic pobiera tablicę wszystkich swoich dzieci
    .post(childController.parentaddchild); //rodzic dodaje nowe dziecko
router.route('/child/login')
    .post(childController.login);	//dziecko się loguje

var parentController = require('./parentController');
router.route('/parents')
    .post(parentController.addparent);	//dodanie nowego rodzica
router.route('/parent/login')
    .post(parentController.login);	//rodzic się loguje

var scheduleController = require('./scheduleController');
router.route('/parent/schedules')
    .get(scheduleController.parentgetschedules)	//rodzic pobiera tablicę wszystkich harmonogramów swoich dzieci
    .post(scheduleController.parentaddschedule);	//rodzic dodaje nowy harmonogram dla swojego dziecka
router.route('/child/schedules')
    .get(scheduleController.childgetschedules);	//dziecko pobiera tablicę wszystkich swoich harmonogramów
	
module.exports = router;	


/*
var childController = require('./childController');
router.route('/children')
    .get(childController.index)
    .post(childController.new);
router.route('/children/:child_id')
    .get(childController.view)
    .patch(childController.update)
    .put(childController.update)
    .delete(childController.delete);
router.route('/childrenlogin')
    .post(childController.login);	

var parentController = require('./parentController');
router.route('/parents')
    .get(parentController.index)
    .post(parentController.new);
router.route('/parents/:login')
    .get(parentController.view)
    .patch(parentController.update)
    .put(parentController.update)
    .delete(parentController.delete);	
router.route('/parentslogin')
    .post(parentController.login);
	

var scheduleController = require('./scheduleController');
router.route('/schedules')
    .get(scheduleController.index)
    .post(scheduleController.new);
router.route('/schedules/:schedule_id')
    .get(scheduleController.view)
    .patch(scheduleController.update)
    .put(scheduleController.update)
    .delete(scheduleController.delete);
	
*/