-- clear existing data and reset identity sequences
TRUNCATE TABLE "book", "author" RESTART IDENTITY;

-- Insert authors
INSERT INTO "author" ("id", "age", "name", "genre") VALUES 
    (1, 23, 'Mark Janel', 'Anthology'),
    (2, 43, 'Olivia Goy', 'Horror'),
    (3, 51, 'Quartis Young', 'Anthology'),
    (4, 34, 'Joana Nimar', 'History');

-- Insert books
INSERT INTO "book" ("id", "isbn", "title", "author_id") VALUES 
    (1, '001-JN', 'A History of Ancient Prague', 4),
    (2, '002-JN', 'A People''s History', 4),
    (3, '001-MJ', 'The Beatles Anthology', 1),
    (4, '001-OG', 'Carrie', 2);


