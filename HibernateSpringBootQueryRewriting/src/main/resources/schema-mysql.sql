-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `book`
CREATE TABLE `book` (
    `id`        BIGINT       NOT NULL AUTO_INCREMENT,
    `title`     VARCHAR(255) NOT NULL,
    `content`   TEXT         NOT NULL,
     CONSTRAINT `book_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- The FULLTEXT index used by MATCH...AGAINST
CREATE FULLTEXT INDEX idx_book_fulltext ON book(title, content);