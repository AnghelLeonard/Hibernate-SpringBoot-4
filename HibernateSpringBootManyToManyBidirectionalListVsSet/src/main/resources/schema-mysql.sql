-- Recreate database
USE `bookstoredb`;
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `author_list`
CREATE TABLE `author_list` (
  `id`    BIGINT        NOT NULL AUTO_INCREMENT,
  `age`   INT(11)       NOT NULL,
  `genre` VARCHAR(255)  DEFAULT NULL,
  `name`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `book_list`
CREATE TABLE `book_list` (
  `id`        BIGINT        NOT NULL AUTO_INCREMENT,    
  `title`     VARCHAR(255)  DEFAULT NULL,
  `isbn`      VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `book_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `author_set`
CREATE TABLE `author_set` (
  `id`    BIGINT        NOT NULL AUTO_INCREMENT,
  `age`   INT(11)       NOT NULL,
  `genre` VARCHAR(255)  DEFAULT NULL,
  `name`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `book_set`
CREATE TABLE `book_set` (
  `id`        BIGINT        NOT NULL AUTO_INCREMENT,    
  `title`     VARCHAR(255)  DEFAULT NULL,
  `isbn`      VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `book_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `author_book_list`
CREATE TABLE `author_book_list` (
  `author_id` BIGINT        NOT NULL,
  `book_id`   BIGINT        NOT NULL,
  CONSTRAINT `author_book_list_uk` UNIQUE (`author_id`, `book_id`),
  CONSTRAINT `author_list_fk` FOREIGN KEY (`author_id`) REFERENCES `author_list` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT `book_list_fk` FOREIGN KEY (`book_id`) REFERENCES `book_list` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION  
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `author_book_set`
CREATE TABLE `author_book_set` (
  `author_id` BIGINT        NOT NULL,
  `book_id`   BIGINT        NOT NULL,
  CONSTRAINT `author_book_set_uk` UNIQUE (`author_id`, `book_id`),
  CONSTRAINT `author_set_fk` FOREIGN KEY (`author_id`) REFERENCES `author_set` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT `book_set_fk` FOREIGN KEY (`book_id`) REFERENCES `book_set` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION  
) ENGINE=InnoDB DEFAULT CHARSET=latin1;