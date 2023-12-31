CREATE DATABASE IF NOT EXISTS messenger_db;

USE messenger_db;

CREATE TABLE IF NOT EXISTS users (
	id INT AUTO_INCREMENT,
	email VARCHAR(255) NOT NULL UNIQUE,
	username VARCHAR(31) NOT NULL UNIQUE,
	password BINARY(60) NOT NULL,
	creation_time DATETIME,
	PRIMARY KEY (id)
);
