-- Recreate database
USE `citylots_db`;
DROP DATABASE IF EXISTS `citylots_db`;
CREATE DATABASE `citylots_db`;
USE `citylots_db`;

-- Create the `lots` table 
CREATE TABLE `lots` (
  `lot` json DEFAULT NULL
);