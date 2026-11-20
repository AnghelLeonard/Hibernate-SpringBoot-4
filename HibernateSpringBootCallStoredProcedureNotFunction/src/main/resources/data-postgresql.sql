-- insert authors
INSERT INTO "author" ("age", "name", "genre", "id") VALUES
(23, 'Mark Janel', 'Anthology', 1),
(43, 'Olivia Goy', 'Horror', 2),
(51, 'Quartis Young', 'Anthology', 3),
(34, 'Joana Nimar', 'History', 4),
(25, 'Zoe Hernandez', 'Drama', 5),
(50, 'Sofia Ramirez', 'History', 6),
(33, 'Ryan Martinez', 'Philosophy', 7),
(25, 'Wyatt Smith', 'Horror', 8),
(25, 'Elizabeth Sanchez', 'Biography', 9),
(75, 'Emily Mendoza', 'Sci-Fi', 10),
(57, 'Riley Bailey', 'Adventure', 11),
(42, 'Mark Sanders', 'Drama', 12),
(57, 'Wyatt Flores', 'History', 13),
(65, 'Elizabeth Sanders', 'Horror', 14),
(34, 'Isabella Hall', 'Mystery', 15),
(55, 'Samuel Kelly', 'Adventure', 16),
(80, 'Emma Hughes', 'Biography', 17),
(32, 'Amelia Hall', 'Biography', 18),
(68, 'Camila Kim', 'Philosophy', 19),
(30, 'Alexander Gray', 'Anthology', 20),
(59, 'Joshua Sanchez', 'Horror', 21),
(70, 'Mila Rodriguez', 'Fantasy', 22),
(68, 'Jack Cox', 'History', 23),
(48, 'Luna Adams', 'Poetry', 24),
(31, 'Henry Bennett', 'Philosophy', 25),
(53, 'Zoe Moore', 'History', 26),
(56, 'Zoey Hall', 'Poetry', 27),
(50, 'Andrew Ortiz', 'Poetry', 28),
(29, 'Scarlett Patel', 'Sci-Fi', 29),
(73, 'Liam Torres', 'Fantasy', 30),
(62, 'William White', 'Sci-Fi', 31),
(72, 'Leah Reyes', 'Poetry', 32),
(55, 'Jack Anderson', 'History', 33),
(55, 'Ella Alvarez', 'Drama', 34),
(73, 'Julian Gomez', 'Mystery', 35),
(85, 'Jacob Wilson', 'Horror', 36),
(36, 'Asher Williams', 'History', 37),
(76, 'Jaxon Taylor', 'Philosophy', 38),
(70, 'William Rivera', 'Philosophy', 39),
(54, 'Zoey Murphy', 'Biography', 40),
(36, 'Olivia Wood', 'Poetry', 41),
(56, 'Levi Castillo', 'Adventure', 42),
(36, 'Lincoln Flores', 'Fantasy', 43),
(80, 'Layla Taylor', 'Anthology', 44),
(55, 'Caleb Ruiz', 'Biography', 45),
(35, 'Bella Jackson', 'Crime', 46),
(47, 'Jaxon Wright', 'History', 47),
(42, 'Luna Sanders', 'Biography', 48),
(22, 'Claire Murphy', 'Philosophy', 49),
(24, 'Scarlett Collins', 'Horror', 50),
(52, 'David Scott', 'Anthology', 51),
(32, 'Michael Morgan', 'Horror', 52),
(30, 'Audrey Collins', 'Adventure', 53),
(38, 'Levi Patel', 'History', 54),
(43, 'Joshua Cruz', 'Fantasy', 55),
(76, 'Hannah Kelly', 'Sci-Fi', 56),
(47, 'Lillian Castillo', 'Drama', 57),
(69, 'Aria Mitchell', 'Romance', 58),
(37, 'Grayson Turner', 'Sci-Fi', 59),
(65, 'Jacob Jones', 'Anthology', 60),
(51, 'Ellie Gutierrez', 'Philosophy', 61),
(31, 'Jacob Janel', 'Drama', 62),
(51, 'Jaxon Brown', 'Horror', 63),
(31, 'Liam Hill', 'Biography', 64),
(84, 'Michael Young', 'Sci-Fi', 65),
(82, 'Lillian Gonzalez', 'Sci-Fi', 66),
(46, 'Luke Carter', 'Horror', 67),
(77, 'Oliver Watson', 'Mystery', 68),
(81, 'Wyatt Carter', 'Crime', 69),
(34, 'Audrey Williams', 'Anthology', 70),
(65, 'Chloe Hughes', 'Adventure', 71),
(46, 'Charlotte Ramirez', 'Sci-Fi', 72),
(39, 'Levi Turner', 'Thriller', 73),
(81, 'Abigail Young', 'Sci-Fi', 74),
(34, 'Sophia Evans', 'Anthology', 75),
(23, 'Leah Rogers', 'Horror', 76),
(43, 'Asher Clark', 'Thriller', 77),
(49, 'Jayden Edwards', 'Crime', 78),
(43, 'Chloe Brown', 'Thriller', 79),
(55, 'Mark Rivera', 'Adventure', 80),
(76, 'Jack Allen', 'Drama', 81),
(84, 'Audrey Ortiz', 'History', 82),
(49, 'Alexander King', 'Anthology', 83),
(29, 'Julian Price', 'Drama', 84),
(28, 'Aiden Brown', 'Philosophy', 85),
(42, 'Nora Stewart', 'Anthology', 86),
(45, 'Eleanor Miller', 'Horror', 87),
(52, 'Mateo Jones', 'Thriller', 88),
(53, 'Amelia Morgan', 'Philosophy', 89),
(32, 'Mateo Johnson', 'Thriller', 90),
(62, 'Joshua Peterson', 'Fantasy', 91),
(62, 'Ethan Brooks', 'Sci-Fi', 92),
(38, 'Avery Campbell', 'Poetry', 93),
(80, 'Lincoln Wright', 'Mystery', 94),
(23, 'Asher Garcia', 'Romance', 95),
(34, 'Natalie Morgan', 'Horror', 96),
(55, 'Levi White', 'History', 97),
(53, 'Samuel Jones', 'Mystery', 98),
(78, 'Jackson Taylor', 'Crime', 99),
(60, 'Lillian Gray', 'Philosophy', 100) ^;

-- insert books
INSERT INTO "book" ("isbn", "title", "author_id", "id") VALUES
('001-JN', 'A History of Ancient Prague', 4, 1),
('002-JN', 'A People''s History', 4, 2),
('001-MJ', 'The Beatles Anthology', 1, 3),
('001-OG', 'Carrie', 2, 4),
('001-JP', 'Silent Mountain', 84, 5),
('001-CH', 'The Star of Mountain', 71, 6),
('001-ES', 'Under the Dark Flame', 14, 7),
('002-ES', 'Sacred Shadows', 14, 8),
('001-JG', 'The Twilight of Voyage', 35, 9),
('001-EB', 'The Voyage of Mountain', 92, 10),
('001-AO', 'The Odyssey of Archive', 82, 11),
('001-JA', 'A Tale of Shadow Memory', 33, 12),
('001-JW', 'The First Dream', 36, 13),
('001-EH', 'Broken Kingdom', 17, 14),
('001-AG', 'The Citadel of City', 95, 15),
('001-JC', 'Silent Echoes', 55, 16),
('001-EM', 'Wandering Shadows', 10, 17),
('001-AW', 'Whispers of the Frozen Citadel', 70, 18),
('001-CK', 'The Shadows of Forest', 19, 19),
('001-ZM', 'The Flame of Star', 40, 20),
('001-SR', 'Songs of the Dark Whisper', 6, 21),
('001-LG', 'Wandering Enigma', 100, 22),
('001-AC', 'Crystal Shadows', 53, 23),
('001-WW', 'Beyond the Wandering Memory', 31, 24),
('003-JN', 'Legends of the Hollow Horizon', 4, 25),
('002-AC', 'Distant Garden', 53, 26),
('001-LR', 'The Kingdom of Prophecy', 32, 27),
('001-MJ', 'Secrets of the Distant Forest', 90, 28),
('001-EG', 'In the Rising Star', 61, 29),
('001-GT', 'The Labyrinth of Covenant', 59, 30),
('001-LT', 'The Silver Voyage', 30, 31),
('001-MM', 'The Symphony of Enigma', 52, 32),
('001-ES', 'The Whisper of Ocean', 9, 33),
('001-LG', 'The Mountain of Covenant', 66, 34),
('001-WC', 'The Empire of Echoes', 69, 35),
('001-EA', 'Rise of the Broken Forest', 34, 36),
('003-ES', 'Shattered Whisper', 14, 37),
('001-LW', 'The Memory of Twilight', 94, 38),
('002-LG', 'Secrets of the Wandering Dawn', 66, 39),
('001-HB', 'The Forest of City', 25, 40),
('001-HK', 'Fall of the Hollow Prophecy', 56, 41),
('002-WC', 'Crystal Garden', 69, 42),
('002-AG', 'Hidden Whisper', 95, 43),
('002-HK', 'Songs of the Hollow Horizon', 56, 44),
('001-MR', 'The Mountain of Enigma', 80, 45),
('001-SK', 'Wandering Labyrinth', 16, 46),
('001-MY', 'The Mountain of Memory', 65, 47),
('001-LC', 'The City of Labyrinth', 42, 48),
('002-CH', 'In the Shattered Mountain', 71, 49),
('001-CM', 'Crystal Flame', 49, 50),
('001-JC', 'Frozen Labyrinth', 23, 51),
('002-MM', 'Rising Empire', 52, 52),
('001-WR', 'The Voyage of Memory', 39, 53),
('001-SE', 'Shadow Horizon', 75, 54),
('001-JJ', 'The Mirror of Mountain', 60, 55),
('001-AO', 'Endless Prophecy', 28, 56),
('003-AG', 'Songs of the Ancient Labyrinth', 95, 57),
('003-LG', 'Shadow Twilight', 66, 58),
('001-LF', 'In the Silver Labyrinth', 43, 59),
('001-SP', 'Beyond the Silent Forest', 29, 60),
('002-LR', 'The Twilight of Enigma', 32, 61),
('001-JT', 'Return to the Shattered Flame', 99, 62),
('001-JA', 'Hidden City', 81, 63),
('002-MJ', 'The Archive of Storm', 90, 64),
('003-LR', 'Songs of the Iron Empire', 32, 65),
('001-LW', 'Secrets of the Eternal Kingdom', 97, 66),
('003-MJ', 'Midnight Forest', 90, 67),
('001-CR', 'A Tale of Midnight Shadows', 72, 68),
('002-JJ', 'Wild Citadel', 60, 69),
('001-AC', 'The Dream of Flame', 77, 70),
('001-LC', 'Rising Garden', 57, 71),
('002-MY', 'The Covenant of Star', 65, 72),
('003-CH', 'The Flame of Kingdom', 71, 73),
('001-NM', 'The Mirror of Symphony', 96, 74),
('002-LW', 'Songs of the Broken Dream', 97, 75),
('002-LG', 'Endless Ocean', 100, 76),
('002-WW', 'The Mirror of Chronicles', 31, 77),
('002-EB', 'The Legacy of Symphony', 92, 78),
('002-LF', 'The Flame of Citadel', 43, 79),
('001-RB', 'Beyond the Eternal Storm', 11, 80),
('001-AM', 'Legends of the Hidden Chronicles', 89, 81),
('001-LP', 'The Horizon of Citadel', 54, 82),
('003-JJ', 'The Forest of Voyage', 60, 83),
('002-LP', 'The Flame of Dream', 54, 84),
('002-SE', 'Silent Enigma', 75, 85),
('001-SJ', 'Crimson Archive', 98, 86),
('002-MJ', 'The Labyrinth of Dream', 1, 87),
('001-SC', 'The Citadel of Garden', 50, 88),
('004-AG', 'Fallen Twilight', 95, 89),
('002-SP', 'The Archive of Empire', 29, 90),
('002-SC', 'The Mountain of Mountain', 50, 91),
('003-MM', 'Forgotten Covenant', 52, 92),
('004-JJ', 'Rise of the Sacred Empire', 60, 93),
('001-DS', 'Frozen Mountain', 51, 94),
('004-JN', 'Songs of the Shattered Shadows', 4, 95),
('005-JJ', 'The Broken Storm', 60, 96),
('002-LC', 'Return to the First Horizon', 42, 97),
('002-SJ', 'The Symphony of Dream', 98, 98),
('003-LP', 'The Covenant of Chronicles', 54, 99),
('002-EG', 'Legends of the Sacred Forest', 61, 100),
('001-CR', 'Songs of the Ancient Dream', 45, 101),
('002-JP', 'The Eternal Voyage', 84, 102),
('001-QY', 'Golden Legacy', 3, 103),
('002-EH', 'The Mountain of Echoes', 17, 104),
('001-LT', 'Return to the Iron Symphony', 73, 105),
('002-JT', 'The Kingdom of Twilight', 99, 106),
('001-JE', 'Iron Echoes', 78, 107),
('003-LG', 'Under the Dark Dawn', 100, 108),
('005-JN', 'The Dawn of Mountain', 4, 109),
('002-CM', 'The City of Voyage', 49, 110),
('002-EM', 'Iron Covenant', 10, 111),
('002-JA', 'A Tale of Iron Dream', 81, 112),
('002-WR', 'Burning Prophecy', 39, 113),
('002-SK', 'Fallen Forest', 16, 114),
('002-CR', 'Shattered Mountain', 45, 115),
('001-LS', 'Fall of the Shadow Horizon', 48, 116),
('002-OG', 'The Covenant of Archive', 2, 117),
('004-ES', 'The City of Shadows', 14, 118),
('003-HK', 'Legends of the Wild Ocean', 56, 119),
('002-JG', 'Fallen Star', 35, 120),
('003-WC', 'The Mirror of Memory', 69, 121),
('002-LW', 'Broken Horizon', 94, 122),
('004-LR', 'Under the Wandering Mirror', 32, 123),
('005-LR', 'The Dawn of Twilight', 32, 124),
('001-NS', 'The Horizon of Empire', 86, 125),
('001-LH', 'The Kingdom of Archive', 64, 126),
('002-AO', 'The Prophecy of Symphony', 28, 127),
('001-LT', 'The Flame of Twilight', 44, 128),
('004-MJ', 'The Citadel of Empire', 90, 129),
('001-LC', 'A Tale of Eternal Garden', 67, 130),
('003-AC', 'The Citadel of Dream', 53, 131),
('003-WW', 'Endless Mirror', 31, 132),
('002-QY', 'Under the Eternal Storm', 3, 133),
('002-AM', 'Under the Silver Dawn', 89, 134),
('002-LS', 'The Citadel of Odyssey', 48, 135),
('003-CR', 'The Garden of Citadel', 45, 136),
('003-LF', 'The City of Mirror', 43, 137),
('003-JG', 'The Symphony of Legacy', 35, 138),
('003-SK', 'Hidden Horizon', 16, 139),
('004-SK', 'Sacred Dream', 16, 140),
('003-AM', 'In the Hidden Garden', 89, 141),
('001-JJ', 'The Garden of Dawn', 62, 142),
('003-SJ', 'Burning Labyrinth', 98, 143),
('001-WF', 'Under the Eternal Whisper', 13, 144),
('002-JC', 'The Empire of City', 23, 145),
('004-WC', 'Under the Lost Forest', 69, 146),
('004-CH', 'Distant Dream', 71, 147),
('001-JB', 'The Frozen Labyrinth', 63, 148),
('003-EG', 'The Mirror of Horizon', 61, 149),
('001-LA', 'Under the Distant Archive', 24, 150),
('001-IH', 'Secrets of the Endless Chronicles', 15, 151),
('001-AY', 'Silver Forest', 74, 152),
('001-AG', 'Rise of the Last Chronicles', 20, 153),
('006-LR', 'Fall of the Velvet Memory', 32, 154),
('002-JE', 'Fall of the Crimson Mirror', 78, 155),
('002-LC', 'The Enigma of Dawn', 57, 156),
('002-JC', 'Rise of the Crystal Echoes', 55, 157),
('004-SJ', 'Songs of the Hidden Symphony', 98, 158),
('001-AB', 'Beyond the Eternal Kingdom', 85, 159),
('005-CH', 'Beyond the Silent Memory', 71, 160),
('001-AM', 'Burning Archive', 58, 161),
('001-JT', 'In the Last City', 38, 162),
('001-AW', 'Distant Mirror', 37, 163),
('003-EM', 'Eternal Star', 10, 164),
('002-EA', 'Secrets of the Dark Citadel', 34, 165),
('003-SP', 'Golden Star', 29, 166),
('004-JG', 'A Tale of Lost Kingdom', 35, 167),
('002-ZM', 'Crystal Covenant', 40, 168),
('002-LT', 'The Mirror of Echoes', 73, 169),
('006-JJ', 'Last City', 60, 170),
('004-MM', 'The Odyssey of Citadel', 52, 171),
('002-LH', 'The Chronicles of Twilight', 64, 172),
('002-SR', 'The Garden of Horizon', 6, 173),
('003-JE', 'The Empire of Chronicles', 78, 174),
('002-LT', 'Rising Enigma', 30, 175),
('002-AY', 'Silent Dream', 74, 176),
('001-EM', 'The Dawn of Forest', 87, 177),
('005-SJ', 'Return to the Wild Ocean', 98, 178),
('003-LC', 'The Kingdom of Dawn', 57, 179),
('004-HK', 'Rising Archive', 56, 180),
('001-MS', 'The Whisper of Memory', 12, 181),
('004-LF', 'Whispers of the Shattered City', 43, 182),
('003-LH', 'The Mountain of Storm', 64, 183),
('006-SJ', 'Lost Mirror', 98, 184),
('002-MS', 'The Symphony of Horizon', 12, 185),
('002-IH', 'The Enigma of Odyssey', 15, 186),
('003-MJ', 'Distant Citadel', 1, 187),
('007-JJ', 'Secret Twilight', 60, 188),
('003-LW', 'The Ocean of Mirror', 97, 189),
('007-SJ', 'In the Broken Citadel', 98, 190),
('003-EH', 'The Mirror of Flame', 17, 191),
('005-MJ', 'The Echoes of Empire', 90, 192),
('003-JA', 'Fallen Legacy', 81, 193),
('001-JP', 'Under the Sacred Empire', 91, 194),
('006-CH', 'The Chronicles of Legacy', 71, 195),
('003-IH', 'The Echoes of Ocean', 15, 196),
('002-AG', 'The Star of City', 20, 197),
('002-JT', 'Iron Symphony', 38, 198),
('004-LP', 'The Archive of Legacy', 54, 199),
('002-GT', 'Golden Storm', 59, 200) ^;

CREATE OR REPLACE PROCEDURE migrate_author_genre_in_batches(
    old_genre VARCHAR,
    new_genre VARCHAR,
    min_age INT,
    batch_size INT,
    OUT total_updated INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    batch_count INT;
BEGIN
    total_updated := 0;

    LOOP
        WITH target_authors AS (
            SELECT id
            FROM author
            WHERE genre = old_genre
              AND age >= min_age
            LIMIT batch_size
            FOR UPDATE SKIP LOCKED
        ),
        updated_authors AS (
            UPDATE author a
            SET genre = new_genre
            FROM target_authors ta
            WHERE a.id = ta.id
            RETURNING a.id
        ),
        updated_books AS (
            UPDATE book b
            SET title = '[' || new_genre || '] ' || b.title
            FROM updated_authors ua
            WHERE b.author_id = ua.id
              AND b.title NOT LIKE '[' || new_genre || '] %'
            RETURNING b.id
        )
        SELECT COUNT(*) INTO batch_count FROM updated_authors;

        total_updated := total_updated + COALESCE(batch_count, 0);

        COMMIT;

        EXIT WHEN batch_count = 0;
    END LOOP;
END;
$$ ^;

CREATE OR REPLACE PROCEDURE purge_authors_by_age_in_batches(
    min_age INT,
    batch_size INT,
    OUT deleted_authors INT,
    OUT deleted_books INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    batch_author_count INT;
    batch_book_count INT;
BEGIN
    deleted_authors := 0;
    deleted_books := 0;

    LOOP       
        WITH target_authors AS (
            SELECT id
            FROM author
            WHERE age >= min_age
            LIMIT batch_size
            FOR UPDATE SKIP LOCKED
        ),        
        removed_books AS (
            DELETE FROM book b
            USING target_authors ta
            WHERE b.author_id = ta.id
            RETURNING b.id
        ),        
        removed_authors AS (
            DELETE FROM author a
            USING target_authors ta
            WHERE a.id = ta.id
            RETURNING a.id
        )
        SELECT
            (SELECT COUNT(*) FROM removed_authors),
            (SELECT COUNT(*) FROM removed_books)
        INTO batch_author_count, batch_book_count;
        
        deleted_authors := deleted_authors + COALESCE(batch_author_count, 0);
        deleted_books := deleted_books + COALESCE(batch_book_count, 0);

        COMMIT;
        
        EXIT WHEN batch_author_count = 0;
    END LOOP;
END;
$$ ^;