-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `author`
CREATE TABLE `author` (
  `id`    BIGINT    NOT NULL,
  `age`   INT(11),
  `genre` VARCHAR(255),
  `name`  VARCHAR(255),
  CONSTRAINT `author_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;