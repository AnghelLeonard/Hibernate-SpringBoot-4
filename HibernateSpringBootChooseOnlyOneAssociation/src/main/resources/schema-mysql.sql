-- Recreate database
USE `bookstoredb` ^;
DROP TRIGGER IF EXISTS `Just_One_Of_Many` ^;
DROP DATABASE IF EXISTS `bookstoredb` ^; 
CREATE DATABASE `bookstoredb` ^;
USE `bookstoredb` ^;

-- Table `article`
CREATE TABLE `article` (
  `id`     BIGINT        NOT NULL AUTO_INCREMENT,  
  `title`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `article_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ^;

-- Table `magazine`
CREATE TABLE `magazine` (
  `id`     BIGINT        NOT NULL AUTO_INCREMENT,  
  `title`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `magazine_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ^;

-- Table `book`
CREATE TABLE `book` (
  `id`     BIGINT        NOT NULL AUTO_INCREMENT,  
  `title`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `book_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ^;

-- Table `review`
CREATE TABLE `review` (
  `id`          BIGINT NOT NULL AUTO_INCREMENT,  
  `content`     TEXT   DEFAULT NULL,
  `book_id`     BIGINT,
  `article_id`  BIGINT,
  `magazine_id` BIGINT,
  CONSTRAINT `book_pk` PRIMARY KEY (id),
  CONSTRAINT `book_review_fk` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `article_review_fk` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `magazine_review_fk` FOREIGN KEY (`magazine_id`) REFERENCES `magazine` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ^;

-- Trigger `Just_One_Of_Many`
CREATE TRIGGER `Just_One_Of_Many`
    BEFORE INSERT ON `review`
        FOR EACH ROW 
            BEGIN 
                IF (NEW.`article_id` IS NOT NULL AND NEW.`magazine_id` IS NOT NULL) OR 
                   (NEW.`article_id` IS NOT NULL AND NEW.`book_id` IS NOT NULL) OR
                     (NEW.`book_id` IS NOT NULL AND NEW.`magazine_id` IS NOT NULL) THEN                    
                        SIGNAL SQLSTATE '45000'
                        SET MESSAGE_TEXT='A review can be associated with either a book, a magazine or an article';                    
                END IF;    
            END ^;

