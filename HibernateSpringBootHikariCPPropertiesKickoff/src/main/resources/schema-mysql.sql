-- Recreate database
USE `numberdb`;
DROP DATABASE IF EXISTS `numberdb`;
CREATE DATABASE `numberdb`;
USE `numberdb`;

-- Create the table 
CREATE TABLE `numberdb`.`ints` (
  `nr` INT DEFAULT NULL
);