-- Indexes for primary keys have been explicitly created.

DROP TABLE Product;
DROP TABLE Image;
DROP TABLE ProductCategory;
DROP TABLE FavouriteAddress;
DROP TABLE CompanyAddress;
DROP TABLE Company;
DROP TABLE CompanyCategory;
DROP TABLE Address;
DROP TABLE City;
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

CREATE TABLE City (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) COLLATE latin1_bin NOT NULL,
    CONSTRAINT CityPK PRIMARY KEY (id)

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
    imageId BIGINT NOT NULL,
    CONSTRAINT ProductIdPK PRIMARY KEY (id),
    CONSTRAINT ProductCompanyIdFK FOREIGN KEY(companyId)
        REFERENCES Company (id),
    CONSTRAINT ProductCategoryIdFK FOREIGN KEY(productCategoryId)
        REFERENCES ProductCategory (id),
    CONSTRAINT ProductImageIdFK FOREIGN KEY(imageId)
    	REFERENCES Image (id)
    
    
) ENGINE = InnoDB;

CREATE INDEX ProductIndexByName ON Product (name);

