-- Drop tables
DROP TABLE IF EXISTS "author", "book" CASCADE;

-- Table author
CREATE TABLE "author" (
  "id"    BIGINT        NOT NULL,
  "age"   INTEGER,    
  "genre" VARCHAR(255),
  "name"  VARCHAR(255),
  CONSTRAINT "author_pk" PRIMARY KEY ("id"));

-- Table book
CREATE TABLE "book" (
  "id"        BIGINT       NOT NULL,  
  "genre"     VARCHAR(255),
  "title"     VARCHAR(255),
  "isbn"      VARCHAR(255),
  CONSTRAINT "book_pk" PRIMARY KEY ("id"));