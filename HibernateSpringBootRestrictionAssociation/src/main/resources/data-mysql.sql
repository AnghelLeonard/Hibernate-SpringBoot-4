-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (34, "Joana Nimar", "History", 1)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`isbn`, `title`, `price`, `author_id`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 25, 1, 1),
  ("002-JN", "A People's History", 15, 1, 2),
  ("003-JN", "Ancients", 19, 1, 3),
  ("004-JN", "History of the World", 24, 1, 4)
ON DUPLICATE KEY UPDATE `id`=`id`;
