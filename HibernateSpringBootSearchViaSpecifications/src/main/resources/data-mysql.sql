-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `rating`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 5, 1),
  (43, "Olivia Goy", "Horror", 4, 2),
  (51, "Quartis Young", "Anthology", 5, 3),
  (34, "Joana Nimar", "History", 4, 4),
  (44, "Kym Less", "Anthology", 3, 5)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`name`, `isbn`, `title`, `price`, `id`) VALUES 
  ("Joana Nimar", "001-JN", "A History of Ancient Prague", 52, 1),
  ("Joana Nimar", "002-JN", "A People's History", 43, 2),
  ("Olivia Goy", "001-OG", "Carrie", 62, 4),
  ("Quartis Young", "001-QY", "War Anthology", 52, 5),
  ("Mark Janel", "002-MJ", "100 Days", 40, 6),
  ("Kim Less", "001-KL", "Modern Anthology", 26, 7)
ON DUPLICATE KEY UPDATE `id`=`id`;