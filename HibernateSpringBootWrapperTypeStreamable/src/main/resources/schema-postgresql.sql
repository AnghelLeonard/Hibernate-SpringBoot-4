-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table book
CREATE TABLE "book" (
  "id"        BIGINT       NOT NULL,  
  "price"     INTEGER,
  "title"     VARCHAR(255),  
  CONSTRAINT "book_pk" PRIMARY KEY ("id"))
