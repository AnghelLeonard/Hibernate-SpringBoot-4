INSERT INTO `author` (`age`, `name`, `genre`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1),
  (43, "Olivia Goy", "Horror", 2),
  (51, "Quartis Young", "Anthology", 3), 
  (34, "Joana Nimar", "History", 4), 
  (38, "Alicia Tom", "Anthology", 5), 
  (56, "Katy Loin", "Anthology", 6) ^;

DROP PROCEDURE IF EXISTS FETCH_AUTHOR_BY_GENRE ^; 

CREATE DEFINER=root@localhost PROCEDURE FETCH_AUTHOR_BY_GENRE(IN p_genre CHAR(20))
BEGIN  
  SELECT * FROM AUTHOR WHERE GENRE = p_genre;    
END ^;

CREATE DEFINER=root@localhost PROCEDURE FETCH_AUTHOR_ID_AND_NAME_BY_GENRE(IN p_genre CHAR(20))
BEGIN  
  SELECT id,name FROM AUTHOR WHERE GENRE = p_genre;    
END ^;