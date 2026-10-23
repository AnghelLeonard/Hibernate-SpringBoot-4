-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `orders`
CREATE TABLE `orders` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `customer_name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)    
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `order_lines`
CREATE TABLE `order_lines` (
    `id`       BIGINT         NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT         NOT NULL,
    `isbn`     VARCHAR(13)    NOT NULL,
    `quantity` INT            NOT NULL,
    `price`    DECIMAL(10, 2) NOT NULL,    
    PRIMARY KEY (`id`),    
    CONSTRAINT `fk_order_lines_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
    UNIQUE KEY `uq_order_id_isbn` (`order_id`, `isbn`),    
    KEY `idx_order_lines_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
    