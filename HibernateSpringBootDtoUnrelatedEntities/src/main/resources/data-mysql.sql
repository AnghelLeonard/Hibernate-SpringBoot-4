-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3),
  (34, "Joana Nimar", "History", 4),
  (44, "Kym Less", "Anthology", 5)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`name`, `isbn`, `title`, `price`, `id`) VALUES 
  ("Joana Nimar", "001-JN", "A History of Ancient Prague", 42, 1),
  ("Joana Nimar", "002-JN", "A People's History", 40, 2),
  ("Mark Janel", "001-MJ", "The Beatles Anthology", 42, 3),
  ("Olivia Goy", "001-OG", "Carrie", 42, 4),
  ("Quartis Young", "001-QY", "War Anthology", 42, 5),
  ("Mark Janel", "002-MJ", "100 Days", 40, 6),
  ("Kim Less", "001-KL", "Modern Anthology", 42, 7)
ON DUPLICATE KEY UPDATE `id`=`id`;
