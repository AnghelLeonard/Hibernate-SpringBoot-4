-- insert books
INSERT INTO `book` (`isbn`, `title`, `price`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 41, 1),
  ("002-JN", "A People's History", 42, 2),
  ("001-MJ", "The Beatles Anthology", 12, 3),
  ("001-OG", "Carrie", 23, 4),
  ("002-OG", "Horror Day", 43, 5)
ON DUPLICATE KEY UPDATE `id`=`id`;
