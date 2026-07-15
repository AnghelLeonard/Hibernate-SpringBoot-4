-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `royalties`, `sellrank`, `rating`, `id`)
  VALUES (23, "Mark Janel", "Anthology", 1200, 289, 3, 1)
ON DUPLICATE KEY UPDATE `id`=`id`;