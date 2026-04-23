-- insert shopping_carts
INSERT INTO `shopping_cart` (`owner`) VALUES ("Mark Juno");

-- insert shopping_cart_books
INSERT INTO `shopping_cart_books` (`shopping_cart_id`, `genre`, `isbn`, `price`, `title`) VALUES 
  (1, "History", "001-JN", 35, "A History of Ancient Prague"),
  (1, "Horror", "001-OG", 55, "Carrie"),
  (1, "Anthology", "001-MJ", 45, "The Beatles Anthology");