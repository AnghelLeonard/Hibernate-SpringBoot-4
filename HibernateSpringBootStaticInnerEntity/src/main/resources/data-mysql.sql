-- insert orders
INSERT INTO `orders` (`customer_name`) VALUES ('John Doe');
INSERT INTO `orders` (`customer_name`) VALUES ('Jane Smith');

-- insert order_lines
INSERT INTO `order_lines` (`order_id`, `isbn`, `quantity`, `price`) 
VALUES (1, '9780134685991', 1, 45.99); 
INSERT INTO `order_lines` (`order_id`, `isbn`, `quantity`, `price`) 
VALUES (1, '9781789801415', 2, 39.50); 
INSERT INTO `order_lines` (`order_id`, `isbn`, `quantity`, `price`) 
VALUES (2, '9780132350884', 1, 42.00); 