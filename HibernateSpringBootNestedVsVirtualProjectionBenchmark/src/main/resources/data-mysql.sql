-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3),
  (34, "Joana Nimar", "History", 4),
  (21, "Leopard Talay", "History", 5),
  (56, "Viorel Goy", "Horror", 6),
  (41, "Marcia Del Karla", "Anthology", 7),
  (37, "Follow Ginger", "Horror", 8),
  (39, "Baib Val", "Anthology", 9),
  (49, "Kelly Ulm", "History", 10)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`isbn`, `title`, `author_id`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 4, 1),
  ("002-JN", "A People's History", 4, 2),
  ("003-JN", "History Now", 4, 3),
  ("001-MJ", "The Beatles Anthology", 1, 4),
  ("001-OG", "Carrie", 2, 5),
  ("002-OG", "Nightmare Of A Day", 2, 6),
  ("003-OG", "Under the Shadow", 2, 7),
  ("001-QY", "Wild Tales", 3, 8),
  ("002-QY", "Kaos", 3, 9),
  ("001-LT", "Hamnet", 5, 10),
  ("002-LT", "Border 2", 5, 11),
  ("003-LT", "The Odyssey", 5, 12),
  ("001-VG", "Get Out", 6, 13),
  ("001-VG", "The Babadook", 6, 14),
  ("001-MDK", "Amores Perros", 7, 15),
  ("002-MDK", "Solo", 7, 16),
  ("003-MDK", "Magnolia", 7, 17),
  ("004-MDK", "Traffic", 7, 18),
  ("001-FG", "Let the Right One In", 8, 19),
  ("002-FG", "Host", 8, 20),
  ("003-FG", "The Loved Ones", 8, 21),
  ("001-BV", "Before the Rain", 9, 22),
  ("002-BV", "Grams", 9, 23),
  ("003-BV", "Night on Earth", 9, 24),
  ("001-KU", "Roofman", 10, 25),
  ("002-KU", "Song Sung Blue", 10, 26),
  ("003-KU", "Oppenheimer", 10, 27),
  ("004-KU", "Blue Moon", 10, 28),
  ("005-KU", "The Testament of Ann Lee", 10, 29)
ON DUPLICATE KEY UPDATE `id`=`id`;
