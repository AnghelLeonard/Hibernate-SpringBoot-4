-- Recreate database
USE `bookstoredb`;
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `book`
CREATE TABLE `book` (
  `id` BIGINT   NOT NULL,
  `author` VARCHAR(255) DEFAULT NULL,
  `isbn` VARCHAR(255) DEFAULT NULL,
  `title` VARCHAR(255) DEFAULT NULL,
  CONSTRAINT `book_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `book_review`
CREATE TABLE `book_review` (
  `id`      BIGINT       NOT NULL AUTO_INCREMENT,  
  `book_id` BIGINT       DEFAULT NULL, 
  `content` VARCHAR(255) DEFAULT NULL, 
  `email`   VARCHAR(255) DEFAULT NULL, 
  `status`  ENUM ('ACCEPT','CHECK','REJECT'), 
  CONSTRAINT `book_review_pk` PRIMARY KEY (id),
  CONSTRAINT `book_book_review_fk` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1