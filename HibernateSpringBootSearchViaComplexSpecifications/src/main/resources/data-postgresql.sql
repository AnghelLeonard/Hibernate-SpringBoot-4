-- Insert authors (IDs 1 to 8)
INSERT INTO "author" ("id", "age", "name", "genre", "rating") VALUES
  (1, 23, 'Mark Janel',     'Anthology', 5),
  (2, 43, 'Olivia Goy',     'Horror',    4),
  (3, 51, 'Quartis Young',  'Anthology', 5),
  (4, 34, 'Joana Nimar',    'History',   4),
  (5, 44, 'Kym Less',       'Anthology', 3),
  (6, 29, 'Elena Rostova',  'Sci-Fi',    5),
  (7, 62, 'Arthur Pendelton','History',  2),
  (8, 38, 'Marcus Vance',   NULL,        4) -- NULL genre to test IS_NULL / IS_NOT_NULL
ON CONFLICT ("id") DO NOTHING;

-- Insert books (IDs 1 to 14)
INSERT INTO "book" ("id", "name", "isbn", "title", "price", "author_id") VALUES
  -- Joana Nimar (id = 4, History, age 34, rating 4)
  (1,  'Joana Nimar',      '001-JN', 'A History of Ancient Prague', 52, 4),
  (2,  'Joana Nimar',      '002-JN', 'A People''s History',         43, 4),
  (3,  'Joana Nimar',      '003-JN', 'Empires of the Danube',       35, 4),

  -- Olivia Goy (id = 2, Horror, age 43, rating 4)
  (4,  'Olivia Goy',       '001-OG', 'Carrie',                      62, 2),
  (5,  'Olivia Goy',       '002-OG', 'Midnight in the Fog',         28, 2),

  -- Quartis Young (id = 3, Anthology, age 51, rating 5)
  (6,  'Quartis Young',    '001-QY', 'War Anthology',               52, 3),
  (7,  'Quartis Young',    '002-QY', 'Voices of the Century',       75, 3),

  -- Mark Janel (id = 1, Anthology, age 23, rating 5)
  (8,  'Mark Janel',       '001-MJ', '100 Days',                    40, 1),
  (9,  'Mark Janel',       '002-MJ', 'Short Tales of Winter',       19, 1),

  -- Kym Less (id = 5, Anthology, age 44, rating 3)
  (10, 'Kym Less',         '001-KL', 'Modern Anthology',            26, 5),

  -- Elena Rostova (id = 6, Sci-Fi, age 29, rating 5)
  (11, 'Elena Rostova',    '001-ER', 'Orbital Drift',               48, 6),
  (12, 'Elena Rostova',    '002-ER', 'The Quantum Archive',         65, 6),

  -- Arthur Pendelton (id = 7, History, age 62, rating 2)
  (13, 'Arthur Pendelton', '001-AP', 'Chronicles of Rome',          30, 7),

  -- Unassigned book (NULL author_id to test IS_NULL on joins/fields)
  (14, 'Unknown',          '001-UN', 'Lost Manuscript',             15, NULL)
ON CONFLICT ("id") DO NOTHING;