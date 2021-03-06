-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema library_spring
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `library_spring` DEFAULT CHARACTER SET utf8 ;
USE `library_spring` ;

-- -----------------------------------------------------
-- DROP existing tables
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library_spring`.`user_types`;
DROP TABLE IF EXISTS `library_spring`.`users`;
DROP TABLE IF EXISTS `library_spring`.`authors`;
DROP TABLE IF EXISTS `library_spring`.`publishers`;
DROP TABLE IF EXISTS `library_spring`.`books`;
DROP TABLE IF EXISTS `library_spring`.`order_types`;
DROP TABLE IF EXISTS `library_spring`.`order_statuses`;
DROP TABLE IF EXISTS `library_spring`.`users_books`;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema library_spring
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `library_spring` DEFAULT CHARACTER SET utf8 ;
USE `library_spring` ;


-- -----------------------------------------------------
-- Table `library_spring`.`user_types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`user_types` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `type_UNIQUE` (`type` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`users` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(20) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `first_name` VARCHAR(30) NOT NULL,
  `last_name` VARCHAR(30) NOT NULL,
  `penalty` DOUBLE NOT NULL DEFAULT 0.0,
  `is_blocked` BIT NOT NULL DEFAULT 0,
  `user_types_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  INDEX `fk_users_user_types_idx` (`user_types_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_user_types`
    FOREIGN KEY (`user_types_id`)
    REFERENCES `library_spring`.`user_types` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`publishers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`publishers` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`authors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`authors` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`books` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `quantity` INT NOT NULL,
  `available` INT NOT NULL,
  `release_date` INT NOT NULL,
  `publishers_id` BIGINT NOT NULL,
  `authors_id` BIGINT NOT NULL,
  INDEX `fk_books_publishers1_idx` (`publishers_id` ASC) VISIBLE,
  INDEX `fk_books_authors1_idx` (`authors_id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_books_publishers1`
    FOREIGN KEY (`publishers_id`)
    REFERENCES `library_spring`.`publishers` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_books_authors1`
    FOREIGN KEY (`authors_id`)
    REFERENCES `library_spring`.`authors` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`order_types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`order_types` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `type_UNIQUE` (`type` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`order_statuses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`order_statuses` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `status_UNIQUE` (`status` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`users_books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`users_books` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_date` DATETIME NOT NULL,
  `open_date` DATE NULL,
  `close_date` DATE NULL,
  `return_date` DATE NULL,
  `order_types_id` INT NOT NULL,
  `order_statuses_id` INT NOT NULL,
  `users_id` BIGINT UNSIGNED NOT NULL,
  `books_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_users_books_order_types1_idx` (`order_types_id` ASC) VISIBLE,
  INDEX `fk_users_books_order_statuses1_idx` (`order_statuses_id` ASC) VISIBLE,
  INDEX `fk_users_books_users1_idx` (`users_id` ASC) VISIBLE,
  INDEX `fk_users_books_books1_idx` (`books_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_books_order_types1`
    FOREIGN KEY (`order_types_id`)
    REFERENCES `library_spring`.`order_types` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_users_books_order_statuses1`
    FOREIGN KEY (`order_statuses_id`)
    REFERENCES `library_spring`.`order_statuses` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_users_books_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `library_spring`.`users` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_users_books_books1`
    FOREIGN KEY (`books_id`)
    REFERENCES `library_spring`.`books` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;

DROP TRIGGER IF EXISTS `library_spring`.`books_BEFORE_UPDATE`;

DELIMITER $$
USE `library_spring`$$
CREATE DEFINER = CURRENT_USER TRIGGER `library_spring`.`books_BEFORE_UPDATE` BEFORE UPDATE ON `books` FOR EACH ROW
BEGIN
	IF NEW.available<0 then
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'There is no available book';
END IF;
END$$
DELIMITER ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;




-- -----------------------------------------------------
-- Add information to `library_spring`.`user_types`
-- -----------------------------------------------------
INSERT INTO `library_spring`.`user_types` VALUES 
(DEFAULT, 'admin'),
(DEFAULT, 'librarian'),
(DEFAULT, 'user');

-- -----------------------------------------------------
-- Add information to `library_spring`.`users`
-- -----------------------------------------------------
INSERT INTO `library_spring`.`users` VALUES 
	(DEFAULT, 'admin',
    '$2a$10$nRZz8s8xB2p6R1JWzfSRBO4J2Mj9JkMhpfZKhzWAq0H5y15EMvmIa',
	'????????', '????????????',DEFAULT, DEFAULT, 1),
	(DEFAULT, 'motana',
	'$2a$10$Z3XWV7fUxTT5EsiEl5c0VeEDi2yye6nw11QBgt2HTsk2IT26u93dW',
	'??????????????', '??????????????', DEFAULT, DEFAULT, 2),
	(DEFAULT, 'GruV',
	'$2a$10$j4rElTY6vRsOi8JN7w6T1.UhCHXo5h/vsFGXdhpPn3YvNPE7dtAMa',
     '????????', '??????????????', DEFAULT, DEFAULT, 3);
	
	-- -----------------------------------------------------
-- Add information to `library_spring`.`order_statuses`
-- -----------------------------------------------------
INSERT INTO `library_spring`.`order_statuses` VALUES
(DEFAULT, 'new'),
(DEFAULT, 'ready'),
(DEFAULT, 'open'),
(DEFAULT, 'closed');

-- -----------------------------------------------------
-- Add information to `library_spring`.`order_types`
-- -----------------------------------------------------
INSERT INTO `library_spring`.`order_types` VALUES
(DEFAULT, 'reading room'),
(DEFAULT, 'subscription');

-- -----------------------------------------------------
-- Add demo information to `library_spring`.`publishers`
-- -----------------------------------------------------
INSERT INTO `library_spring`.`publishers` VALUES 
(DEFAULT, '??????????'),
(DEFAULT, '??????????????');

-- -----------------------------------------------------
-- Add demo information to `library`.`authors`
-- -----------------------------------------------------
INSERT INTO `library_spring`.`authors` VALUES 
(DEFAULT, '????????????????', '??????????????'),
(DEFAULT, '??????????', '??????????????'),
(DEFAULT, '????????????', '??????????????');

-- -----------------------------------------------------
-- Add demo information to `library_spring`.`books`
-- -----------------------------------------------------
INSERT INTO `library_spring`.`books` VALUES 
(DEFAULT, '?????????????????????? ????????????', 10, 10, "2000", 1, 1),
(DEFAULT, '?????? ??????????????', 10, 10, "2001", 2, 2),
(DEFAULT, '???????????????? ????????', 10, 10, "2010", 2, 1);

