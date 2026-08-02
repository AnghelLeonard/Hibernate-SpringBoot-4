-- Recreate database
USE `bookstoredb`;
DROP DATABASE IF EXISTS `bookstoredb`;
CREATE DATABASE `bookstoredb`;
USE `bookstoredb`;

-- Table `author`
CREATE TABLE `author` (
  `id`    BIGINT        NOT NULL AUTO_INCREMENT,
  `age`   INT(11)       NOT NULL,
  `genre` VARCHAR(255)  DEFAULT NULL,
  `name`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `book`
CREATE TABLE `book` (
  `id`          BIGINT        NOT NULL,
  `author_id`   BIGINT        NOT NULL,
  `isbn`        VARCHAR(255)  DEFAULT NULL,
  `title`       VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (`id`),
  CONSTRAINT `book_author_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `ebook`
CREATE TABLE `ebook` (  
  `id`        BIGINT      NOT NULL,
  `author_id` BIGINT        NOT NULL,
  `format`    VARCHAR(255)  DEFAULT NULL,  
  `isbn`      VARCHAR(255)  DEFAULT NULL,  
  `title`     VARCHAR(255)  DEFAULT NULL,  
  CONSTRAINT `author_pk` PRIMARY KEY (`id`),
  CONSTRAINT `ebook_author_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `paperback`
CREATE TABLE `paperback` (  
  `id`          BIGINT         NOT NULL,
  `author_id`   BIGINT        NOT NULL,
  `isbn`        VARCHAR(255)  DEFAULT NULL,  
  `size_in`     VARCHAR(255)  DEFAULT NULL,  
  `title`       VARCHAR(255)  DEFAULT NULL,  
  `weight_lbs`  VARCHAR(255)  DEFAULT NULL,  
  CONSTRAINT `paperback_pk` PRIMARY KEY (`id`),
  CONSTRAINT `paperback_author_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `hibernate_sequences`
CREATE TABLE `hibernate_sequences` (  
  `next_val`       BIGINT,
  `sequence_name`  VARCHAR(255)  NOT NULL,  
  CONSTRAINT `hibernate_sequences_pk` PRIMARY KEY (`sequence_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 