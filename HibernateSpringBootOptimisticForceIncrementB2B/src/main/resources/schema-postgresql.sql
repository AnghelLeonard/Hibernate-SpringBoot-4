-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table "invoice"
CREATE TABLE "invoice" (
    "id"           BIGINT         NOT NULL,
    "credit_limit" NUMERIC(19, 2) NOT NULL,
    "version"      INTEGER        NOT NULL DEFAULT 0,
    CONSTRAINT "invoice_pk" PRIMARY KEY ("id")
);

-- Table "invoice_item"
CREATE TABLE "invoice_item" (
    "id"          BIGINT         NOT NULL,
    "invoice_id"  BIGINT         NOT NULL,
    "description" VARCHAR(255)   NOT NULL,
    "amount"      NUMERIC(19, 2) NOT NULL,
    CONSTRAINT "invoice_item_pk" PRIMARY KEY ("id"),
    CONSTRAINT "invoice_item_invoice_fk" FOREIGN KEY ("invoice_id")
        REFERENCES "invoice" ("id")
);


