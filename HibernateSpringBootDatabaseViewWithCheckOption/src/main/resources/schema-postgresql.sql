-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table "author"
CREATE TABLE "author" (
  "id"        BIGINT        NOT NULL,
  "age"       INTEGER       NOT NULL,
  "genre"     VARCHAR(255)  DEFAULT NULL,
  "name"      VARCHAR(255)  DEFAULT NULL,
  "sellrank"  INTEGER       DEFAULT NULL,
  "royalties" INTEGER       DEFAULT NULL,
  "rating"    INTEGER       DEFAULT NULL,
  CONSTRAINT "author_pk" PRIMARY KEY ("id"));