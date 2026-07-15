-- insert books
INSERT INTO `book` (`id`, `title`, `genre`, `isbn`, `author`, `price`) VALUES 
  (1, "Carrie", "Horror", "001-OG", "Olivia Goy", 23),
  (2, "Old One", "History", "001-JN", "Joana Nimar", 40),
  (3, "Best Shot", "Anthology", "001-MJ", "Mark Janel", 30), 
  (4, "Happy End", "Horror", "002-OG", "Olivia Goy", 36),
  (5, "Long Run", "Anthology", "002-MJ", "Mark Janel", 10)  
ON DUPLICATE KEY UPDATE `id`=`id`;