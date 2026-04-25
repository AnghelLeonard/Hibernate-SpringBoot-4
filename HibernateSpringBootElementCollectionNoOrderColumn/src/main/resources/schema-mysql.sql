-- Recreate database
USE `bookstoredb`;
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `shopping_cart`
CREATE TABLE `shopping_cart` (
  `id`    BIGINT        NOT NULL AUTO_INCREMENT,  
  `owner` VARCHAR(255)  DEFAULT NULL,  
  CONSTRAINT `shopping_cart_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `shopping_cart_books`
CREATE TABLE `shopping_cart_books` (
  `shopping_cart_id`  BIGINT        NOT NULL,    
  `title`             VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `shopping_cart_books_fk` FOREIGN KEY (`shopping_cart_id`) REFERENCES `shopping_cart` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;