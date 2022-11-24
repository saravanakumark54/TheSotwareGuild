DROP DATABASE IF EXISTS superherodbtest;

CREATE DATABASE superherodbtest;

USE superherodbtest;
  
CREATE TABLE Power(
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL
  );

CREATE TABLE Super(
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(30) NOT NULL,
  description VARCHAR(150) NULL,
  imagePath VARCHAR(200) NULL,
  powerId INT NULL,
  CONSTRAINT fk_Super_Power
    FOREIGN KEY (powerId)
    REFERENCES Power(id)
);

CREATE TABLE Location(
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  description VARCHAR(150) NULL,
  address VARCHAR(150) NOT NULL,
  latitude VARCHAR(10) NOT NULL,
  longitude VARCHAR(10) NOT NULL
  );

CREATE TABLE Organization(
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(150) NULL,
  address VARCHAR(150) NULL,
  contact VARCHAR(20) NULL
  );

CREATE TABLE Sighting(
	id INT PRIMARY KEY AUTO_INCREMENT,
	superId INT NOT NULL,
	locationId INT NOT NULL,
	date DATETIME NOT NULL,
	CONSTRAINT fk_Super_Location
    FOREIGN KEY (superId)
    REFERENCES Super(id),
  CONSTRAINT fk_Location_Super
    FOREIGN KEY (locationId)
    REFERENCES Location(id)
);

CREATE TABLE OrganizationMember(
  organizationId INT NOT NULL,
  superId INT NOT NULL,
  PRIMARY KEY (organizationId, superId),
  CONSTRAINT fk_Organization_Super
    FOREIGN KEY (organizationId)
    REFERENCES Organization(id),
  CONSTRAINT fk_Super_Organization
    FOREIGN KEY (superId)
    REFERENCES Super(id)
);