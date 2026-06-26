-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3),
  (34, "Joana Nimar", "History", 4),
  (41, "Kyla Lou", "Anthology", 5),
  (31, "Merci Umaal", "Anthology", 6)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`isbn`, `title`, `author_id`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 4, 1),
  ("002-JN", "A People's History", 4, 2),
  ("001-MJ", "The Beatles Anthology", 1, 3),
  ("001-OG", "Carrie", 2, 4),
  ("001-QY", "Anthology Of An Year", 3, 5),
  ("001-KL", "Personal Anthology", 5, 6),
  ("001-MU", "Ultimate Anthology", 6, 7),
  ("002-MJ", "Old Anthology", 1, 8),
  ("003-MJ", "New Anthology", 1, 9)
ON DUPLICATE KEY UPDATE `id`=`id`;