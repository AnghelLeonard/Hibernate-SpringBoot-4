-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table product
CREATE TABLE IF NOT EXISTS "product" (
    "id"    BIGINT       PRIMARY KEY,
    "name"  VARCHAR(100) NOT NULL,
    "stock" INT          NOT NULL CHECK (stock >= 0)
);