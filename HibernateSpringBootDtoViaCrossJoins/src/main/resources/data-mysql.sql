-- insert books
INSERT INTO `book` (`isbn`, `title`, `id`) VALUES 
  ("001-JN", "A History of Ancient Prague", 1),
  ("002-JN", "A People's History", 2),
  ("001-OG", "Carrie", 3)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert formats
INSERT INTO `format` (`format_type`, `id`) VALUES 
  ("paperback", 1),
  ("kindle", 2),
  ("pdf", 3)
ON DUPLICATE KEY UPDATE `id`=`id`;