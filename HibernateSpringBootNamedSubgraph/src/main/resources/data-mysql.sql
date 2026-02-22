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

-- insert pubishers
INSERT INTO `publisher` (`company`, `id`) VALUES
  ("AnthologyPublisher", 1),
  ("HorrorPublisher", 2),
  ("HistoryPublisher", 3)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`isbn`, `title`, `author_id`, `publisher_id`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 4, 3, 1),
  ("002-JN", "A People's History", 4, 3, 2),
  ("003-JN", "History Day", 4, 3, 3),
  ("001-MJ", "The Beatles Anthology", 1, 1, 4),
  ("001-OG", "Carrie", 2, 2, 5),
  ("002-OG", "House Of Pain", 2, 2, 6),
  ("001-AT", "Anthology 2000", 5, 1, 7)
ON DUPLICATE KEY UPDATE `id`=`id`;
