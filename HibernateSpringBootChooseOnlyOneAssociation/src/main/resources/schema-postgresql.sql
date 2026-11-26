-- Drop schema
DROP SCHEMA IF EXISTS "public" CASCADE ^;
CREATE SCHEMA "public" ^;

-- Table "article"
CREATE TABLE "article" (
    "id" BIGINT          NOT NULL,
    "title" VARCHAR(255) DEFAULT NULL,
    CONSTRAINT "article_pk" PRIMARY KEY ("id")
) ^;

-- Table "magazine"
CREATE TABLE "magazine" (
    "id" BIGINT          NOT NULL,
    "title" VARCHAR(255) DEFAULT NULL,
    CONSTRAINT "magazine_pk" PRIMARY KEY ("id")
) ^;

-- Table "book"
CREATE TABLE "book" (
    "id" BIGINT          NOT NULL,
    "title" VARCHAR(255) DEFAULT NULL,
    CONSTRAINT "book_pk" PRIMARY KEY ("id")
) ^;

-- Table "review"
CREATE TABLE "review" (
    "id" BIGINT    NOT NULL,
    "content" TEXT DEFAULT NULL,
    "book_id" BIGINT,
    "article_id" BIGINT,
    "magazine_id" BIGINT,
    CONSTRAINT "review_pk" PRIMARY KEY ("id"),
    CONSTRAINT "book_review_fk" FOREIGN KEY ("book_id") REFERENCES "book" ("id"),
    CONSTRAINT "article_review_fk" FOREIGN KEY ("article_id") REFERENCES "article" ("id"),
    CONSTRAINT "magazine_review_fk" FOREIGN KEY ("magazine_id") REFERENCES "magazine" ("id")
) ^;

-- Create the trigger function
CREATE OR REPLACE FUNCTION "just_one_of_many_func"()
RETURNS TRIGGER AS $$
BEGIN
    IF (NEW."article_id" IS NOT NULL AND NEW."magazine_id" IS NOT NULL) OR 
       (NEW."article_id" IS NOT NULL AND NEW."book_id" IS NOT NULL) OR
       (NEW."book_id" IS NOT NULL AND NEW."magazine_id" IS NOT NULL) THEN
        RAISE EXCEPTION 'A review can be associated with either a book, a magazine or an article'
        USING ERRCODE = '45000';
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql ^;

-- Attach the trigger to the "review" table
CREATE TRIGGER "Just_One_Of_Many"
    BEFORE INSERT ON "review"
    FOR EACH ROW
    EXECUTE FUNCTION "just_one_of_many_func"() ^;


