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
  `id`         BIGINT        NOT NULL AUTO_INCREMENT,
  `author_id`  BIGINT        NOT NULL,
  `isbn`       VARCHAR(255)  DEFAULT NULL,
  `title`      VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `book_pk` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `author_audit`
CREATE TABLE `author_audit` (
  `id`      BIGINT        NOT NULL,  
  `rev`     INTEGER       NOT NULL,
  `revend`  INTEGER       DEFAULT NULL,
  `revtype` TINYINT       DEFAULT NULL,
  `age`     INTEGER       DEFAULT NULL,
  `genre`   VARCHAR(255)  DEFAULT NULL,
  `name`    VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_audit_pk` PRIMARY KEY (`rev`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `book_audit`
CREATE TABLE `book_audit` (
  `id`         BIGINT        NOT NULL,
  `age`        INTEGER       DEFAULT NULL,
  `rev`        INTEGER       NOT NULL,
  `revend`     INTEGER       DEFAULT NULL,
  `revtype`    TINYINT       DEFAULT NULL,
  `author_id`  BIGINT        DEFAULT NULL,
  `isbn`       VARCHAR(255)  DEFAULT NULL,
  `title`      VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `book_audit_pk` PRIMARY KEY (`rev`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table `revinfo`
CREATE TABLE `revinfo` (  
  `rev`        INTEGER       NOT NULL AUTO_INCREMENT,
  `revtstmp`   BIGINT       DEFAULT NULL, 
  CONSTRAINT `revinfo_pk` PRIMARY KEY (`rev`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `book` 
  ADD CONSTRAINT `author_id_book_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`);

ALTER TABLE `author_audit` 
  ADD CONSTRAINT `rev_author_audit_fk1` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`);

ALTER TABLE `author_audit` 
  ADD CONSTRAINT `rev_author_audit_fk2` FOREIGN KEY (`revend`) REFERENCES `revinfo` (`rev`);

ALTER TABLE `book_audit` 
  ADD CONSTRAINT `rev_book_audit_fk1` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`rev`);

ALTER TABLE `book_audit` 
  ADD CONSTRAINT `rev_book_audit_fk2` FOREIGN KEY (`revend`) REFERENCES `revinfo` (`rev`);