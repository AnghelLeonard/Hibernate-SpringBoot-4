-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `author`
CREATE TABLE `author` (
  `id`    BIGINT        NOT NULL AUTO_INCREMENT,
  `age`   INT(11)       NOT NULL,
  `genre` VARCHAR(255)  DEFAULT NULL,
  `name`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `book`
CREATE TABLE `book` (
  `id`        BIGINT         NOT NULL AUTO_INCREMENT,  
  `author_id` BIGINT,
  `dtype`     TINYINT(1)     NOT NULL CHECK (`dtype` in (1,2,3)),
  `format`    VARCHAR(255),
  `isbn`      VARCHAR(255),
  `size_in`   VARCHAR(255), 
  `title`     VARCHAR(255),
  `weight_lbs` VARCHAR(255),
  CONSTRAINT `book_pk` PRIMARY KEY (id),
  CONSTRAINT `book_author_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;