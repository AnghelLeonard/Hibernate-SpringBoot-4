-- Recreate database
USE `bookstoredb` ^;
DROP DATABASE IF EXISTS `bookstoredb` ^;
CREATE DATABASE `bookstoredb` ^;
USE `bookstoredb` ^;

-- Table `book`
CREATE TABLE `book` (
  `id`        BIGINT        NOT NULL AUTO_INCREMENT,  
  `price`     INT           NOT NULL,
  `title`     VARCHAR(255)  DEFAULT NULL,
  `isbn`      VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `book_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ^;

CREATE FUNCTION apply_discount(original_price INT, discount_percentage INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE final_price INT;
    
    IF original_price <= 0 THEN
        RETURN 0;
    ELSEIF discount_percentage <= 0 THEN
        RETURN original_price;
    ELSEIF discount_percentage >= 100 THEN
        RETURN 0;
    END IF;
        
    SET final_price = original_price * (1 - (discount_percentage / 100));
       
    RETURN final_price;
END ^;

CREATE FUNCTION slugify(input_text VARCHAR(255)) 
RETURNS VARCHAR(255)
DETERMINISTIC
BEGIN
    DECLARE slug VARCHAR(255);
        
    SET slug = LOWER(input_text);        
    SET slug = REPLACE(slug, '&', 'and');        
    SET slug = REGEXP_REPLACE(slug, '[^a-z0-9]+', '-');    
    SET slug = TRIM(BOTH '-' FROM slug);
    
    RETURN slug;
END ^;
