-- Indexes for primary keys have been explicitly created.

DROP TABLE DiscountTicket;
DROP TABLE Goal;
DROP TABLE GoalType;
DROP TABLE OrderItem;
DROP TABLE OrderTable;
DROP TABLE ShoppingCartItem;
DROP TABLE ShoppingCart;
DROP TABLE Product;
DROP TABLE Image;
DROP TABLE ProductCategory;
DROP TABLE FavouriteAddress;
DROP TABLE CompanyAddress;
DROP TABLE Company;
DROP TABLE CompanyCategory;
DROP TABLE Address;
DROP TABLE City;
DROP TABLE Province;
DROP TABLE User;

CREATE TABLE User (
    id BIGINT NOT NULL AUTO_INCREMENT,
    userName VARCHAR(60) COLLATE latin1_bin NOT NULL,
    password VARCHAR(60) NOT NULL, 
    firstName VARCHAR(60) NOT NULL,
    lastName VARCHAR(60) NOT NULL, 
    email VARCHAR(60) NOT NULL,
    phone VARCHAR(60) NOT NULL,
    role TINYINT NOT NULL,
    CONSTRAINT UserPK PRIMARY KEY (id),
    CONSTRAINT UserNameUniqueKey UNIQUE (userName)
) ENGINE = InnoDB;

CREATE INDEX UserIndexByUserName ON User (userName);


CREATE TABLE Province (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) COLLATE latin1_bin NOT NULL,
    CONSTRAINT ProvincePK PRIMARY KEY (id)

) ENGINE = InnoDB;

CREATE INDEX ProvinceIndexByName ON Province (name);

CREATE TABLE City (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) COLLATE latin1_bin NOT NULL,
    provinceId BIGINT NOT NULL,
    
    CONSTRAINT CityPK PRIMARY KEY (id),
    CONSTRAINT CityProvinceIdFK FOREIGN KEY(provinceId)
        REFERENCES Province (id)

) ENGINE = InnoDB;

CREATE INDEX CityIndexByName ON City (name);

CREATE TABLE Address(
    id BIGINT NOT NULL AUTO_INCREMENT,
    street VARCHAR(60) COLLATE latin1_bin NOT NULL,
    cp VARCHAR(20) NOT NULL,
    cityId BIGINT NOT NULL,
    
    CONSTRAINT AddressPK PRIMARY KEY (id),
    CONSTRAINT AddressCityIdFK FOREIGN KEY(cityId)
        REFERENCES City (id)
    
) ENGINE = InnoDB;

CREATE INDEX AddressIndexByStreet ON Address (street);

CREATE TABLE CompanyCategory (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    CONSTRAINT CompanyCategoryPK PRIMARY KEY (id),
    CONSTRAINT CompanyCategoryNameUniqueKey UNIQUE (name)
) ENGINE = InnoDB;

CREATE INDEX CompanyCategoryIndexByName ON CompanyCategory (name);


CREATE TABLE Company(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) COLLATE latin1_bin NOT NULL,
    capacity SMALLINT NOT NULL,
    reserve BOOLEAN NOT NULL,
    homeSale BOOLEAN NOT NULL,
    reservePercentage SMALLINT NOT NULL,
    userId BIGINT NOT NULL,
    companyCategoryId BIGINT NOT NULL,
    block BOOLEAN NOT NULL,
    CONSTRAINT CompanyIdPK PRIMARY KEY (id),
    CONSTRAINT CompanyUserIdFK FOREIGN KEY(userId)
        REFERENCES User (id),
    CONSTRAINT CompanyCategoryIdFK FOREIGN KEY(companyCategoryId)
        REFERENCES CompanyCategory (id)
    
    
) ENGINE = InnoDB;

CREATE INDEX CompanyIndexByName ON Company (name);

CREATE TABLE CompanyAddress(
    companyId BIGINT NOT NULL,
    addressId BIGINT NOT NULL,
    CONSTRAINT CompanyAddressCompanyIdFK FOREIGN KEY(companyId)
        REFERENCES Company (id),
    CONSTRAINT CompanyAddressAddressIdFK FOREIGN KEY(addressId)
        REFERENCES Address (id)
    
) ENGINE = InnoDB;

CREATE TABLE FavouriteAddress(
    userId BIGINT NOT NULL,
    addressId BIGINT NOT NULL,
    CONSTRAINT FavouriteAddressUserIdFK FOREIGN KEY(userId)
        REFERENCES User (id),
    CONSTRAINT FavouriteAddressAddressIdFK FOREIGN KEY(addressId)
        REFERENCES Address (id)
    
) ENGINE = InnoDB;

CREATE TABLE ProductCategory (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    CONSTRAINT ProductCategoryPK PRIMARY KEY (id),
    CONSTRAINT ProductCategoryNameUniqueKey UNIQUE (name)
) ENGINE = InnoDB;

CREATE INDEX ProductCategoryIndexByName ON ProductCategory (name);

CREATE TABLE Image(
    id BIGINT NOT NULL AUTO_INCREMENT,
    path VARCHAR(200) COLLATE latin1_bin NOT NULL,
    
    CONSTRAINT ImagePK PRIMARY KEY (id),
    CONSTRAINT ImagePathUniqueKey UNIQUE (path)
) ENGINE = InnoDB;

CREATE INDEX ImageIndexByPath ON Image (path);

CREATE TABLE Product(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) COLLATE latin1_bin NOT NULL,
    description VARCHAR(200) COLLATE latin1_bin,
    price DECIMAL(11, 2) NOT NULL,
    block BOOLEAN NOT NULL,
    companyId BIGINT NOT NULL,
    productCategoryId BIGINT NOT NULL,
    imageId BIGINT,
    CONSTRAINT ProductIdPK PRIMARY KEY (id),
    CONSTRAINT ProductCompanyIdFK FOREIGN KEY(companyId)
        REFERENCES Company (id),
    CONSTRAINT ProductCategoryIdFK FOREIGN KEY(productCategoryId)
        REFERENCES ProductCategory (id),
    CONSTRAINT ProductImageIdFK FOREIGN KEY(imageId)
    	REFERENCES Image (id)
    
    
) ENGINE = InnoDB;

CREATE INDEX ProductIndexByName ON Product (name);

CREATE TABLE ShoppingCart (
    id BIGINT NOT NULL AUTO_INCREMENT,
    homeSale BOOLEAN NOT NULL,
    userId BIGINT NOT NULL,
    CONSTRAINT ShoppingCartPK PRIMARY KEY (id),
    CONSTRAINT ShoppingCartUserIdFK FOREIGN KEY(userId)
        REFERENCES User (id)
) ENGINE = InnoDB;

CREATE TABLE ShoppingCartItem (
    id BIGINT NOT NULL AUTO_INCREMENT,
    productId BIGINT NOT NULL,
    quantity SMALLINT NOT NULL,
    shoppingCartId BIGINT NOT NULL,
    CONSTRAINT ShoppingCartItemPK PRIMARY KEY (id),
    CONSTRAINT ShoppingCartItemProductIdFK FOREIGN KEY(productId)
        REFERENCES Product (id),
    CONSTRAINT ShoppingCartItemShoppingCartIdFK FOREIGN KEY(shoppingCartId)
        REFERENCES ShoppingCart (id)
) ENGINE = InnoDB;

CREATE TABLE OrderTable (
    id BIGINT NOT NULL AUTO_INCREMENT,
    userId BIGINT NOT NULL,
    companyId BIGINT NOT NULL,
    date DATETIME NOT NULL,
    homeSale BOOLEAN NOT NULL,
    street VARCHAR(200) NOT NULL,
    cp VARCHAR(20) NOT NULL,
    totalPrice DECIMAL(11, 2) NOT NULL,
    CONSTRAINT OrderPK PRIMARY KEY (id),
    CONSTRAINT OrderUserIdFK FOREIGN KEY(userId)
        REFERENCES User (id),
    CONSTRAINT OrderCompanyIdFK FOREIGN KEY(companyId)
        REFERENCES Company (id)
) ENGINE = InnoDB;

CREATE TABLE OrderItem (
    id BIGINT NOT NULL AUTO_INCREMENT,
    productId BIGINT NOT NULL,
    productPrice DECIMAL(11, 2) NOT NULL,
    quantity SMALLINT NOT NULL,
    orderId BIGINT NOT NULL,
    CONSTRAINT OrderItemPK PRIMARY KEY (id),
    CONSTRAINT OrderItemProductIdFK FOREIGN KEY(productId)
        REFERENCES Product (id),
    CONSTRAINT OrderItemOrderIdFK FOREIGN KEY(orderId)
        REFERENCES OrderTable (id)
) ENGINE = InnoDB;

CREATE TABLE GoalType (
    id BIGINT NOT NULL AUTO_INCREMENT,
    goalName VARCHAR(60) NOT NULL,
    CONSTRAINT GoalTypePK PRIMARY KEY (id),
    CONSTRAINT GoalTypeGoalNameUniqueKey UNIQUE (goalName)
) ENGINE = InnoDB;

CREATE INDEX GoalTypeIndexByGoalName ON GoalType (goalName);

CREATE TABLE Goal (
    id BIGINT NOT NULL AUTO_INCREMENT,
    discountCash DECIMAL(11, 2),
    discountPercentage SMALLINT,
    goalQuantity SMALLINT NOT NULL, 
    companyId BIGINT NOT NULL,
    goalTypeId BIGINT NOT NULL,
    CONSTRAINT GoalPK PRIMARY KEY (id),
    CONSTRAINT GoalCompanyIdFK FOREIGN KEY(companyId)
        REFERENCES Company (id),
    CONSTRAINT DiscountTickeGoalTypeIdFK FOREIGN KEY(goalTypeId)
        REFERENCES GoalType (id)
) ENGINE = InnoDB;

CREATE TABLE DiscountTicket (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(60) COLLATE latin1_bin NOT NULL,
    expirationDate DATETIME NOT NULL, 
    discountType TINYINT NOT NULL,
    userId BIGINT NOT NULL, 
    goalId BIGINT NOT NULL,
    orderId BIGINT,
    used BOOLEAN NOT NULL,
    CONSTRAINT DiscountTicketPK PRIMARY KEY (id),
    CONSTRAINT DiscountTicketCodeUniqueKey UNIQUE (code),
    CONSTRAINT DiscountTicketUserIdFK FOREIGN KEY(userId)
        REFERENCES User (id),
    CONSTRAINT DiscountTickeGoalIdFK FOREIGN KEY(goalId)
        REFERENCES Goal (id),
    CONSTRAINT DiscountTicketOrderIdFK FOREIGN KEY(orderId)
        REFERENCES OrderTable (id)
) ENGINE = InnoDB;

CREATE INDEX DiscountTicketIndexByCode ON DiscountTicket (code);



