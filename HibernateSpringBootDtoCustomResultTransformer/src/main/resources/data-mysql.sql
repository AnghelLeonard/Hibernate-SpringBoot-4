-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3),
  (34, "Joana Nimar", "History", 4) 
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`isbn`, `title`, `author_id`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 4, 1),
  ("002-JN", "A People's History", 4, 2),
  ("001-MJ", "The Beatles Anthology", 1, 3),
  ("001-OG", "Carrie", 2, 4),
  ("003-JN", "History Tody", 4, 5),
  ("002-OG", "Horror Train", 2, 6),
  ("002-MJ", "Anthology Of An Year", 1, 7),
  ("003-MJ", "Anthology From A to Z", 1, 8),
  ("004-MJ", "Past Anthology", 1, 9)
ON DUPLICATE KEY UPDATE `id`=`id`;