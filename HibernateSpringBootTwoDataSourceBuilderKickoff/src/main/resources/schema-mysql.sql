-- Recreate databases
DROP DATABASE IF EXISTS `authorsdb`;
CREATE DATABASE `authorsdb`;
DROP DATABASE IF EXISTS `booksdb`;
CREATE DATABASE `booksdb`;

-- Table `author`
CREATE TABLE `authorsdb`.`author` (
  `id`    BIGINT        NOT NULL AUTO_INCREMENT,
  `age`   INT(11)       NOT NULL,
  `genre` VARCHAR(255)  DEFAULT NULL,
  `name`  VARCHAR(255)  DEFAULT NULL,
  `books` TEXT          DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `book`
CREATE TABLE `booksdb`.`book` (
  `id`        BIGINT        NOT NULL AUTO_INCREMENT,  
  `authors`   TEXT          DEFAULT NULL,
  `title`     VARCHAR(255)  DEFAULT NULL,
  `isbn`      VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `book_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

