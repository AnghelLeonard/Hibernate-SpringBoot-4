-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `chapter`
CREATE TABLE `author` (
  `id`      BIGINT        NOT NULL AUTO_INCREMENT,
  `age`     SMALLINT      NOT NULL,
  `name`    VARCHAR(255)  DEFAULT NULL,
  `genre`   VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;