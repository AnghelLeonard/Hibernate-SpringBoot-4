-- Drop schema
DROP SCHEMA IF EXISTS "public" CASCADE;
CREATE SCHEMA "public";

-- Table book
CREATE TABLE "book" (
  "id"        BIGINT       NOT NULL,  
  "price"     INTEGER,
  "title"     VARCHAR(255),
  "isbn"      VARCHAR(255),
  CONSTRAINT "book_pk" PRIMARY KEY ("id"))