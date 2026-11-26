-- Drop schema
DROP SCHEMA IF EXISTS "public" CASCADE;
CREATE SCHEMA "public";

-- Table "author_list"
CREATE TABLE "author_list" (
  "id"    BIGINT        NOT NULL,
  "age"   INTEGER       NOT NULL,
  "genre" VARCHAR(255)  DEFAULT NULL,
  "name"  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT "author_list_pk" PRIMARY KEY ("id")
);

-- Table "book_list"
CREATE TABLE "book_list" (
  "id"        BIGINT        NOT NULL,    
  "title"     VARCHAR(255)  DEFAULT NULL,
  "isbn"      VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT "book_list_pk" PRIMARY KEY ("id")
);

-- Table "author_set"
CREATE TABLE "author_set" (
  "id"    BIGINT        NOT NULL,
  "age"   INTEGER       NOT NULL,
  "genre" VARCHAR(255)  DEFAULT NULL,
  "name"  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT "author_set_pk" PRIMARY KEY ("id")
);

-- Table "book_set"
CREATE TABLE "book_set" (
  "id"        BIGINT        NOT NULL,    
  "title"     VARCHAR(255)  DEFAULT NULL,
  "isbn"      VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT "book_set_pk" PRIMARY KEY ("id")
);

-- Table "author_book_list"
CREATE TABLE "author_book_list" (
  "author_id" BIGINT NOT NULL,
  "book_id"   BIGINT NOT NULL,
  CONSTRAINT "author_book_list_uk" UNIQUE ("author_id", "book_id"),
  CONSTRAINT "author_list_fk" FOREIGN KEY ("author_id") REFERENCES "author_list" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "book_list_fk" FOREIGN KEY ("book_id") REFERENCES "book_list" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION  
);

-- Table "author_book_set"
CREATE TABLE "author_book_set" (
  "author_id" BIGINT NOT NULL,
  "book_id"   BIGINT NOT NULL,
  CONSTRAINT "author_book_set_uk" UNIQUE ("author_id", "book_id"),
  CONSTRAINT "author_set_fk" FOREIGN KEY ("author_id") REFERENCES "author_set" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "book_set_fk" FOREIGN KEY ("book_id") REFERENCES "book_set" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION  
);
