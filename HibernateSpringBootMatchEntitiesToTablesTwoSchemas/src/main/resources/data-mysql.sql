-- insert authors
USE `authorsdb`;
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
USE `booksdb`;
INSERT INTO `book` (`isbn`, `title`, `id`) VALUES 
  ("001-MJ", "The Beatles Anthology", 1)
ON DUPLICATE KEY UPDATE `id`=`id`;