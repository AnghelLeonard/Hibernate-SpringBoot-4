-- insert books
INSERT INTO `book` (`price`, `title`, `id`) VALUES 
  (23, "Anthology of a day", 1),
  (35, "Carrie", 2),
  (28, "Facts and words", 3),
  (20, "Late today", 4),
  (43, "Mystery", 5)
ON DUPLICATE KEY UPDATE `id`=`id`;
