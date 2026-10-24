-- Database schema for the User Management application (MySQL 8)

CREATE DATABASE IF NOT EXISTS usermanagement
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE usermanagement;

CREATE TABLE IF NOT EXISTS users (
  id      INT          NOT NULL AUTO_INCREMENT,
  name    VARCHAR(120) NOT NULL,
  email   VARCHAR(220) NOT NULL,
  country VARCHAR(120),
  PRIMARY KEY (id)
);

-- Optional sample data
INSERT INTO users (name, email, country) VALUES
  ('Alice Martin', 'alice.martin@example.com', 'France'),
  ('Youssef Alaoui', 'youssef.alaoui@example.com', 'Morocco');
