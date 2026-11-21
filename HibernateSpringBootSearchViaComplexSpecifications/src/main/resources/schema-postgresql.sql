-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table "author"
CREATE TABLE "author" (
  "id"     BIGINT       NOT NULL,
  "age"    INTEGER      NOT NULL,
  "genre"  VARCHAR(255) DEFAULT NULL,
  "name"   VARCHAR(255) DEFAULT NULL,
  "rating" INTEGER      DEFAULT NULL,
  CONSTRAINT "author_pk" PRIMARY KEY ("id")
);

-- Table "book"
CREATE TABLE "book" (
  "id"        BIGINT       NOT NULL,
  "name"      VARCHAR(255) DEFAULT NULL,
  "title"     VARCHAR(255) DEFAULT NULL,
  "isbn"      VARCHAR(255) DEFAULT NULL,
  "price"     INTEGER      DEFAULT NULL,
  "author_id" BIGINT       DEFAULT NULL,
  CONSTRAINT "book_pk" PRIMARY KEY ("id"),
  CONSTRAINT "fk_book_author" FOREIGN KEY ("author_id") REFERENCES "author" ("id")
);

-- Index on foreign key for faster joins
CREATE INDEX "idx_book_author_id" ON "book" ("author_id");

