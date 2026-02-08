-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1) 
ON DUPLICATE KEY UPDATE `id`=`id`;