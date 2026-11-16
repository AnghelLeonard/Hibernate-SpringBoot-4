-- insert authors
INSERT INTO "author" ("age", "name", "genre", "id", "created_at") VALUES 
  (23, 'Mark Janel', 'Anthology', 1, '2026-10-12T08:15:30Z'),
  (43, 'Olivia Goy', 'Horror', 2, '2026-10-15T14:22:10Z'),
  (51, 'Quartis Young', 'Anthology', 3, '2026-10-20T19:05:45Z'),
  (34, 'Joana Nimar', 'History', 4, '2026-11-01T09:40:00Z'),
  (41, 'Pyla Lou', 'Anthology', 5, '2026-11-08T16:12:55Z'),
  (31, 'Merci Umaal', 'Anthology', 6, '2026-11-16T06:58:00Z')
ON CONFLICT ("id") DO NOTHING;

-- insert books
INSERT INTO "book" ("isbn", "title", "author_id", "id") VALUES 
  ('001-JN', 'A History of Ancient Prague', 4, 1),
  ('002-JN', 'A People''s History', 4, 2),
  ('001-MJ', 'The Beatles Anthology', 1, 3),
  ('001-OG', 'Carrie', 2, 4),
  ('001-QY', 'Anthology Of An Year', 3, 5),
  ('001-PL', 'Personal Anthology', 5, 6),
  ('002-PL', 'Anthology of 2021', 5, 7),
  ('003-PL', 'Anthology of 2022', 5, 8),
  ('004-PL', 'Anthology of 2023', 5, 9),
  ('005-PL', 'Anthology of 2024', 5, 10),
  ('006-PL', 'Anthology of 2025', 5, 11),
  ('001-MU', 'Ultimate Anthology', 6, 12),
  ('002-MJ', 'Anthology From Zero To Expert', 1, 13),
  ('003-MJ', 'Quick Anthology', 1, 14),
  ('002-MU', '1959 Anthology', 6, 15)
ON CONFLICT ("id") DO NOTHING;
