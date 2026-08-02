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
  CONSTRAINT `author_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `book`
CREATE TABLE `book` (
  `id`          BIGINT        NOT NULL AUTO_INCREMENT,
  `author_id`   BIGINT        NOT NULL,
  `isbn`        VARCHAR(255)  DEFAULT NULL,
  `title`       VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (`id`),
  CONSTRAINT `book_author_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `ebook`
CREATE TABLE `ebook` (  
  `ebook_book_id`   BIGINT      NOT NULL,
  `format`        VARCHAR(255)  DEFAULT NULL,  
  CONSTRAINT `author_pk` PRIMARY KEY (`ebook_book_id`),
  CONSTRAINT `ebook_book_fk` FOREIGN KEY (`ebook_book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `ebook`
CREATE TABLE `paperback` (  
  `paperback_book_id`   BIGINT         NOT NULL,
  `size_in`             VARCHAR(255)  DEFAULT NULL,  
  `weight_lbs`          VARCHAR(255)  DEFAULT NULL,  
  CONSTRAINT `author_pk` PRIMARY KEY (`paperback_book_id`),
  CONSTRAINT `paperback_book_fk` FOREIGN KEY (`paperback_book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;