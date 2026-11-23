-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table "author"
CREATE TABLE "author" (
    "id" BIGINT NOT NULL,
    "age" INTEGER NOT NULL,
    "genre" VARCHAR(255) DEFAULT NULL,
    "name" VARCHAR(255) DEFAULT NULL,
    CONSTRAINT "author_pk" PRIMARY KEY ("id")
);

-- Table "book"
CREATE TABLE "book" (
    "id" BIGINT NOT NULL,
    "author_id" BIGINT,
    "dtype" SMALLINT NOT NULL CHECK ("dtype" IN (1, 2, 3)),
    "format" VARCHAR(255),
    "isbn" VARCHAR(255),
    "size_in" VARCHAR(255),
    "title" VARCHAR(255),
    "weight_lbs" VARCHAR(255),
    CONSTRAINT "book_pk" PRIMARY KEY ("id"),
    CONSTRAINT "book_author_fk" FOREIGN KEY ("author_id") REFERENCES "author" ("id")
);

