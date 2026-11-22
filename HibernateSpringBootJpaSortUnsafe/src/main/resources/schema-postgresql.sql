-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table "book"
CREATE TABLE "book" (
  "id"                  BIGINT           NOT NULL,
  "title"               VARCHAR(255)     DEFAULT NULL,
  "isbn"                VARCHAR(255)     DEFAULT NULL,
  "price"               DOUBLE PRECISION DEFAULT NULL,
  "discount_percentage" DOUBLE PRECISION DEFAULT NULL,
  CONSTRAINT "book_pk" PRIMARY KEY ("id")
);
