-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3),
  (34, "Joana Nimar", "History", 4),
  (38, "Alicia Tom", "Anthology", 5),
  (56, "Katy Loin", "Anthology", 6)
ON DUPLICATE KEY UPDATE `id`=`id`;
