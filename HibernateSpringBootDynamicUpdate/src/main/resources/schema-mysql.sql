-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `author`
CREATE TABLE `author` (
  `id`        BIGINT        NOT NULL AUTO_INCREMENT,
  `age`       INT(11)       NOT NULL,
  `genre`     VARCHAR(255)  DEFAULT NULL,
  `name`      VARCHAR(255)  DEFAULT NULL,
  `sellrank`  INT(11)       NOT NULL,
  `royalties` INT(11)       NOT NULL,
  `rating`    INT(11)       NOT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;