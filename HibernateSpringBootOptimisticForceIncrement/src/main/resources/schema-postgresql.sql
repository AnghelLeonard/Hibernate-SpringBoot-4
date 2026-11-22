-- Recreate database
-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table "chapter"
CREATE TABLE "chapter" (
  "id"      BIGINT       NOT NULL,
  "version" INTEGER      NOT NULL,
  "content" VARCHAR(255) DEFAULT NULL,
  "title"   VARCHAR(255) DEFAULT NULL,
  CONSTRAINT "chapter_pk" PRIMARY KEY ("id")
);

-- Table "modification"
CREATE TABLE "modification" (
  "id" BIGINT                 NOT NULL,
  "chapter_id"   BIGINT       NOT NULL,
  "description"  VARCHAR(255) DEFAULT NULL,
  "modification" VARCHAR(255) DEFAULT NULL,
  CONSTRAINT "modification_pk" PRIMARY KEY ("id"),
  CONSTRAINT "modification_chapter_fk" FOREIGN KEY ("chapter_id") REFERENCES "chapter" ("id")
);
