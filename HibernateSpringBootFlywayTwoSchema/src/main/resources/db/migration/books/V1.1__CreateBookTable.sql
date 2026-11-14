-- Table "book"
CREATE TABLE IF NOT EXISTS "book" (
    "id" BIGINT NOT NULL,
    "isbn" VARCHAR(255),
    "title" VARCHAR(255),
    "authors" VARCHAR(255),
    CONSTRAINT "book_pk" PRIMARY KEY ("id"));

ALTER TABLE "book" OWNER TO "postgres";
