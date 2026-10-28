-- Drop tables
DROP TABLE IF EXISTS "author", "book" CASCADE;

-- Table author
CREATE TABLE "author" (
  "id"           BIGINT        NOT NULL,
  "age"          INTEGER,    
  "author_genre" VARCHAR(255),
  "author_name"  VARCHAR(255),
  CONSTRAINT "author_pk" PRIMARY KEY ("id"));

-- Table book
CREATE TABLE "book" (
  "id"             BIGINT       NOT NULL,  
  "author_id"      BIGINT,
  "book_title"     VARCHAR(255),
  "book_isbn"      VARCHAR(255),
  CONSTRAINT "book_pk" PRIMARY KEY ("id"),
  CONSTRAINT "book_author_fk" FOREIGN KEY ("author_id") REFERENCES "author" ("id"));