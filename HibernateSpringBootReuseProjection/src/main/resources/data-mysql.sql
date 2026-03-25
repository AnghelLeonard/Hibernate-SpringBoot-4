-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `email`, `address`, `rating`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", "markj@gmail.com", "mark's address", 99, 1),
  (43, "Olivia Goy", "Horror", "oliviag@gmail.com", "olivia's address", 89, 2),
  (51, "Quartis Young", "Anthology", "young@gmail.com", "quartis's address", 84, 3),
  (34, "Joana Nimar", "History", "jn@gmail.com", "joana's address", 95, 4),
  (33, "Marin Kyrab", "History", "marin@gmail.com", "marin's address", 82, 5)
ON DUPLICATE KEY UPDATE `id`=`id`;

