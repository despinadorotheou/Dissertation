REPLACE INTO role VALUES (1,'ADMIN');
REPLACE INTO role VALUES (2,'USER');
REPLACE INTO user VALUES (1, 1,'dd186@student.le.ac.uk','Dorotheou','Despina','$2a$10$P6ENyTd74quDcQSsKiAqV.U3hcsvn.d/79pw3UyRvjLKCdechChyq');
REPLACE INTO user_role VALUES (1,1);
REPLACE INTO user_role VALUES (1,2);
REPLACE INTO category VALUES (1,'Drinks');
REPLACE INTO category VALUES (2, 'Sandwiches');
REPLACE INTO category VALUES (3, 'Snacks');
REPLACE INTO category VALUES (4, 'Coffee');
REPLACE INTO product(productId, product_name, description, price, quantity,preference, ingredients ) VALUES (1,'Buxton Still Natural Mineral Water Bottle','500ml bottle', 0.75, 50, 'Vegan', 'water');
REPLACE INTO product_category VALUES (1,1);

