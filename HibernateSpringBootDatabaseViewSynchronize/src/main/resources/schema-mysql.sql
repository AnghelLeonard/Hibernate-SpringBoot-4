-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `orders`
CREATE TABLE `orders` (
  `id`          BIGINT        NOT NULL AUTO_INCREMENT,
  `amount`      DECIMAL(38,2) NOT NULL,
  `created_at`  DATETIME(6)   NOT NULL,
  `customer_id` BIGINT        NOT NULL,  
  CONSTRAINT `order_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

