-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-- in the "pa-project" database.
-------------------------------------------------------------------------------
INSERT INTO CompanyCategory (name) VALUES ('Tradicional');
INSERT INTO CompanyCategory (name) VALUES ('Chino');
INSERT INTO CompanyCategory (name) VALUES ('Turco');
INSERT INTO CompanyCategory (name) VALUES ('Vegano');

INSERT INTO City (name) VALUES ('A Coruña');
INSERT INTO City (name) VALUES ('Lugo');
INSERT INTO City (name) VALUES ('Guitiriz');
INSERT INTO City (name) VALUES ('Simancas');
INSERT INTO City (name) VALUES ('Sevilla');
INSERT INTO City (name) VALUES ('Ferrol');

INSERT INTO User (userName, password, firstName, lastName, email, phone, role)
	VALUES ('client', '$2a$10$aZ7xTAvxVTqnrdbeXB.HJujBgVfjeCzvPikU/YyFYxtXUOSgMu67i', 'name', 'lastname', 'client@client.com', 132456798, 0); 
INSERT INTO User (userName, password, firstName, lastName, email, phone, role)
	VALUES ('business', '$2a$10$B6DS8oiZ0fUXNC7HlNRe0.0FpNTWstct2SEujaJYX83Abupq6ptOO', 'nameb', 'lastnameb', 'business@business.com', 345176824, 1); 
	
INSERT INTO Company (name, capacity, reserve, homeSale, reservePercentage, userId, companyCategoryId, block)
	VALUES ('Delivr', 25, true, true, 20, 2, 3, false);
	
INSERT INTO Address (street, cp, cityId)
	VALUES ('Rosalia 23', 15400, 1);
INSERT INTO Address (street, cp, cityId)
	VALUES ('Castelao 56', 15400, 1);
	
INSERT INTO CompanyAddress (companyId, addressId)
	VALUES (1, 1);
INSERT INTO CompanyAddress (companyId, addressId)
	VALUES (1, 2);

INSERT INTO ProductCategory (name) VALUES ("Bocadillos");
INSERT INTO ProductCategory (name) VALUES ("Hamburguesas");
INSERT INTO ProductCategory (name) VALUES ("Ensaladas");
INSERT INTO ProductCategory (name) VALUES ("Kebab");
INSERT INTO ProductCategory (name) VALUES ("Sushi");
INSERT INTO ProductCategory (name) VALUES ("Menú");

INSERT INTO Image (path) VALUES ('/img/bocataTortilla.jpg');
INSERT INTO Image (path) VALUES ('/img/burger.jpg');

INSERT INTO Product (name, description, price, block, companyId, productCategoryId, imageId)
	Values("Bocadillo de tortilla", "Tortilla con cebolla", 2.50, false, 1, 1, 1);

INSERT INTO Product (name, description, price, block, companyId, productCategoryId, imageId)
	Values("Hamburguesa completa", "carne de ternera, lechuga, tomate y cebolla", 4.60, false, 1, 2, 2);
	
INSERT INTO ShoppingCart(userId)
	VALUES (1);
INSERT INTO ShoppingCart(userId)
	VALUES (2);

	




