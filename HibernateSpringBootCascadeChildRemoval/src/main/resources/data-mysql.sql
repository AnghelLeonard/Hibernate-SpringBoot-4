-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (34, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3),
  (34, "Joana Nimar", "History", 4)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`isbn`, `title`, `author_id`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 4, 1),
  ("002-JN", "A People's History", 4, 2),
  ("001-MJ", "The Beatles Anthology", 1, 3),
  ("003-JN", "Modern History", 4, 4),
  ("001-OG", "House Of Pain", 2, 5)
ON DUPLICATE KEY UPDATE `id`=`id`;
