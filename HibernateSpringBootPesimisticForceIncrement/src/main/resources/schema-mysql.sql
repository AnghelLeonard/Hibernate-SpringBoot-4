-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `chapter`
CREATE TABLE `chapter` (
  `id`      BIGINT        NOT NULL AUTO_INCREMENT,
  `version` SMALLINT      NOT NULL,
  `content` VARCHAR(255)  DEFAULT NULL,
  `title`   VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `modification`
CREATE TABLE `modification` (
  `id`            BIGINT        NOT NULL AUTO_INCREMENT,
  `chapter_id`    BIGINT        NOT NULL,  
  `description`   VARCHAR(255)  DEFAULT NULL,
  `modification`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (`id`),
  CONSTRAINT `modification_chapter_fk` FOREIGN KEY (`chapter_id`) REFERENCES `chapter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;