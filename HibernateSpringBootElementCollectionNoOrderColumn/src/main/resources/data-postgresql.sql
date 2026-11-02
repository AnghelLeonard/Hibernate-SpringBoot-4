-- insert shopping_carts
INSERT INTO "shopping_cart" ("id", "owner") VALUES (1, 'Mark Juno');

-- insert shopping_cart_books
INSERT INTO "shopping_cart_books" ("shopping_cart_id", "title") VALUES 
  (1, 'A History of Ancient Prague'),
  (1, 'Carrie'),
  (1, 'The Beatles Anthology');