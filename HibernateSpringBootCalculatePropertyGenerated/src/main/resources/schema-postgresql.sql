-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table `book`
CREATE TABLE "book" (
  "id"         BIGINT NOT NULL,
  "discounted" DOUBLE PRECISION GENERATED ALWAYS AS ("price" - "price" * 0.25),
--"discounted" DOUBLE PRECISION GENERATED ALWAYS AS ("price" - "price" * 0.25) STORED,
  "price"      DOUBLE PRECISION NOT NULL,
  "isbn"       VARCHAR(255),
  "title"      VARCHAR(255),
  PRIMARY KEY ("id"));
