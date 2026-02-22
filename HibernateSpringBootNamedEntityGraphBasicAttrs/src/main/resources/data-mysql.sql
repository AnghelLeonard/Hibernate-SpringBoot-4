-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3),
  (34, "Joana Nimar", "History", 4),
  (38, "Alicia Tom", "Anthology", 5),
  (56, "Katy Loin", "Anthology", 6),
  (23, "Wuth Troll", "Anthology", 7) 
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`isbn`, `title`, `author_id`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 4, 1),
  ("002-JN", "A People's History", 4, 2),
  ("003-JN", "History Day", 4, 3),
  ("001-MJ", "The Beatles Anthology", 1, 4),
  ("001-OG", "Carrie", 2, 5),
  ("002-OG", "House Of Pain", 2, 6),
  ("001-AT", "Anthology 2000", 5, 7),
  ("001-KL", "My Anthology", 6, 8),
  ("001-QY", "Money Anthology", 3, 9)
ON DUPLICATE KEY UPDATE `id`=`id`;
