-- insert authors
INSERT INTO "author" ("age", "name", "genre", "id") VALUES 
  (34, 'Joana Nimar', 'History', 1)
ON CONFLICT ("id") DO NOTHING;

-- insert books
INSERT INTO "book" ("isbn", "title", "discontinued", "author_id", "id") VALUES 
  ('001-JN', 'A History of Ancient Prague', FALSE, 1, 1),
  ('002-JN', 'A People''s History', TRUE, 1, 2),
  ('003-JN', 'Ancients', TRUE, 1, 3),
  ('004-JN', 'History of the World', FALSE, 1, 4)
ON CONFLICT ("id") DO NOTHING;


