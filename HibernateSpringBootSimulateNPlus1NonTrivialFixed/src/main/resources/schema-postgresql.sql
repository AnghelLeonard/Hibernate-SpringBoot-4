-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Create tables
CREATE TABLE "publisher" (
    "id" BIGINT PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL
);

CREATE TABLE "literary_agency" (
    "id" BIGINT PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL
);

CREATE TABLE "author" (
    "id" BIGINT PRIMARY KEY,
    "pen_name" VARCHAR(255) NOT NULL,
    "agency_id" BIGINT,
    CONSTRAINT "fk_author_agency" 
        FOREIGN KEY ("agency_id") REFERENCES "literary_agency"("id")
);

CREATE TABLE "book" (
    "id" BIGINT PRIMARY KEY,
    "isbn" VARCHAR(20) NOT NULL UNIQUE,
    "status" VARCHAR(50) NOT NULL,
    "publisher_id" BIGINT,
    CONSTRAINT "fk_book_publisher" 
        FOREIGN KEY ("publisher_id") REFERENCES "publisher"("id")
);

CREATE TABLE "royalty_contract" (
    "id" BIGINT PRIMARY KEY,
    "contract_code" VARCHAR(100) NOT NULL UNIQUE,
    "book_id" BIGINT NOT NULL UNIQUE,
    CONSTRAINT "fk_contract_book" 
        FOREIGN KEY ("book_id") REFERENCES "book"("id") ON DELETE CASCADE
);

CREATE TABLE "book_author" (
    "id" BIGINT PRIMARY KEY,
    "role" VARCHAR(50) NOT NULL,
    "royalty_split" NUMERIC(5, 2) NOT NULL,
    "book_id" BIGINT NOT NULL,
    "author_id" BIGINT NOT NULL,
    CONSTRAINT "fk_book_author_book" 
        FOREIGN KEY ("book_id") REFERENCES "book"("id") ON DELETE CASCADE,
    CONSTRAINT "fk_book_author_author" 
        FOREIGN KEY ("author_id") REFERENCES "author"("id"),
    CONSTRAINT "uq_book_author" UNIQUE ("book_id", "author_id")
);

-- Foreign key and filter indexes
CREATE INDEX "idx_author_agency_id" ON "author"("agency_id");
CREATE INDEX "idx_book_publisher_id" ON "book"("publisher_id");
CREATE INDEX "idx_book_status" ON "book"("status");
CREATE INDEX "idx_book_author_book_id" ON "book_author"("book_id");
CREATE INDEX "idx_book_author_author_id" ON "book_author"("author_id");