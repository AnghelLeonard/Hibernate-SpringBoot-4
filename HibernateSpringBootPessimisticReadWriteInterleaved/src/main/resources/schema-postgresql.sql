-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table department_budget
CREATE TABLE IF NOT EXISTS "department_budget" (
    "dept_code"          VARCHAR(20)    PRIMARY KEY,
    "max_transfer_limit" NUMERIC(19, 2) NOT NULL
);

-- Table employee_wallet
CREATE TABLE IF NOT EXISTS "employee_wallet" (
    "employee_id" VARCHAR(50)    PRIMARY KEY,
    "dept_code"   VARCHAR(20)    NOT NULL REFERENCES "department_budget" ("dept_code"),
    "balance"     NUMERIC(19, 2) NOT NULL CHECK ("balance" >= 0)
);