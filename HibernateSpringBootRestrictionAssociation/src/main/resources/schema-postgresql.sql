-- Drop schema
DROP SCHEMA IF EXISTS "public" CASCADE;
CREATE SCHEMA "public";

-- Table "author"
CREATE TABLE "author" (
  "id"    BIGINT        NOT NULL,
  "age"   INTEGER       NOT NULL,
  "genre" VARCHAR(255)  DEFAULT NULL,
  "name"  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT "author_pk" PRIMARY KEY ("id")
);

-- Table "book"
CREATE TABLE "book" (
  "id"           BIGINT        NOT NULL,  
  "author_id"    BIGINT        NOT NULL,
  "discontinued" BOOLEAN       NOT NULL DEFAULT FALSE,
  "title"        VARCHAR(255)  DEFAULT NULL,
  "isbn"         VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT "book_pk" PRIMARY KEY ("id"),
  CONSTRAINT "book_author_fk" FOREIGN KEY ("author_id") REFERENCES "author" ("id")
);
