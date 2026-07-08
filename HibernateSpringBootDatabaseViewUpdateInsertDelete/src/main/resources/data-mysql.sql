-- insert authors
INSERT INTO `author` (`age`, `name`, `genre`, `royalties`, `sellrank`, `rating`, `id`) VALUES 
  (23, "Mark Janel", "Anthology", 1200, 289, 3, 1),
  (43, "Olivia Goy", "Horror", 4000, 490, 5, 2),
  (51, "Quartis Young", "Anthology", 900, 122, 4, 3),
  (34, "Joana Nimar", "History", 5600, 554, 4, 4),
  (47, "Kakki Jou", "Anthology", 1000, 231, 5, 5),
  (56, "Fair Pouille", "Anthology", 3400, 344, 5, 6);

CREATE OR REPLACE VIEW AUTHOR_ANTHOLOGY_VIEW
AS
SELECT
    a.id,
    a.name,
    a.age,
    a.genre
FROM
    author a
WHERE a.genre = "Anthology";

-- use: ...  = "Anthology" WITH CHECK OPTION ^; for ensuring that the this operation is conformed with the definition of the view