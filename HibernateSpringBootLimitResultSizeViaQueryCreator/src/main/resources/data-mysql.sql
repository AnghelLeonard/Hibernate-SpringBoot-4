-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
(56, "Mark Janel", "Anthology", 1),
(43, "Olivia Goy", "Horror", 2),
(51, "Quartis Young", "Anthology", 3),
(34, "Joana Nimar", "History", 4),
(38, "Alicia Tom", "Anthology", 5),
(56, "Anatoly Quentin", "Anthology", 6),
(56, "Katy Loin", "Anthology", 7),
(30, "Ulm Nair", "Horror", 8),
(37, "Ilan Tastenir", "Horror", 9),
(57, "Biar Gul", "History", 10),
(56, "Rona Fullos", "History", 11),
(56, "Joana Vaser", "Anthology", 12),
(44, "Vitali Hill", "History", 13),
(56, "Carmen Corra", "Horror", 14),
(56, "Dalia Rene", "Anthology", 15)
ON DUPLICATE KEY UPDATE `id`=`id`;
