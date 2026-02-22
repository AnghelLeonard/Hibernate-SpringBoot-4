-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `best_selling`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", "No", 1),
  (43, "Olivia Goy", "Horror", "No", 2),
  (51, "Quartis Young", "Anthology", "Yes", 3),
  (34, "Joana Nimar", "History", "Yes", 4),
  (38, "Alicia Tom", "Anthology", "No", 5),
  (56, "Katy Loin", "Anthology", "No", 6)
ON DUPLICATE KEY UPDATE `id`=`id`;
