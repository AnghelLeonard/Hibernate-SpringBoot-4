-- insert 10 authors
INSERT INTO "author" ("age", "name", "genre", "id") VALUES 
  (34, 'Joana Nimar', 'History', 1),
  (23, 'Mark Janel', 'Anthology', 2),
  (43, 'Olivia Goy', 'Horror', 3),
  (51, 'Quartis Young', 'Anthology', 4),
  (38, 'Alicia Tom', 'Sci-Fi', 5),
  (56, 'Katy Loin', 'Fantasy', 6),
  (29, 'Martin Leon', 'Mystery', 7),
  (47, 'Qart Pinkil', 'Biography', 8),
  (61, 'Leonard Vance', 'Philosophy', 9),
  (31, 'Elena Vancea', 'History', 10)
ON CONFLICT ("id") DO NOTHING;

-- insert 10 books (1-to-1 via shared primary key author_id)
INSERT INTO "book" ("author_id", "title", "isbn") VALUES 
  (1, 'A History of Ancient Prague', '001-JN'),
  (2, 'The Beatles Anthology', '001-MJ'),
  (3, 'House Of Pain', '001-OG'),
  (4, 'Modern Short Stories', '001-QY'),
  (5, 'Shadows of Andromeda', '001-AT'),
  (6, 'Chronicles of the Elderwood', '001-KL'),
  (7, 'The Midnight Express Clue', '001-ML'),
  (8, 'Life Behind the Curtain', '001-QP'),
  (9, 'Thoughts on Modern Logic', '001-LV'),
  (10, 'Empires of the Danube', '001-EV')
ON CONFLICT ("author_id") DO NOTHING;