-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
-- Table "inventory"
CREATE TABLE "inventory" (
  "id"       BIGINT       NOT NULL,
  "quantity" INTEGER      NOT NULL,
  "title"    VARCHAR(255) DEFAULT NULL,
  "version"  INTEGER      NOT NULL,
  CONSTRAINT "author_pk" PRIMARY KEY ("id")
);
