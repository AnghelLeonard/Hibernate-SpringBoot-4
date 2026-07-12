-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3),
  (34, "Joana Nimar", "History", 4),
  (38, "Alicia Tom", "Anthology", 5),
  (56, "Katy Loin", "Anthology", 6)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`isbn`, `title`, `author_id`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 4, 1),
  ("002-JN", "A People's History", 4, 2),
  ("001-MJ", "The Beatles Anthology", 1, 3),
  ("001-OG", "Carrie", 2, 4),
  ("001-AT", "Anthology of a Day", 5, 5),
  ("002-AT", "Anthology Myths", 5, 6),
  ("003-AT", "Wonders of the Anthology", 5, 7),
  ("001-KL", "Anthology 999", 6, 8),
  ("002-KL", "Extreme Anthology", 6, 9)
ON DUPLICATE KEY UPDATE `id`=`id`;
