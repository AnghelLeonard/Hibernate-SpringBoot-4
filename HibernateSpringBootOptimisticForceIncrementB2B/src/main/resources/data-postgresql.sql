-- insert an initial invoice with ID 42 and a $1000 credit limit
INSERT INTO "invoice" ("id", "credit_limit", "version") VALUES 
  (42, 1000.00, 0)
ON CONFLICT ("id") DO NOTHING;