-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `author`
CREATE TABLE `author` (
  `id`      BIGINT        NOT NULL AUTO_INCREMENT,
  `age`     INT(11)       NOT NULL,
  `genre`   VARCHAR(255)  DEFAULT NULL,
  `name`    VARCHAR(255)  DEFAULT NULL,
  `deleted` BIT DEFAULT   0,
  CONSTRAINT `author_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `book`
CREATE TABLE `book` (
  `id`        BIGINT        NOT NULL AUTO_INCREMENT,    
  `title`     VARCHAR(255)  DEFAULT NULL,
  `isbn`      VARCHAR(255)  DEFAULT NULL,
  `deleted`   BIT DEFAULT   0,
  CONSTRAINT `book_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `author_book`
CREATE TABLE `author_book` (
  `author_id` BIGINT        NOT NULL,
  `book_id`   BIGINT        NOT NULL,
  `deleted`   BIT DEFAULT   0,
  CONSTRAINT `author_book_uk` UNIQUE (`author_id`, `book_id`),
  CONSTRAINT `author_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT `book_fk` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION  
) ENGINE=InnoDB DEFAULT CHARSET=latin1;