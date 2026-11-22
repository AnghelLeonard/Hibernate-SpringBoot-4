-- insert inventory
INSERT INTO "inventory" ("id", "sku", "available_quantity") VALUES 
  (1, 'BOOK-978-0134685991', 5)
ON CONFLICT ("id") DO NOTHING;