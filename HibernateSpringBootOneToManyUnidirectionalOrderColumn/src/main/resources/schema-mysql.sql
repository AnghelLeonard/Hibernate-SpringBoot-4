-- Recreate database
USE `bookstoredb`;
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `author`
CREATE TABLE `author` (
  `id`    BIGINT        NOT NULL AUTO_INCREMENT,
  `age`   INT(11)       NOT NULL,
  `genre` VARCHAR(255)  DEFAULT NULL,
  `name`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `book`
CREATE TABLE `book` (
  `id`        BIGINT        NOT NULL AUTO_INCREMENT,    
  `title`     VARCHAR(255)  DEFAULT NULL,
  `isbn`      VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `book_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `author_books`
CREATE TABLE `author_books` (
  `author_id`   BIGINT        NOT NULL,
  `books_id`    BIGINT        NOT NULL,
  `books_order` BIGINT        NOT NULL,
  CONSTRAINT `author_book_uk` UNIQUE (`author_id`, `books_id`, `books_order`),
  CONSTRAINT `author_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT `books_fk` FOREIGN KEY (`books_id`) REFERENCES `book` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION  
) ENGINE=InnoDB DEFAULT CHARSET=latin1;