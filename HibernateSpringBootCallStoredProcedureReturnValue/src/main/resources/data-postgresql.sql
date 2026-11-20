-- insert authors
INSERT INTO "author" ("age", "name", "genre", "id") VALUES 
  (23, 'Mark Janel', 'Anthology', 1), 
  (43, 'Olivia Goy', 'Horror', 2), 
  (51, 'Quartis Young', 'Anthology', 3), 
  (34, 'Joana Nimar', 'History', 4), 
  (38, 'Alicia Tom', 'Anthology', 5), 
  (56, 'Katy Loin', 'Anthology', 6) ^;

DROP PROCEDURE IF EXISTS count_author_by_genre ^; 

CREATE OR REPLACE PROCEDURE count_author_by_genre(
    IN p_genre CHAR(20),
    OUT p_count INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    SELECT COUNT(*) INTO p_count
    FROM author
    WHERE genre = p_genre;
END;
$$ ^; 

CREATE OR REPLACE FUNCTION count_author_by_genre_func(
    p_genre VARCHAR(20)
)
RETURNS INT
LANGUAGE plpgsql
AS $$
DECLARE
    v_count INT;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM author
    WHERE genre = p_genre;

    RETURN v_count;
END;
$$ ^;
