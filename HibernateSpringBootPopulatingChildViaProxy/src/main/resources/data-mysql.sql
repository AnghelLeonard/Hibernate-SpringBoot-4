-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1) 
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert bookstore_clerks
INSERT INTO `bookstore_clerk` (`first_name`, `last_name`, `age`, `id`) VALUES 
 ('Andrew', 'Smith', 24, 1),
 ('Elena', 'Jones', 31, 2),
 ('Michael', 'Miller', 19, 3),
 ('Anna', 'Taylor', 28, 4),
 ('Robert', 'Brown', 45, 5);
