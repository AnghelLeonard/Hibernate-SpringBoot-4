-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3),
  (34, "Joana Nimar", "History", 4)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`isbn`, `title`, `rank`, `author_id`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 1, 4, 1),
  ("002-JN", "A People's History", 5, 4, 2),
  ("003-JN", "History Now", 6, 4, 3),
  ("001-MJ", "The Beatles Anthology", 2, 1, 4),
  ("001-OG", "Carrie", 3, 2, 5),
 ("002-OG", "Nightmare Of A Day", 4, 2, 6)
ON DUPLICATE KEY UPDATE `id`=`id`;
