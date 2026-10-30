-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table author
CREATE TABLE "author" (
  "id"    BIGINT        NOT NULL,
  "age"   INTEGER,    
  "genre" VARCHAR(255),
  "name"  VARCHAR(255),
  CONSTRAINT "author_pk" PRIMARY KEY ("id"));

-- Table publisher
CREATE TABLE "publisher" (
  "id"        BIGINT        NOT NULL,    
  "company"   VARCHAR(255),  
CONSTRAINT "publisher_pk" PRIMARY KEY ("id"));

-- Table book
CREATE TABLE "book" (
  "id"           BIGINT        NOT NULL,  
  "author_id"    BIGINT,
  "publisher_id" BIGINT,
  "title"        VARCHAR(255),
  "isbn"         VARCHAR(255),
  CONSTRAINT "book_pk" PRIMARY KEY ("id"),
  CONSTRAINT "book_author_fk" FOREIGN KEY ("author_id") REFERENCES "author" ("id"),
  CONSTRAINT "book_publisher_fk" FOREIGN KEY ("publisher_id") REFERENCES "publisher" ("id"));