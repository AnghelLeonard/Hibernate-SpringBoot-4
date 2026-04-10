-- Recreate database
USE `bookstoredb`;
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `book`
CREATE TABLE `book` (
  `id`        BIGINT        NOT NULL AUTO_INCREMENT,    
  `title`     VARCHAR(255)  DEFAULT NULL,
  `isbn`      VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `book_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `format`
CREATE TABLE `format` (
  `id`           BIGINT        NOT NULL AUTO_INCREMENT,    
  `format_type`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `format_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;