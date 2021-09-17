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


-- -----------------------------------------------------
-- Table `library_spring`.`user_types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`user_types` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `type_UNIQUE` (`type` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`users` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(20) NOT NULL,
  `password` BINARY(32) NOT NULL,
  `salt` BINARY(32) NOT NULL,
  `first_name` VARCHAR(30) NOT NULL,
  `last_name` VARCHAR(30) NOT NULL,
  `penalty` DECIMAL(8,2) NULL DEFAULT 0,
  `is_blocked` TINYINT NOT NULL DEFAULT 0,
  `user_type_id` INT UNSIGNED NOT NULL DEFAULT 3,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_user_user_type_idx` (`user_type_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_user_type`
    FOREIGN KEY (`user_type_id`)
    REFERENCES `library_spring`.`user_types` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`authors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`authors` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`publishers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`publishers` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`books` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `quantity` INT UNSIGNED NOT NULL,
  `available` INT UNSIGNED NOT NULL,
  `author_id` INT UNSIGNED NOT NULL,
  `publisher_id` INT UNSIGNED NOT NULL,
  `release_date` SMALLINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_book_author1_idx` (`author_id` ASC) VISIBLE,
  INDEX `fk_book_publisher1_idx` (`publisher_id` ASC) VISIBLE,
  CONSTRAINT `fk_book_author1`
    FOREIGN KEY (`author_id`)
    REFERENCES `library_spring`.`authors` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_book_publisher1`
    FOREIGN KEY (`publisher_id`)
    REFERENCES `library_spring`.`publishers` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`order_types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`order_types` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `type_UNIQUE` (`type` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library_spring`.`order_statuses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library_spring`.`order_statuses` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
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
  `order_types_id` INT UNSIGNED NOT NULL,
  `order_statuses_id` INT UNSIGNED NOT NULL,
  `users_id` BIGINT UNSIGNED NOT NULL,
  `books_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_users_books_order_types1_idx` (`order_types_id` ASC) VISIBLE,
  INDEX `fk_users_books_order_statuses1_idx` (`order_statuses_id` ASC) VISIBLE,
  INDEX `fk_users_books_users1_idx` (`users_id` ASC) VISIBLE,
  INDEX `fk_users_books_books1_idx` (`books_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_books_order_types1`
    FOREIGN KEY (`order_types_id`)
    REFERENCES `library_spring`.`order_types` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_books_order_statuses1`
    FOREIGN KEY (`order_statuses_id`)
    REFERENCES `library_spring`.`order_statuses` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_books_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `library_spring`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_books_books1`
    FOREIGN KEY (`books_id`)
    REFERENCES `library_spring`.`books` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


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
	UNHEX('FA271CA06E188F1419A57ADCD088B827A6E07345D0D486A3F57F8B7B9E7B4645'),
	UNHEX('FAC8213C377A2883AC7872A2516DE817EEF64B5D2C724FA00B2447ACDC0B9485'),
	'Вася', 'Пупкин',DEFAULT, DEFAULT, 1),
	(DEFAULT, 'motana',
	UNHEX('FA271CA06E188F1419A57ADCD088B827A6E07345D0D486A3F57F8B7B9E7B4645'),
	UNHEX('FAC8213C377A2883AC7872A2516DE817EEF64B5D2C724FA00B2447ACDC0B9485'),
	'Татьяна', 'Грудень', DEFAULT, DEFAULT, 2),
	(DEFAULT, 'GruV',
	UNHEX('FA271CA06E188F1419A57ADCD088B827A6E07345D0D486A3F57F8B7B9E7B4645'),
	UNHEX('FAC8213C377A2883AC7872A2516DE817EEF64B5D2C724FA00B2447ACDC0B9485'),
	'Грудень', 'Иван', DEFAULT, DEFAULT, 3);
	
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
(DEFAULT, 'Єксмо'),
(DEFAULT, 'Астрель');

-- -----------------------------------------------------
-- Add demo information to `library`.`authors`
-- -----------------------------------------------------
INSERT INTO `library_spring`.`authors` VALUES 
(DEFAULT, 'Вячеслав', 'Шалыгин'),
(DEFAULT, 'Роман', 'Куликов'),
(DEFAULT, 'Сергей', 'Недоруб');

-- -----------------------------------------------------
-- Add demo information to `library_spring`.`books`
-- -----------------------------------------------------
INSERT INTO `library_spring`.`books` VALUES 
(DEFAULT, 'Тринадцатый сектор', 10, 10, 1, 1, "2000"),
(DEFAULT, 'Зов припяти', 10, 10, 2, 2, "2001"),
(DEFAULT, 'Песочные часы', 10, 10, 3, 1, "2010");

