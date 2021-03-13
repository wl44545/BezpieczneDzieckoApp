var config = require('../config/config.json');
var fs = require('fs');
var process = require('process');
var nodemailer = require('nodemailer');
var local_ip = require('ip');
const open = require('open');
let external_ip = require('ext-ip')();
var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
var crypto = require('crypto');
var express = require('express');
var bodyParser = require('body-parser');
var
  CircularJSON = require('circular-json'),
  obj = { foo: 'bar' },
  str
;

var genRandomString = function(length){
	return crypto.randomBytes(Math.ceil(length/2))
		.toString('hex')
		.slice(0,length);
};

var sha512 = function(password,salt){
	var hash = crypto.createHmac('sha512',salt);
	hash.update(password);
	var value = hash.digest('hex');
	return {
		salt:salt,
		passwordHash:value
	};
};

function saltHashPassword(userPassword){
	var salt = genRandomString(16);
	var passwordData = sha512(userPassword,salt);
	return passwordData;
}

function checkHashPassword(userPassword,salt){
	var passwordData = sha512(userPassword,salt);
	return passwordData;	
}