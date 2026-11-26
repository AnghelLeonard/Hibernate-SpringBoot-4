-- Drop tables
DROP SCHEMA public CASCADE ^;
CREATE SCHEMA public ^;

-- Table "author"
CREATE TABLE "author" (
    "id" BIGINT NOT NULL,
    "age" INTEGER NOT NULL,
    "genre" VARCHAR(255) DEFAULT NULL,
    "name" VARCHAR(255) DEFAULT NULL,
    CONSTRAINT "author_pk" PRIMARY KEY ("id")
) ^;

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
) ^;

-- Drop existing triggers
DROP TRIGGER IF EXISTS "ebook_format_trigger" ON "book" ^;
DROP TRIGGER IF EXISTS "paperback_weight_trigger" ON "book" ^;
DROP TRIGGER IF EXISTS "paperback_size_trigger" ON "book" ^;

-- Drop existing trigger functions
DROP FUNCTION IF EXISTS "check_ebook_format"() ^;
DROP FUNCTION IF EXISTS "check_paperback_weight"() ^;
DROP FUNCTION IF EXISTS "check_paperback_size"() ^;

-- E-book format trigger function and trigger
CREATE OR REPLACE FUNCTION "check_ebook_format"()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW."dtype" = 3 AND NEW."format" IS NULL THEN
        RAISE EXCEPTION 'The format of e-book cannot be null' USING ERRCODE = '45000';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql ^;

CREATE TRIGGER "ebook_format_trigger"
BEFORE INSERT ON "book"
FOR EACH ROW
EXECUTE FUNCTION "check_ebook_format"() ^;

-- Paperback weight trigger function and trigger
CREATE OR REPLACE FUNCTION "check_paperback_weight"()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW."dtype" = 2 AND NEW."weight_lbs" IS NULL THEN
        RAISE EXCEPTION 'The weight of paperback cannot be null' USING ERRCODE = '45000';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql ^;

CREATE TRIGGER "paperback_weight_trigger"
BEFORE INSERT ON "book"
FOR EACH ROW
EXECUTE FUNCTION "check_paperback_weight"() ^;

-- Paperback size trigger function and trigger
CREATE OR REPLACE FUNCTION "check_paperback_size"()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW."dtype" = 2 AND NEW."size_in" IS NULL THEN
        RAISE EXCEPTION 'The size of paperback cannot be null' USING ERRCODE = '45000';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql ^;

CREATE TRIGGER "paperback_size_trigger"
BEFORE INSERT ON "book"
FOR EACH ROW
EXECUTE FUNCTION "check_paperback_size"() ^;

