-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3),
  (34, "Joana Nimar", "History", 4),
  (37, "Larisa Tomay", "History", 5)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`isbn`, `title`, `price`, `author_id`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 34, 4, 1),
  ("002-JN", "A People's History", 44, 4, 2),
  ("003-JN", "World History", 74, 4, 3),
  ("004-JN", "One of a kind", 14, 4, 4),
  ("005-JN", "History 2000+", 73, 4, 5),
  ("001-MJ", "The Beatles Anthology", 30, 1, 6),
  ("002-MJ", "Cool Anthology", 25, 1, 7),
  ("001-OG", "Carrie", 33, 2, 8),
  ("001-LT", "Ghost Soldiers", 4, 5, 9),
  ("002-LT", "The Guns of August", 29, 5, 10),
  ("003-LT", "The Diary of a Young Girl", 59, 5, 11),
  ("004-LT", "The Devil in the White City", 69, 5, 12),
  ("005-LT", "Night", 19, 5, 13),
  ("006-LT", "The Histories", 56, 5, 14)
ON DUPLICATE KEY UPDATE `id`=`id`;
