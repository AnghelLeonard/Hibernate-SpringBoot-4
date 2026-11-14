-- Table `author`
CREATE TABLE IF NOT EXISTS `author` (
  `id`    BIGINT        NOT NULL,
  `age`   INT(11)       NOT NULL,
  `genre` VARCHAR(255)  DEFAULT NULL,
  `name`  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT `author_pk` PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;