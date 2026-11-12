-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Create the "ints" table 
CREATE TABLE "ints" (
  "nr" INTEGER DEFAULT NULL
);