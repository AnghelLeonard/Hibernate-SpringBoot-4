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
INSERT INTO `book` (`isbn`, `title`, `genre`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", "History", 1),
  ("002-JN", "A People's History", "History", 2),
  ("001-MJ", "The Beatles Anthology", "Anthology", 3),
  ("001-OG", "Carrie", "Horror", 4)
ON DUPLICATE KEY UPDATE `id`=`id`;
