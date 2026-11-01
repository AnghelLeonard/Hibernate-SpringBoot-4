-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Create the "lots" table 
CREATE TABLE "lots" (
  "lot" jsonb DEFAULT NULL
);