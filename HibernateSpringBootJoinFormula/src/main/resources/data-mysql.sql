-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES
 (23, "Mark Janel", "Anthology", 1),
 (43, "Olivia Goy", "Horror", 2),
 (51, "Quartis Young", "Anthology", 3),
 (34, "Joana Nimar", "History", 4);

-- insert books
INSERT INTO `book` (`isbn`, `title`, `price`, `author_id`, `id`) VALUES
 ("001-JN", "A History of Ancient Prague", 23, 4, 1),
 ("002-JN", "A People's History", 34, 4, 2),
 ("001-MJ", "The Beatles Anthology", 55, 1, 3),
 ("002-MJ", "Anthology Of '99", 44, 1, 4),
 ("001-OG", "Carrie", 33, 2, 5),
 ("002-OG", "Last Day", 25, 2, 6),
 ("003-JN", "History Today", 41, 4, 7),
 ("003-MJ", "Anthology Of A Game", 21, 1, 8);
