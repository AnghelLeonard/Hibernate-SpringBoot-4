-- insert inventory
INSERT INTO `inventory` (`id`, `title`, `quantity`, `version`) VALUES 
  ("1", "A People's History", 10, 0)
ON DUPLICATE KEY UPDATE `id`=`id`;