-- Recreate database
DROP TABLE IF EXISTS "author", "book" CASCADE;

-- Table book
CREATE TABLE "book" (
  "id"        BIGINT       NOT NULL,  
  "price"     INTEGER,
  "title"     VARCHAR(255),
  "isbn"      VARCHAR(255),
  CONSTRAINT "book_pk" PRIMARY KEY ("id"))

