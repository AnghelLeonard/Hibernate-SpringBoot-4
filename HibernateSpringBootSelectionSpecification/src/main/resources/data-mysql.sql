-- insert authors
INSERT INTO `author` (`name`) VALUES 
('J.R.R. Tolkien'),
('George R.R. Martin'),
('Brandon Sanderson'),
('Frank Herbert'),
('Stephen King'),
('Isaac Asimov'),
('Arthur C. Clarke'),
('Neil Gaiman'),
('Terry Pratchett'),
('Patrick Rothfuss'),
('H.P. Lovecraft'),
('Agatha Christie'),
('Dan Brown'),
('J.K. Rowling'),
('Philip K. Dick'),
('Ursula K. Le Guin'),
('Robert Jordan'),
('Haruki Murakami'),
('Ernest Hemingway'),
('Mark Twain')
ON DUPLICATE KEY UPDATE `id`=`id`;

-- insert books
INSERT INTO `book` (`title`, `isbn`, `publication_date`, `author_id`) VALUES 
-- J.R.R. Tolkien (id: 1)
('The Fellowship of the Ring', '9780618640157', '1954-07-29 00:00:00', 1),
('The Two Towers', '9780618640164', '1954-11-11 00:00:00', 1),
('The Return of the King', '9780618640171', '1955-10-20 00:00:00', 1),

-- George R.R. Martin (id: 2)
('A Game of Thrones', '9780553103540', '1996-08-01 00:00:00', 2),
('A Clash of Kings', '9780553108033', '1998-11-16 00:00:00', 2),
('A Storm of Swords', '9780553106634', '2000-08-08 00:00:00', 20),

-- Brandon Sanderson (id: 3)
('The Way of Kings', '9780765326355', '2015-08-31 00:00:00', 3),
('Words of Radiance', '9780765326362', '2014-03-04 00:00:00', 3),
('Oathbringer', '9780765326379', '2017-11-14 00:00:00', 3),
('Rhythm of War', '9780765326386', '2020-11-17 08:00:00', 3),
('The Lost Metal', '9781761049750', '2022-11-15 09:00:00', 3),
('The Wind and Truth', '9781250319180', '2024-12-06 00:00:00', 3),

-- Frank Herbert (id: 4)
('Dune', '9780441172719', '2022-08-01 00:00:00', 4),
('Dune Messiah', '9780441172696', '2020-10-01 00:00:00', 4),
('Children of Dune', '9780441104025', '2024-04-01 00:00:00', 4),

-- Stephen King (id: 5)
('The Shining', '9780385121682', '1977-01-28 00:00:00', 5),
('It', '9780670813025', '1986-09-15 00:00:00', 5),
('Fairy Tale', '9781668002179', '2022-09-06 05:00:00', 5),
('Holly', '9781668016138', '2023-09-05 06:00:00', 5),

-- Isaac Asimov (id: 6)
('Foundation', '9780553293357', '1951-06-01 00:00:00', 6),
('Foundation and Empire', '9780553293371', '1952-06-01 00:00:00', 6),
('Second Foundation', '9780553293364', '1953-06-01 00:00:00', 6),

-- Arthur C. Clarke (id: 7)
('2001: A Space Odyssey', '9780453002691', '1968-06-01 00:00:00', 7),
('Rendezvous with Rama', '9780491011129', '1973-06-01 00:00:00', 7),

-- Neil Gaiman (id: 8)
('American Gods', '9780380973651', '2001-06-19 00:00:00', 8),
('Anansi Boys', '9780380973668', '2005-09-20 00:00:00', 8),
('The Ocean at the End of the Lane', '9780062255655', '2013-06-18 00:00:00', 8),

-- Terry Pratchett (id: 9)
('The Colour of Magic', '9780861403240', '1983-11-24 00:00:00', 9),
('The Light Fantastic', '9780861403257', '1986-06-02 00:00:00', 9),
('Equal Rites', '9780575039506', '1987-01-15 00:00:00', 9),

-- Patrick Rothfuss (id: 10)
('The Name of the Wind', '9780756404079', '2007-03-27 00:00:00', 10),
('The Wise Mans Fear', '9780756404734', '2011-03-01 00:00:00', 10),

-- H.P. Lovecraft (id: 11)
('The Call of Cthulhu', '9780618346257', '1928-02-01 00:00:00', 11),

-- Agatha Christie (id: 12)
('Murder on the Orient Express', '9780007119318', '1934-01-01 00:00:00', 12),
('And Then There Were None', '9780062073488', '1939-11-06 00:00:00', 12),

-- Dan Brown (id: 13)
('Angels & Demons', '9780671027360', '2000-05-01 00:00:00', 13),
('The Da Vinci Code', '9780385504201', '2003-03-18 00:00:00', 13),

-- J.K. Rowling (id: 14)
('Harry Potter and the Philosophers Stone', '9780747532699', '1997-06-26 00:00:00', 14),
('Harry Potter and the Chamber of Secrets', '9780747538493', '1998-07-02 00:00:00', 14),

-- Philip K. Dick (id: 15)
('Do Androids Dream of Electric Sheep?', '9780345404473', '1968-03-01 00:00:00', 15),
('Ubik', '9780394516882', '1969-05-01 00:00:00', 15),

-- Ursula K. Le Guin (id: 16)
('A Wizard of Earthsea', '9780395276532', '1968-11-01 00:00:00', 16),
('The Left Hand of Darkness', '9780441478125', '1969-03-01 00:00:00', 16),

-- Robert Jordan (id: 17)
('The Eye of the World', '9780312850098', '1990-01-15 00:00:00', 17),
('The Great Hunt', '9780312851408', '1990-11-15 00:00:00', 17),

-- Haruki Murakami (id: 18)
('Norwegian Wood', '9784062036321', '1987-09-04 00:00:00', 18),
('Kafka on the Shore', '9784104259021', '2002-09-12 00:00:00', 18),

-- Ernest Hemingway (id: 19)
('The Old Man and the Sea', '9780684830490', '1952-09-01 00:00:00', 19),
('A Farewell to Arms', '9780684801469', '1929-09-27 00:00:00', 19),

-- Mark Twain (id: 20)
('Adventures of Huckleberry Finn', '9780486280615', '1884-12-10 00:00:00', 20),
('The Adventures of Tom Sawyer', '9780486418599', '1876-06-09 00:00:00', 20)
ON DUPLICATE KEY UPDATE `id`=`id`;
