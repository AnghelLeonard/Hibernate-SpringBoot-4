-- Table "book"
CREATE TABLE IF NOT EXISTS "book" (
    "id" BIGINT NOT NULL,
    "isbn" VARCHAR(255),
    "title" VARCHAR(255),
    "author_id" BIGINT,
    CONSTRAINT "book_pk" PRIMARY KEY ("id"),
    CONSTRAINT "book_author_fk" FOREIGN KEY ("author_id")
        REFERENCES "author" ("id")
);

ALTER TABLE "book" OWNER TO "postgres";
