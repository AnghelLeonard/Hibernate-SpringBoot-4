-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table "shopping_cart"
CREATE TABLE "shopping_cart" (
  "id"    BIGINT        NOT NULL,  
  "owner" VARCHAR(255),  
  CONSTRAINT "shopping_cart_pk" PRIMARY KEY ("id"));

-- Table "shopping_cart_books"
CREATE TABLE "shopping_cart_books" (
  "shopping_cart_id"  BIGINT        NOT NULL,  
  "title"             VARCHAR(255),
  CONSTRAINT "shopping_cart_books_fk" FOREIGN KEY ("shopping_cart_id") REFERENCES "shopping_cart" ("id"));