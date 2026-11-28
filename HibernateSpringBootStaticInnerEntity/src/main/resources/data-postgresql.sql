-- insert orders
INSERT INTO "orders" ("customer_name", "id") VALUES ('John Doe', 1);
INSERT INTO "orders" ("customer_name", "id") VALUES ('Jane Smith', 2);

-- insert order_lines
INSERT INTO "order_lines" ("order_id", "isbn", "quantity", "price") 
VALUES (1, '9780134685991', 1, 45.99);

INSERT INTO "order_lines" ("order_id", "isbn", "quantity", "price") 
VALUES (1, '9781789801415', 2, 39.50);

INSERT INTO "order_lines" ("order_id", "isbn", "quantity", "price") 
VALUES (2, '9780132350884', 1, 42.00);
