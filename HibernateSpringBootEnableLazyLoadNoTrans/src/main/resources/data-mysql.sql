-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3),
  (34, "Joana Nimar", "History", 4) 
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`isbn`, `price`, `title`, `author_id`, `id`) VALUES 
  ("001-JN", 25, "A History of Ancient Prague", 4, 1),
  ("002-JN", 30, "A People's History", 4, 2),
  ("001-MJ", 31, "The Beatles Anthology", 1, 3),
  ("001-OG", 35, "Carrie", 2, 4)
ON DUPLICATE KEY UPDATE `id`=`id`;