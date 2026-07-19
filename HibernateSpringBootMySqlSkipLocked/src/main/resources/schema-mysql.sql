-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `author`
CREATE TABLE `book` (
  `id`      BIGINT        NOT NULL AUTO_INCREMENT, 
  `isbn`   VARCHAR(255)   DEFAULT NULL,
  `title`  VARCHAR(255)   DEFAULT NULL,
  `status` ENUM ('APPROVED','PENDING','REJECTED'),
  CONSTRAINT `author_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;