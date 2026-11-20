INSERT INTO author ("age", "name", "genre", "nickname", "id") VALUES 
  (23, 'Mark Janel', 'Anthology', 'MJ', 1),
  (43, 'Olivia Goy', 'Horror', 'OG', 2),
  (51, 'Quartis Young', 'Anthology', 'QY', 3), 
  (34, 'Joana Nimar', 'History', 'JN', 4), 
  (38, 'Alicia Tom', 'Anthology', 'AT', 5), 
  (56, 'Katy Loin', 'Anthology', 'KL', 6) ^;

DROP PROCEDURE IF EXISTS fetch_nickname_and_age_by_id ^; 
DROP PROCEDURE IF EXISTS fetch_nickname_and_age_by_id_func ^;
DROP PROCEDURE IF EXISTS fetch_nickname_and_age_by_genre ^;
DROP PROCEDURE IF EXISTS fetch_nickname_and_age_by_genre_func ^;  
DROP PROCEDURE IF EXISTS fetch_author_by_genre ^; 
DROP PROCEDURE IF EXISTS fetch_author_by_genre_func ^; 
DROP PROCEDURE IF EXISTS fetch_nickname_genre_and_age_by_genre ^; 
DROP PROCEDURE IF EXISTS fetch_nickname_genre_and_age_by_genre_table_func ^;
DROP PROCEDURE IF EXISTS fetch_nickname_genre_and_age_by_genre_two_cursors_func ^;

CREATE OR REPLACE PROCEDURE fetch_nickname_and_age_by_id(
    IN p_id INT,
    OUT o_nickname VARCHAR(20),
    OUT o_age INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    SELECT nickname, age
    INTO o_nickname, o_age
    FROM author
    WHERE id = p_id;
END;
$$ ^;

CREATE OR REPLACE FUNCTION fetch_nickname_and_age_by_id_func(
    IN p_id INT,
    OUT o_nickname VARCHAR(20),
    OUT o_age INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    SELECT nickname, age
    INTO o_nickname, o_age
    FROM author
    WHERE id = p_id;
END;
$$ ^;


CREATE OR REPLACE FUNCTION fetch_nickname_and_age_by_genre_func(
    p_genre VARCHAR(20)
)
RETURNS TABLE (
    nickname VARCHAR,
    age INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT a.nickname, a.age
    FROM author a
    WHERE a.genre = p_genre;
END;
$$ ^;

CREATE OR REPLACE PROCEDURE fetch_nickname_and_age_by_genre(
    IN p_genre VARCHAR(20),
    INOUT p_cursor REFCURSOR DEFAULT 'author_cursor'
)
LANGUAGE plpgsql
AS $$
BEGIN
    OPEN p_cursor FOR
    SELECT nickname, age
    FROM author
    WHERE genre = p_genre;
END;
$$ ^;

CREATE OR REPLACE PROCEDURE fetch_nickname_genre_and_age_by_genre(
    IN p_genre VARCHAR(20),
    INOUT p_cursor_1 REFCURSOR DEFAULT 'cursor_matching_genre',
    INOUT p_cursor_2 REFCURSOR DEFAULT 'cursor_other_genres'
)
LANGUAGE plpgsql
AS $$
BEGIN
    OPEN p_cursor_1 FOR
    SELECT nickname, age
    FROM author
    WHERE genre = p_genre;

    OPEN p_cursor_2 FOR
    SELECT genre, age
    FROM author
    WHERE genre <> p_genre;
END;
$$ ^;

CREATE OR REPLACE FUNCTION fetch_nickname_genre_and_age_by_genre_two_cursors_func(
    p_genre VARCHAR(20),
    p_cursor_1 REFCURSOR DEFAULT 'cursor_matching_genre',
    p_cursor_2 REFCURSOR DEFAULT 'cursor_other_genres'
)
RETURNS SETOF refcursor
LANGUAGE plpgsql
AS $$
BEGIN
    OPEN p_cursor_1 FOR
    SELECT nickname, age
    FROM author
    WHERE genre = p_genre;
    RETURN NEXT p_cursor_1;

    OPEN p_cursor_2 FOR
    SELECT genre, age
    FROM author
    WHERE genre <> p_genre;
    RETURN NEXT p_cursor_2;
END;
$$ ^;

CREATE OR REPLACE FUNCTION fetch_nickname_genre_and_age_by_genre_table_func(
    p_genre VARCHAR(20)
)
RETURNS TABLE (
    is_matching BOOLEAN,
    nickname VARCHAR,
    genre VARCHAR,
    age INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT TRUE, a.nickname, NULL::VARCHAR, a.age
    FROM author a
    WHERE a.genre = p_genre

    UNION ALL

    SELECT FALSE, NULL::VARCHAR, a.genre, a.age
    FROM author a
    WHERE a.genre <> p_genre;
END;
$$ ^;

CREATE OR REPLACE FUNCTION fetch_author_by_genre_func(
    p_genre VARCHAR(20)
)
RETURNS SETOF author
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT *
    FROM author
    WHERE genre = p_genre;
END;
$$ ^;

CREATE OR REPLACE PROCEDURE fetch_author_by_genre(
    IN p_genre VARCHAR(20),
    INOUT p_cursor REFCURSOR DEFAULT 'author_cursor'
)
LANGUAGE plpgsql
AS $$
BEGIN
    OPEN p_cursor FOR
    SELECT *
    FROM author
    WHERE genre = p_genre;
END;
$$ ^;

