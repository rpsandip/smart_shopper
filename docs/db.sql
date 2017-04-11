CREATE SCHEMA `shop` ;

CREATE TABLE `shop`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(45) NULL,
  `parentid` VARCHAR(45) NOT NULL,
  `referralcode` VARCHAR(45) NOT NULL,
  `createdon` DATETIME NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `shop`.`product_category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  
 CREATE TABLE `shop`.`product_details` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NULL,
  `price` VARCHAR(45) NOT NULL,
  `details` VARCHAR(45) NULL,
  `points` VARCHAR(45) NULL,
  `categoryid` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `categoryid_idx` (`categoryid` ASC),
  CONSTRAINT `categoryid`
    FOREIGN KEY (`categoryid`)
    REFERENCES `shop`.`product_category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

  
  