-- Drop schema
DROP SCHEMA IF EXISTS "public" CASCADE;
CREATE SCHEMA "public";

-- Table "author"
CREATE TABLE "author" (
  "id"    BIGINT       NOT NULL,
  "age"   INT          NOT NULL,
  "genre" VARCHAR(255) DEFAULT NULL,
  "name"  VARCHAR(255) DEFAULT NULL,
  CONSTRAINT "author_pk" PRIMARY KEY ("id")
);

-- Table "book"
CREATE TABLE "book" (
  "id"    BIGINT       NOT NULL,
  "title" VARCHAR(255) DEFAULT NULL,
  "isbn"  VARCHAR(255) DEFAULT NULL,
  CONSTRAINT "book_pk" PRIMARY KEY ("id")
);

-- Table "author_books"
CREATE TABLE "author_books" (
  "author_id" BIGINT NOT NULL,
  "books_id"  BIGINT NOT NULL,
  CONSTRAINT "author_book_uk" UNIQUE ("author_id", "books_id"),
  CONSTRAINT "author_fk" FOREIGN KEY ("author_id") REFERENCES "author" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "books_fk" FOREIGN KEY ("books_id") REFERENCES "book" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);
