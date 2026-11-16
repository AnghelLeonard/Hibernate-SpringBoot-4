-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table author
CREATE TABLE "author" (
  "id"         BIGINT                   NOT NULL,
  "age"        INTEGER,    
  "genre"      VARCHAR(255),
  "name"       VARCHAR(255),
  "created_at" TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT "author_pk" PRIMARY KEY ("id"));

-- Table book
CREATE TABLE "book" (
  "id"        BIGINT       NOT NULL,  
  "author_id" BIGINT,
  "title"     VARCHAR(255),
  "isbn"      VARCHAR(255),
  CONSTRAINT "book_pk" PRIMARY KEY ("id"),
  CONSTRAINT "book_author_fk" FOREIGN KEY ("author_id") REFERENCES "author" ("id"));

CREATE INDEX "idx_author_genre_name_id" ON "author" ("genre", "name" ASC, "id" ASC);
CREATE INDEX "idx_book_author_id" ON "book" ("author_id");
CREATE INDEX idx_author_genre_created_at_id ON "author" ("genre", "created_at" ASC, "id" ASC);