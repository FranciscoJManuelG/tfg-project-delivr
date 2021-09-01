-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-- in the "pa-project" database.
-------------------------------------------------------------------------------
INSERT INTO CompanyCategory (name) VALUES ('Tradicional');
INSERT INTO CompanyCategory (name) VALUES ('Chino');
INSERT INTO CompanyCategory (name) VALUES ('Turco');
INSERT INTO CompanyCategory (name) VALUES ('Vegano');

INSERT INTO Province (name) VALUES ('A Coruña');
INSERT INTO Province (name) VALUES ('Álava');
INSERT INTO Province (name) VALUES ('Albacete');
INSERT INTO Province (name) VALUES ('Alicante');
INSERT INTO Province (name) VALUES ('Almería');
INSERT INTO Province (name) VALUES ('Asturias');
INSERT INTO Province (name) VALUES ('Ávila');
INSERT INTO Province (name) VALUES ('Badajoz');
INSERT INTO Province (name) VALUES ('Baleares');
INSERT INTO Province (name) VALUES ('Barcelona');
INSERT INTO Province (name) VALUES ('Burgos');
INSERT INTO Province (name) VALUES ('Cáceres');
INSERT INTO Province (name) VALUES ('Cádiz');
INSERT INTO Province (name) VALUES ('Cantabria');
INSERT INTO Province (name) VALUES ('Castellón');
INSERT INTO Province (name) VALUES ('Ciudad Real');
INSERT INTO Province (name) VALUES ('Córdoba');
INSERT INTO Province (name) VALUES ('Cuenca');
INSERT INTO Province (name) VALUES ('Girona');
INSERT INTO Province (name) VALUES ('Granada');
INSERT INTO Province (name) VALUES ('Guadalajara');
INSERT INTO Province (name) VALUES ('Gipuzkoa');
INSERT INTO Province (name) VALUES ('Huelva');
INSERT INTO Province (name) VALUES ('Huesca');
INSERT INTO Province (name) VALUES ('Jaén');
INSERT INTO Province (name) VALUES ('La Rioja');
INSERT INTO Province (name) VALUES ('Las Palmas');
INSERT INTO Province (name) VALUES ('León');
INSERT INTO Province (name) VALUES ('Lérida');
INSERT INTO Province (name) VALUES ('Lugo');
INSERT INTO Province (name) VALUES ('Madrid');
INSERT INTO Province (name) VALUES ('Málaga');
INSERT INTO Province (name) VALUES ('Murcia');
INSERT INTO Province (name) VALUES ('Navarra');
INSERT INTO Province (name) VALUES ('Ourense');
INSERT INTO Province (name) VALUES ('Palencia');
INSERT INTO Province (name) VALUES ('Pontevedra');
INSERT INTO Province (name) VALUES ('Salamanca');
INSERT INTO Province (name) VALUES ('Segovia');
INSERT INTO Province (name) VALUES ('Sevilla');
INSERT INTO Province (name) VALUES ('Soria');
INSERT INTO Province (name) VALUES ('Tarragona');
INSERT INTO Province (name) VALUES ('Santa Cruz de Tenerife');
INSERT INTO Province (name) VALUES ('Teruel');
INSERT INTO Province (name) VALUES ('Toledo');
INSERT INTO Province (name) VALUES ('Valencia');
INSERT INTO Province (name) VALUES ('Valladolid');
INSERT INTO Province (name) VALUES ('Vizcaya');
INSERT INTO Province (name) VALUES ('Zamora');
INSERT INTO Province (name) VALUES ('Zaragoza');
INSERT INTO Province (name) VALUES ('Ceuta');
INSERT INTO Province (name) VALUES ('Melilla');

INSERT INTO City (name, provinceId) VALUES ('A Coruña', 1);
INSERT INTO City (name, provinceId) VALUES ('Lugo', 30);
INSERT INTO City (name, provinceId) VALUES ('Guitiriz', 30);
INSERT INTO City (name, provinceId) VALUES ('Simancas', 47);
INSERT INTO City (name, provinceId) VALUES ('Sevilla', 40);
INSERT INTO City (name, provinceId) VALUES ('Ferrol', 1);

INSERT INTO User (userName, password, firstName, lastName, email, phone, role, globalBalance, renewDate, feePaid)
	VALUES ('client', '$2a$10$aZ7xTAvxVTqnrdbeXB.HJujBgVfjeCzvPikU/YyFYxtXUOSgMu67i', 'name', 'lastname', 'client@client.com', 132456798, 0, 0.00, '2021-10-10',true); 
INSERT INTO User (userName, password, firstName, lastName, email, phone, role, globalBalance, renewDate, feePaid)
	VALUES ('business', '$2a$10$B6DS8oiZ0fUXNC7HlNRe0.0FpNTWstct2SEujaJYX83Abupq6ptOO', 'nameb', 'lastnameb', 'business@business.com', 345176824, 1, 0.00, '2021-10-10',true); 
INSERT INTO User (userName, password, firstName, lastName, email, phone, role, globalBalance, renewDate, feePaid)
	VALUES ('admin', '$2a$10$B6DS8oiZ0fUXNC7HlNRe0.0FpNTWstct2SEujaJYX83Abupq6ptOO', 'admin', 'admin', 'admin@admin.com', 345176824, 2, 0.00, '2021-10-10',true); 
	
INSERT INTO Company (name, capacity, reserve, homeSale, reservePercentage, openingTime, closingTime, lunchTime, dinerTime, userId, companyCategoryId, block) 
	VALUES ('Delivr', 25, true, true, 20, TIME('10:00'), TIME('23:00'), TIME('14:00'), TIME('21:00'), 2, 3, false);
	
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
	
INSERT INTO ShoppingCart(userId, homeSale)
	VALUES (1, false);
INSERT INTO ShoppingCart(userId, homeSale)
	VALUES (2, false);
INSERT INTO ShoppingCart(userId, homeSale)
	VALUES (3, false);

INSERT INTO Menu(userId)
	VALUES (1);
INSERT INTO Menu(userId)
	VALUES (2);
INSERT INTO Menu(userId)
	VALUES (3);
	
INSERT INTO GoalType(goalName)
    VALUES ("Numero de pedidos");

	




