let express = require('express');
let bodyParser = require('body-parser');
let mongoose = require('mongoose');
let apiRoutes = require("./routes");

let app = express();
app.use(bodyParser.urlencoded({
    extended: true
}));
app.use(bodyParser.json());

mongoose.connect('mongodb+srv://admin:OtzQbrG4okTZaQmv@bezpiecznedziecko.r4mfn.mongodb.net/bezpiecznedziecko', { useNewUrlParser: true, useUnifiedTopology: true});

var db = mongoose.connection;
if(!db)
    console.log("Error connecting db")
else
    console.log("Db connected successfully")

var port = process.env.PORT || 8080;

app.get('/', (req, res) => res.send('Welcome to Bezpieczne Dziecko REST-API'));
app.use('/', apiRoutes);
app.listen(port, function () {
    console.log("Server is running on port " + port);
});