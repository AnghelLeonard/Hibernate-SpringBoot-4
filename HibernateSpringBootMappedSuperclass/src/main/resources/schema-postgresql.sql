-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table "author"
CREATE TABLE "author" (
  "id"    BIGINT        NOT NULL,
  "age"   INT           NOT NULL,
  "genre" VARCHAR(255)  DEFAULT NULL,
  "name"  VARCHAR(255)  DEFAULT NULL,
  CONSTRAINT "author_pk" PRIMARY KEY ("id")
);

-- Table "ebook"
CREATE TABLE "ebook" (  
  "id"        BIGINT        NOT NULL,
  "author_id" BIGINT        NOT NULL,
  "format"    VARCHAR(255)  DEFAULT NULL,  
  "isbn"      VARCHAR(255)  DEFAULT NULL,  
  "title"     VARCHAR(255)  DEFAULT NULL,  
  CONSTRAINT "ebook_pk" PRIMARY KEY ("id"),
  CONSTRAINT "ebook_author_fk" FOREIGN KEY ("author_id") REFERENCES "author" ("id")
);

-- Table "paperback"
CREATE TABLE "paperback" (  
  "id"          BIGINT        NOT NULL,
  "author_id"   BIGINT        NOT NULL,
  "isbn"        VARCHAR(255)  DEFAULT NULL,  
  "size_in"     VARCHAR(255)  DEFAULT NULL,  
  "title"       VARCHAR(255)  DEFAULT NULL,  
  "weight_lbs"  VARCHAR(255)  DEFAULT NULL,  
  CONSTRAINT "paperback_pk" PRIMARY KEY ("id"),
  CONSTRAINT "paperback_author_fk" FOREIGN KEY ("author_id") REFERENCES "author" ("id")
);

 