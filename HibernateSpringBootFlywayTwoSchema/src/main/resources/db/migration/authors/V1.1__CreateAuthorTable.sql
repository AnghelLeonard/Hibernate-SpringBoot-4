-- Table "author"
CREATE TABLE IF NOT EXISTS "author" (
    "id" BIGINT NOT NULL,
    "age" INTEGER NOT NULL,
    "genre" VARCHAR(255),
    "name" VARCHAR(255),
    "books" VARCHAR(255),
    CONSTRAINT "author_pk" PRIMARY KEY ("id"));

ALTER TABLE "author" OWNER TO "postgres";