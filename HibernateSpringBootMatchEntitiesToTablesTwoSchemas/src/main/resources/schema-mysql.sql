-- Recreate `authorsdb` database
DROP DATABASE IF EXISTS `authorsdb`;
CREATE DATABASE `authorsdb`;
USE `authorsdb`;

-- Table `authorsdb.author`
CREATE TABLE `author` (
  `id`    BIGINT        NOT NULL AUTO_INCREMENT,
  `age`   INT(11)       NOT NULL,
  `genre` VARCHAR(255)  DEFAULT NULL,
  `name`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Recreate `booksdb` database
DROP DATABASE IF EXISTS `booksdb`;
CREATE DATABASE `booksdb`;
USE `booksdb`;

-- Table `booksdb.book`
CREATE TABLE `book` (
  `id`    BIGINT        NOT NULL AUTO_INCREMENT,  
  `isbn` VARCHAR(255)  DEFAULT NULL,
  `title`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `book_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;