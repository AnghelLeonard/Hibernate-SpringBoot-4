-- Drop tables
DROP TABLE IF EXISTS "author", "book", "format" CASCADE;

-- Table book
CREATE TABLE "book" (
  "id"        BIGINT       NOT NULL,    
  "title"     VARCHAR(255),
  "isbn"      VARCHAR(255),
  CONSTRAINT "book_pk" PRIMARY KEY ("id"));

-- Table format
CREATE TABLE "format" (
  "id"           BIGINT        NOT NULL,    
  "format_type"  VARCHAR(255),
  CONSTRAINT "format_pk" PRIMARY KEY ("id"));

