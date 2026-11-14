-- Table `book`
CREATE TABLE `book` (
  `id`        BIGINT        NOT NULL AUTO_INCREMENT,  
  `author_id` BIGINT        NOT NULL,
  `title`     VARCHAR(255)  DEFAULT NULL,
  `isbn`      VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `book_pk` PRIMARY KEY (id),
  CONSTRAINT `book_author_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;