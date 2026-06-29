-- insert publishers
INSERT INTO `publisher` (`company`, `id`) VALUES 
  ("AnthologyPublisher", 1),
  ("HorrorPublisher", 2),
  ("HistoryPublisher", 3);

-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `publisher_id`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1, 1),
  (43, "Olivia Goy", "Horror", 2, 2),
  (51, "Quartis Young", "Anthology", 1, 3),
  (34, "Joana Nimar", "History", 3, 4),
  (38, "Alicia Tom", "Anthology", 1, 5),
  (56, "Katy Loin", "Horror", 1, 6),
  (23, "Wuth Troll", "Anthology", 1, 7);

-- insert books
INSERT INTO `book` (`isbn`, `title`, `price`, `author_id`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 34, 4, 1),
  ("002-JN", "A People's History", 36, 4, 2),
  ("003-JN", "History Day", 25, 4, 3),
  ("001-MJ", "The Beatles Anthology", 20, 1, 4),
  ("001-OG", "Carrie", 44, 2, 5),
  ("001-KL", "House Of Pain", 41, 6, 6),
  ("001-AT", "Anthology 2000", 16, 5, 7);
