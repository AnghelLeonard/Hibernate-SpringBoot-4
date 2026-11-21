-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Table "orders"
CREATE TABLE "orders" (
  "id"          BIGINT          NOT NULL,
  "amount"      NUMERIC(38,2)   NOT NULL,
  "created_at"  TIMESTAMPTZ(6)  NOT NULL,
  "customer_id" BIGINT          NOT NULL,
  CONSTRAINT "order_pk" PRIMARY KEY ("id")
);


