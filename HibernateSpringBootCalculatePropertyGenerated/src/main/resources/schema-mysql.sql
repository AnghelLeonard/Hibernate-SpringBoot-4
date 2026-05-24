-- Recreate database
USE `bookstoredb`;
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `book`
CREATE TABLE `book` (
  `discounted` DOUBLE AS (`price` - `price` * 0.25),
--`discounted` DOUBLE GENERATED ALWAYS AS ((`price` - `price` * 0.25)) STORED,
  `price`      FLOAT(53) NOT NULL,
  `id`         BIGINT NOT NULL AUTO_INCREMENT,
  `isbn`       VARCHAR(255),
  `title`      VARCHAR(255),
 PRIMARY KEY (`id`)) engine=innodb
