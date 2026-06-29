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
  `author_id` BIGINT        NOT NULL,
  `title`     VARCHAR(255)  DEFAULT NULL,
  `isbn`      VARCHAR(255)  DEFAULT NULL,
  `deleted`   BIT DEFAULT   0,
  CONSTRAINT `book_pk` PRIMARY KEY (id),
  CONSTRAINT `book_author_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;