-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table book
CREATE TABLE "book" (
  "id"        BIGINT       NOT NULL,  
  "price"     INTEGER,
  "title"     VARCHAR(255),
  "isbn"      VARCHAR(255),
  "status"    VARCHAR(255),
  CONSTRAINT "book_pk" PRIMARY KEY ("id")) ^;

CREATE OR REPLACE FUNCTION apply_discount(original_price INT, discount_percentage INT)
RETURNS INT
LANGUAGE plpgsql
IMMUTABLE
AS $$
DECLARE
    final_price INT;
BEGIN
    IF original_price <= 0 THEN
        RETURN 0;
    ELSIF discount_percentage <= 0 THEN
        RETURN original_price;
    ELSIF discount_percentage >= 100 THEN
        RETURN 0;
    END IF;

    final_price := ROUND(original_price * (1 - (discount_percentage / 100.0)));

    RETURN final_price;
END;
$$ ^;

CREATE OR REPLACE FUNCTION slugify(input_text VARCHAR(255))
RETURNS VARCHAR(255)
LANGUAGE plpgsql
IMMUTABLE
AS $$
DECLARE
    slug VARCHAR(255);
BEGIN
    slug := LOWER(input_text);
    slug := REPLACE(slug, '&', 'and');
    slug := REGEXP_REPLACE(slug, '[^a-z0-9]+', '-', 'g');
    slug := TRIM(BOTH '-' FROM slug);

    RETURN slug;
END;
$$ ^;
