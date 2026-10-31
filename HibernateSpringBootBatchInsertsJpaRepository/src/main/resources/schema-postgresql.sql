-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table author
CREATE TABLE "author" (
  "id"      BIGINT        NOT NULL,
  "age"     INTEGER,  
  "version" INTEGER       NOT NULL DEFAULT 0,  
  "genre"   VARCHAR(255),
  "name"    VARCHAR(255),
  CONSTRAINT "author_pk" PRIMARY KEY ("id"));