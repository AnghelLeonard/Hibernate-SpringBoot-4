-- Recreate database
DROP DATABASE IF EXISTS `bookstoredb` ^;
CREATE DATABASE `bookstoredb` ^;
USE `bookstoredb` ^;

-- Table `author`
CREATE TABLE `author` (
  `id`    BIGINT        NOT NULL AUTO_INCREMENT,
  `age`   INT(11)       NOT NULL,
  `genre` VARCHAR(255)  DEFAULT NULL,
  `name`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ^;

-- Table `book`
CREATE TABLE `book` (
  `id`        BIGINT         NOT NULL AUTO_INCREMENT,  
  `author_id` BIGINT,
  `dtype`     TINYINT(1)     NOT NULL CHECK (`dtype` in (1,2,3)),
  `format`    VARCHAR(255),
  `isbn`      VARCHAR(255),
  `size_in`   VARCHAR(255), 
  `title`     VARCHAR(255),
  `weight_lbs` VARCHAR(255),
  CONSTRAINT `book_pk` PRIMARY KEY (id),
  CONSTRAINT `book_author_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ^;

DROP TRIGGER IF EXISTS ebook_format_trigger ^; 
DROP TRIGGER IF EXISTS paperback_weight_trigger ^; 
DROP TRIGGER IF EXISTS paperback_size_trigger ^; 

CREATE TRIGGER ebook_format_trigger
    BEFORE INSERT ON book
        FOR EACH ROW 
            BEGIN 
                IF NEW.DTYPE = 2 THEN 
                    IF NEW.format IS NULL THEN  
                        SIGNAL SQLSTATE '45000'
                        SET MESSAGE_TEXT='The format of e-book cannot be null';
                    END IF;
                END IF;    
            END ^;

CREATE TRIGGER paperback_weight_trigger
    BEFORE INSERT ON book
        FOR EACH ROW 
            BEGIN 
                IF NEW.DTYPE = 3 THEN 
                    IF NEW.weight_lbs IS NULL THEN  
                        SIGNAL SQLSTATE '45000'
                        SET MESSAGE_TEXT='The weight of paperback cannot be null';
                    END IF;
                END IF;    
            END ^;

CREATE TRIGGER paperback_size_trigger
    BEFORE INSERT ON book
        FOR EACH ROW 
            BEGIN 
                IF NEW.DTYPE = 3 THEN 
                    IF NEW.size_in IS NULL THEN  
                        SIGNAL SQLSTATE '45000'
                        SET MESSAGE_TEXT='The size of paperback cannot be null';
                    END IF;
                END IF;    
            END ^;
			