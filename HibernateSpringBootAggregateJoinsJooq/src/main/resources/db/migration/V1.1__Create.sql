CREATE SCHEMA IF NOT EXISTS bookstoredb;
SET search_path TO bookstoredb;

-- -----------------------------------------------------
-- Table author
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "author" (
  "age"   INT NOT NULL,
  "id"    BIGSERIAL NOT NULL,
  "genre" VARCHAR(255) NULL DEFAULT NULL,
  "name"  VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY ("id")
);


-- -----------------------------------------------------
-- Table tag
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "tag" (
  "id"  BIGSERIAL    NOT NULL,
  "tag" VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY ("id")
);


-- -----------------------------------------------------
-- Table author_tag
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "author_tag" (
  "author_id" BIGINT NOT NULL,
  "tag_id"    BIGINT NOT NULL,
  PRIMARY KEY ("author_id", "tag_id"),
  CONSTRAINT FK2o47m42wm7uslsmd9kfk2qu2a
    FOREIGN KEY ("author_id") REFERENCES "author" ("id"),
  CONSTRAINT FK7u4elvrjbqr9ydtmggje64sva
    FOREIGN KEY ("tag_id") REFERENCES "tag" ("id")
);

CREATE INDEX IF NOT EXISTS "FK7u4elvrjbqr9ydtmggje64sva_idx" ON "author_tag" ("tag_id" ASC);

-- -----------------------------------------------------
-- Table publisher
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "publisher" (
  "id"      BIGSERIAL NOT NULL,
  "address" VARCHAR(255) NULL DEFAULT NULL,
  "company" VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY ("id")
);

-- -----------------------------------------------------
-- Table book
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "book" (
  "author_id"    BIGINT NULL DEFAULT NULL,
  "id"           BIGSERIAL NOT NULL,
  "publisher_id" BIGINT NULL DEFAULT NULL,
  "isbn"         VARCHAR(255) NULL DEFAULT NULL,
  "title"        VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY ("id"),
  CONSTRAINT "FKgtvt7p649s4x80y6f4842pnfq"
    FOREIGN KEY ("publisher_id") REFERENCES publisher ("id"),
  CONSTRAINT "FKklnrv3weler2ftkweewlky958"
    FOREIGN KEY ("author_id") REFERENCES "author" ("id")
);

CREATE INDEX IF NOT EXISTS "FKklnrv3weler2ftkweewlky958_idx" ON "book" ("author_id" ASC);
CREATE INDEX IF NOT EXISTS "FKgtvt7p649s4x80y6f4842pnfq_idx" ON "book" ("publisher_id" ASC);

-- -----------------------------------------------------
-- Table review
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "review" (
  "book_id"  BIGINT NULL DEFAULT NULL,
  "id"       BIGSERIAL NOT NULL,
  "language" VARCHAR(255) NULL DEFAULT NULL,
  "script"   VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY ("id"),
  CONSTRAINT "FK70yrt09r4r54tcgkrwbeqenbs"
    FOREIGN KEY ("book_id") REFERENCES "book" ("id")
);

CREATE INDEX IF NOT EXISTS "FK70yrt09r4r54tcgkrwbeqenbs_idx" ON "review" ("book_id" ASC);

-- -----------------------------------------------------
-- Table reviewer
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "reviewer" (
  "reviewer_age"  INT NOT NULL,
  "id"            BIGSERIAL NOT NULL,
  "reviewer_name" VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY ("id")
);


-- -----------------------------------------------------
-- Table review_reviewer
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "review_reviewer" (
  "review_id"   BIGINT NOT NULL,
  "reviewer_id" BIGINT NOT NULL,
  PRIMARY KEY ("review_id", "reviewer_id"),
  CONSTRAINT "FKfmsm54dlkff3gplylr3ujjuy1"
    FOREIGN KEY ("review_id") REFERENCES "review" ("id"),
  CONSTRAINT "FKpuvpkaucyj6pmrmi2f6xs2qxs"
    FOREIGN KEY ("reviewer_id") REFERENCES "reviewer" ("id")
);

CREATE INDEX IF NOT EXISTS "FKpuvpkaucyj6pmrmi2f6xs2qxs_idx" ON "review_reviewer" ("reviewer_id" ASC);
