-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `inventory`
CREATE TABLE `inventory` (
  `id`       BIGINT        NOT NULL,
  `quantity` INTEGER       NOT NULL,  
  `title`    VARCHAR(255)  DEFAULT NULL,
  `version`  SMALLINT,
  CONSTRAINT `author_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;