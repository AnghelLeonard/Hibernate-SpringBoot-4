-- insert authors
INSERT INTO "product" ("id", "name", "stock") VALUES 
  (1, 'Limited Edition Sneakers', 1)
ON CONFLICT (id) DO UPDATE SET stock = 1;
