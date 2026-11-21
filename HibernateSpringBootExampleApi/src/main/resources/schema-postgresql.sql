-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table "book"
CREATE TABLE "book" (
  "id"     BIGINT       NOT NULL,
  "price"  INTEGER      DEFAULT NULL,
  "title"  VARCHAR(255) DEFAULT NULL,
  "isbn"   VARCHAR(255) DEFAULT NULL,
  "author" VARCHAR(255) DEFAULT NULL,
  "genre"  VARCHAR(255) DEFAULT NULL,
  CONSTRAINT "book_pk" PRIMARY KEY ("id")
);
