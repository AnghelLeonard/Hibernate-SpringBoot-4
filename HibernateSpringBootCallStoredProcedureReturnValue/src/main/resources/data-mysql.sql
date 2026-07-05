INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3), 
  (34, "Joana Nimar", "History", 4), 
  (38, "Alicia Tom", "Anthology", 5), 
  (56, "Katy Loin", "Anthology", 6) ^;

DROP PROCEDURE IF EXISTS COUNT_AUTHOR_BY_GENRE ^; 
DROP PROCEDURE IF EXISTS COUNT_AUTHOR_BY_GT_AGE ^;

CREATE DEFINER=root@localhost PROCEDURE COUNT_AUTHOR_BY_GENRE(IN p_genre CHAR(20), OUT p_count INT)
BEGIN  
  SELECT COUNT(*) INTO p_count FROM author WHERE genre = p_genre;    
END ^; 

CREATE DEFINER=root@localhost PROCEDURE COUNT_AUTHOR_BY_GT_AGE(IN p_age INT, OUT p_count INT)
BEGIN  
  SELECT COUNT(*) INTO p_count FROM author WHERE age >= p_age;    
END ^;