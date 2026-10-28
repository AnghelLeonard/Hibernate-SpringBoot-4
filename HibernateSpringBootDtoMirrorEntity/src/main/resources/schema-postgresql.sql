-- Drop tables
DROP TABLE IF EXISTS "author", "book" CASCADE;

-- Table author
CREATE TABLE "author" (
  "id"        BIGINT        NOT NULL,
  "age"       INTEGER,    
  "genre"     VARCHAR(255),
  "name"      VARCHAR(255),
  "address"   VARCHAR(255), 
  "nickname"  VARCHAR(255),
  "language"  VARCHAR(255),
  "rank"      INTEGER,
  CONSTRAINT "author_pk" PRIMARY KEY ("id"))
