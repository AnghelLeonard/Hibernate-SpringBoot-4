-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `book`
CREATE TABLE `book` (
  `id`                  BIGINT        NOT NULL AUTO_INCREMENT,  
  `title`               VARCHAR(255)  DEFAULT NULL,
  `isbn`                VARCHAR(255)  DEFAULT NULL,
  `price`               DOUBLE        DEFAULT NULL,
  `discount_percentage` DOUBLE DEFAULT NULL,
  CONSTRAINT `book_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;