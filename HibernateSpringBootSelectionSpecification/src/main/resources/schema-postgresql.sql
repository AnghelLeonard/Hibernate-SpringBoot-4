-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table "author"
CREATE TABLE "author" (
  "id"   BIGINT       NOT NULL,
  "name" VARCHAR(255) DEFAULT NULL,
  CONSTRAINT "author_pk" PRIMARY KEY ("id")
);

-- Table "book"
CREATE TABLE "book" (
  "id"               BIGINT       NOT NULL,
  "author_id"        BIGINT       NOT NULL,
  "title"            VARCHAR(255) DEFAULT NULL,
  "isbn"             VARCHAR(255) DEFAULT NULL,
  "publication_date" TIMESTAMP    NOT NULL,
  CONSTRAINT "book_pk" PRIMARY KEY ("id"),
  CONSTRAINT "book_author_fk" FOREIGN KEY ("author_id") REFERENCES "author" ("id")
);
