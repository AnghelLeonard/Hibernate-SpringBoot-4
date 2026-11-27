-- Drop schema
DROP SCHEMA IF EXISTS "public" CASCADE;
CREATE SCHEMA "public";

-- Table "author"
CREATE TABLE "author" (
    "id" BIGINT               NOT NULL,
    "age" INTEGER             NOT NULL,
    "best_selling" VARCHAR(3) NOT NULL,
    "genre" VARCHAR(255)      DEFAULT NULL,
    "name" VARCHAR(255)       DEFAULT NULL,
    CONSTRAINT "author_pk" PRIMARY KEY ("id")
);
