USE `bookstoredb` ;

-- -----------------------------------------------------
-- Table `bookstoredb`.`author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstoredb`.`author` (
  `age` INT NOT NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `genre` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookstoredb`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstoredb`.`tag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tag` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookstoredb`.`author_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstoredb`.`author_tag` (
  `author_id` BIGINT NOT NULL,
  `tag_id` BIGINT NOT NULL,
  PRIMARY KEY (`author_id`, `tag_id`),
  INDEX `FK7u4elvrjbqr9ydtmggje64sva` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `FK2o47m42wm7uslsmd9kfk2qu2a`
    FOREIGN KEY (`author_id`)
    REFERENCES `bookstoredb`.`author` (`id`),
  CONSTRAINT `FK7u4elvrjbqr9ydtmggje64sva`
    FOREIGN KEY (`tag_id`)
    REFERENCES `bookstoredb`.`tag` (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookstoredb`.`publisher`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstoredb`.`publisher` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `address` VARCHAR(255) NULL DEFAULT NULL,
  `company` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookstoredb`.`book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstoredb`.`book` (
  `author_id` BIGINT NULL DEFAULT NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `publisher_id` BIGINT NULL DEFAULT NULL,
  `isbn` VARCHAR(255) NULL DEFAULT NULL,
  `title` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKklnrv3weler2ftkweewlky958` (`author_id` ASC) VISIBLE,
  INDEX `FKgtvt7p649s4x80y6f4842pnfq` (`publisher_id` ASC) VISIBLE,
  CONSTRAINT `FKgtvt7p649s4x80y6f4842pnfq`
    FOREIGN KEY (`publisher_id`)
    REFERENCES `bookstoredb`.`publisher` (`id`),
  CONSTRAINT `FKklnrv3weler2ftkweewlky958`
    FOREIGN KEY (`author_id`)
    REFERENCES `bookstoredb`.`author` (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookstoredb`.`review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstoredb`.`review` (
  `book_id` BIGINT NULL DEFAULT NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `language` VARCHAR(255) NULL DEFAULT NULL,
  `script` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK70yrt09r4r54tcgkrwbeqenbs` (`book_id` ASC) VISIBLE,
  CONSTRAINT `FK70yrt09r4r54tcgkrwbeqenbs`
    FOREIGN KEY (`book_id`)
    REFERENCES `bookstoredb`.`book` (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookstoredb`.`reviewer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstoredb`.`reviewer` (
  `reviewer_age` INT NOT NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `reviewer_name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bookstoredb`.`review_reviewer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bookstoredb`.`review_reviewer` (
  `review_id` BIGINT NOT NULL,
  `reviewer_id` BIGINT NOT NULL,
  PRIMARY KEY (`review_id`, `reviewer_id`),
  INDEX `FKpuvpkaucyj6pmrmi2f6xs2qxs` (`reviewer_id` ASC) VISIBLE,
  CONSTRAINT `FKfmsm54dlkff3gplylr3ujjuy1`
    FOREIGN KEY (`review_id`)
    REFERENCES `bookstoredb`.`review` (`id`),
  CONSTRAINT `FKpuvpkaucyj6pmrmi2f6xs2qxs`
    FOREIGN KEY (`reviewer_id`)
    REFERENCES `bookstoredb`.`reviewer` (`id`))
ENGINE = InnoDB;