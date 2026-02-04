-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (51, "Quartis Young", "Anthology", 2),
  (38, "Alicia Tom", "Anthology", 3),
  (56, "Katy Loin", "Anthology", 4),
  (38, "Martin Leon", "Anthology", 5),
  (56, "Qart Pinkil", "Anthology", 6)
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`isbn`, `title`, `id`) VALUES ("001-all", "Encyclopedia", 1);

-- insert in the junction table
INSERT INTO `author_book` (`author_id`, `book_id`) VALUES 
  (1, 1),
  (2, 1),
  (3, 1),
  (4, 1),
  (5, 1),
  (6, 1);