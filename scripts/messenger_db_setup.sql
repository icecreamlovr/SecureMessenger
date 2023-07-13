CREATE DATABASE IF NOT EXISTS messenger_db;

USE messenger_db;

CREATE TABLE IF NOT EXISTS users (
    id INT,
    email VARCHAR(255),
    name VARCHAR(31),
    password BINARY(60),
    salt BINARY(16),
    creation_time DATETIME
);