-- Drop tables
DROP TABLE IF EXISTS "author" CASCADE;

-- Table author
CREATE TABLE "author" (
  "age"   INTEGER NOT NULL, 
  "id"    BIGINT  NOT NULL, 
  "genre" VARCHAR(255), 
  "name"  VARCHAR(255), 
  CONSTRAINT "author_pk" PRIMARY KEY ("id"))