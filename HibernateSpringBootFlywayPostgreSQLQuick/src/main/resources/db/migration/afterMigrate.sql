DELETE FROM book;
DELETE FROM author;

-- insert authors
INSERT INTO author (age, name, genre) VALUES 
  (23, 'Mark Janel', 'Anthology'),
  (43, 'Olivia Goy', 'Horror'),
  (51, 'Quartis Young', 'Anthology'),
  (34, 'Joana Nimar', 'History');

-- insert books
INSERT INTO book (isbn, title, author_id) VALUES 
  ('001-JN', 'A History of Ancient Prague', 4),
  ('002-JN', 'A People''s History', 4),
  ('001-MJ', 'The Beatles Anthology', 1),
  ('001-OG', 'Carrie', 2);

