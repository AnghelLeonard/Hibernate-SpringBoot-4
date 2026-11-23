-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table tax_rate
CREATE TABLE IF NOT EXISTS "tax_rate" (
    "country_code" VARCHAR(10)    PRIMARY KEY,
    "percentage"   NUMERIC(10, 4) NOT NULL
);

-- Table invoice
CREATE TABLE IF NOT EXISTS "invoice" (
    "id"           BIGINT        PRIMARY KEY,
    "country_code" VARCHAR(10)    NOT NULL,
    "net_amount"   NUMERIC(19, 2) NOT NULL,
    "tax_amount"   NUMERIC(19, 2) NOT NULL,
    "total_amount" NUMERIC(19, 2) NOT NULL,
    CONSTRAINT "fk_invoice_country"
        FOREIGN KEY ("country_code") REFERENCES "tax_rate" ("country_code")
);