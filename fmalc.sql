-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema fmalc
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema fmalc
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `fmalc` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `fmalc` ;

-- -----------------------------------------------------
-- Table `fmalc`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`account` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `is_active` BIT(1) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `role_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_gex1lmaqpg0ir5g1f5eftyaa1` (`username` ASC) VISIBLE,
  INDEX `FKd4vb66o896tay3yy52oqxr9w0` (`role_id` ASC) VISIBLE,
  CONSTRAINT `FKd4vb66o896tay3yy52oqxr9w0`
    FOREIGN KEY (`role_id`)
    REFERENCES `fmalc`.`role` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`driver_license`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`driver_license` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `license_type` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`vehicle_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`vehicle_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `average_fuel` DOUBLE NOT NULL,
  `maximum_capacity` DOUBLE NOT NULL,
  `vehicle_type_name` VARCHAR(255) NOT NULL,
  `driver_license_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKt3pnwwq1sdl2byeb5jwqfqcp4` (`driver_license_id` ASC) VISIBLE,
  CONSTRAINT `FKt3pnwwq1sdl2byeb5jwqfqcp4`
    FOREIGN KEY (`driver_license_id`)
    REFERENCES `fmalc`.`driver_license` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`vehicle`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`vehicle` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `date_of_manufacture` DATE NOT NULL,
  `kilometer_running` INT(11) NOT NULL,
  `license_plates` VARCHAR(255) NOT NULL,
  `status` INT(11) NOT NULL,
  `vehicle_name` VARCHAR(255) NOT NULL,
  `weight` DOUBLE NOT NULL,
  `vehicle_type_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_bft8ds3fdkk56rghb77fnyg1v` (`license_plates` ASC) VISIBLE,
  INDEX `FKddtxlc05rojc56bprvek17hnk` (`vehicle_type_id` ASC) VISIBLE,
  CONSTRAINT `FKddtxlc05rojc56bprvek17hnk`
    FOREIGN KEY (`vehicle_type_id`)
    REFERENCES `fmalc`.`vehicle_type` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`fleet_manager`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`fleet_manager` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `identity_no` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `phone_number` VARCHAR(255) NOT NULL,
  `account_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK54bxyg99j7xwen5dq5fcldnem` (`account_id` ASC) VISIBLE,
  CONSTRAINT `FK54bxyg99j7xwen5dq5fcldnem`
    FOREIGN KEY (`account_id`)
    REFERENCES `fmalc`.`account` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`driver`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`driver` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `expires` DATETIME(6) NOT NULL,
  `identity_no` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `no` VARCHAR(255) NOT NULL,
  `phone_number` VARCHAR(255) NOT NULL,
  `status` INT(11) NOT NULL,
  `account_id` INT(11) NOT NULL,
  `fleet_manager_id` INT(11) NOT NULL,
  `driver_license_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKjadmq2tv9b60y5p0wfy16w9it` (`account_id` ASC) VISIBLE,
  INDEX `FKkp2fbic2m55upbj3lnmui0li8` (`fleet_manager_id` ASC) VISIBLE,
  INDEX `FK5rvj784j5f9u4yxur6slxpmhp` (`driver_license_id` ASC) VISIBLE,
  CONSTRAINT `FK5rvj784j5f9u4yxur6slxpmhp`
    FOREIGN KEY (`driver_license_id`)
    REFERENCES `fmalc`.`driver_license` (`id`),
  CONSTRAINT `FKjadmq2tv9b60y5p0wfy16w9it`
    FOREIGN KEY (`account_id`)
    REFERENCES `fmalc`.`account` (`id`),
  CONSTRAINT `FKkp2fbic2m55upbj3lnmui0li8`
    FOREIGN KEY (`fleet_manager_id`)
    REFERENCES `fmalc`.`fleet_manager` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`alert`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`alert` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(255) NOT NULL,
  `level` INT(11) NOT NULL,
  `time` DATETIME(6) NOT NULL,
  `driver_id` INT(11) NOT NULL,
  `vehicle_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKqk387ajf1m7tm5kcutlh6fxkl` (`driver_id` ASC) VISIBLE,
  INDEX `FKm7c2d4cxe1uopwr6njxp5k1f8` (`vehicle_id` ASC) VISIBLE,
  CONSTRAINT `FKm7c2d4cxe1uopwr6njxp5k1f8`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `fmalc`.`vehicle` (`id`),
  CONSTRAINT `FKqk387ajf1m7tm5kcutlh6fxkl`
    FOREIGN KEY (`driver_id`)
    REFERENCES `fmalc`.`driver` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`consignment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`consignment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `amount` INT(11) NOT NULL,
  `owner_name` VARCHAR(255) NOT NULL,
  `owner_note` VARCHAR(255) NULL DEFAULT NULL,
  `owner_reason_note` VARCHAR(255) NULL DEFAULT NULL,
  `status` INT(11) NOT NULL,
  `weight` DOUBLE NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`consignment_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`consignment_history` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `note` VARCHAR(255) NULL DEFAULT NULL,
  `time` DATETIME(6) NOT NULL,
  `consignment_id` INT(11) NOT NULL,
  `fleet_manager_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK7un2dbefb101im4cxfl4q3jbr` (`consignment_id` ASC) VISIBLE,
  INDEX `FK93viguflthjfaqmhxisxyviab` (`fleet_manager_id` ASC) VISIBLE,
  CONSTRAINT `FK7un2dbefb101im4cxfl4q3jbr`
    FOREIGN KEY (`consignment_id`)
    REFERENCES `fmalc`.`consignment` (`id`),
  CONSTRAINT `FK93viguflthjfaqmhxisxyviab`
    FOREIGN KEY (`fleet_manager_id`)
    REFERENCES `fmalc`.`fleet_manager` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`place`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`place` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `actual_time` DATETIME(6) NULL DEFAULT NULL,
  `address` VARCHAR(255) NOT NULL,
  `latitude` DOUBLE NOT NULL,
  `longitude` DOUBLE NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `planned_time` DATETIME(6) NOT NULL,
  `type` INT(11) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`delivery_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`delivery_detail` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `priority` INT(11) NOT NULL,
  `consignment_id` INT(11) NOT NULL,
  `place_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK1omaipqw78wifjeuf5p9cod3y` (`consignment_id` ASC) VISIBLE,
  INDEX `FKqwg9ilph9vwlrwin4lk601316` (`place_id` ASC) VISIBLE,
  CONSTRAINT `FK1omaipqw78wifjeuf5p9cod3y`
    FOREIGN KEY (`consignment_id`)
    REFERENCES `fmalc`.`consignment` (`id`),
  CONSTRAINT `FKqwg9ilph9vwlrwin4lk601316`
    FOREIGN KEY (`place_id`)
    REFERENCES `fmalc`.`place` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`inspection`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`inspection` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `inspection_level` INT(11) NOT NULL,
  `inspection_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`location` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `latitude` DOUBLE NOT NULL,
  `longitude` DOUBLE NOT NULL,
  `time` DATETIME(6) NOT NULL,
  `vehicle_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKh2ol71lr9c17y0bu97ui8nf01` (`vehicle_id` ASC) VISIBLE,
  CONSTRAINT `FKh2ol71lr9c17y0bu97ui8nf01`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `fmalc`.`vehicle` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`maintain_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`maintain_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(255) NOT NULL,
  `kilometers_number` INT(11) NOT NULL,
  `maintain_type_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`maintain`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`maintain` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `image_maintain` VARCHAR(255) NOT NULL,
  `km_new` INT(11) NOT NULL,
  `km_old` INT(11) NOT NULL,
  `maintain_date` DATE NOT NULL,
  `maintain_type_id` INT(11) NOT NULL,
  `vehicle_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKbmtw9rro4ih3v925y762qfl0b` (`maintain_type_id` ASC) VISIBLE,
  INDEX `FKlko4mk7oirvydvfhg2o4baaim` (`vehicle_id` ASC) VISIBLE,
  CONSTRAINT `FKbmtw9rro4ih3v925y762qfl0b`
    FOREIGN KEY (`maintain_type_id`)
    REFERENCES `fmalc`.`maintain_type` (`id`),
  CONSTRAINT `FKlko4mk7oirvydvfhg2o4baaim`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `fmalc`.`vehicle` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`notify_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`notify_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `notify_type_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`notify`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`notify` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(255) NOT NULL,
  `time` DATETIME(6) NOT NULL,
  `notify_type_id` INT(11) NOT NULL,
  `vehicle_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKfuaa2xoyuhpdt8jqd7x1yg7n9` (`notify_type_id` ASC) VISIBLE,
  INDEX `FKogm4hu41r0w8qt3h6ot0s4fdi` (`vehicle_id` ASC) VISIBLE,
  CONSTRAINT `FKfuaa2xoyuhpdt8jqd7x1yg7n9`
    FOREIGN KEY (`notify_type_id`)
    REFERENCES `fmalc`.`notify_type` (`id`),
  CONSTRAINT `FKogm4hu41r0w8qt3h6ot0s4fdi`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `fmalc`.`vehicle` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`report_issue`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`report_issue` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `time` DATETIME(6) NOT NULL,
  `driver_id` INT(11) NOT NULL,
  `inspection_id` INT(11) NOT NULL,
  `vehicle_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKqxax5dvrukemgvsbd230ald9q` (`driver_id` ASC) VISIBLE,
  INDEX `FK4kahp60iq5kenmd1qhb0ry7qs` (`inspection_id` ASC) VISIBLE,
  INDEX `FK5qaqj68iv6peccwu8j1atfg16` (`vehicle_id` ASC) VISIBLE,
  CONSTRAINT `FK4kahp60iq5kenmd1qhb0ry7qs`
    FOREIGN KEY (`inspection_id`)
    REFERENCES `fmalc`.`inspection` (`id`),
  CONSTRAINT `FK5qaqj68iv6peccwu8j1atfg16`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `fmalc`.`vehicle` (`id`),
  CONSTRAINT `FKqxax5dvrukemgvsbd230ald9q`
    FOREIGN KEY (`driver_id`)
    REFERENCES `fmalc`.`driver` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fmalc`.`schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fmalc`.`schedule` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `image_consignment` VARCHAR(255) NOT NULL,
  `note` VARCHAR(255) NULL DEFAULT NULL,
  `consignment_id` INT(11) NOT NULL,
  `driver_id` INT(11) NOT NULL,
  `vehicle_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKihr8fcsx7b2mygrjsh0bapdhj` (`consignment_id` ASC) VISIBLE,
  INDEX `FKmjfuvpp6n6ce63ppi9j3xnntr` (`driver_id` ASC) VISIBLE,
  INDEX `FK5s6sphay5edjq73mnw5gxwwaa` (`vehicle_id` ASC) VISIBLE,
  CONSTRAINT `FK5s6sphay5edjq73mnw5gxwwaa`
    FOREIGN KEY (`vehicle_id`)
    REFERENCES `fmalc`.`vehicle` (`id`),
  CONSTRAINT `FKihr8fcsx7b2mygrjsh0bapdhj`
    FOREIGN KEY (`consignment_id`)
    REFERENCES `fmalc`.`consignment` (`id`),
  CONSTRAINT `FKmjfuvpp6n6ce63ppi9j3xnntr`
    FOREIGN KEY (`driver_id`)
    REFERENCES `fmalc`.`driver` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
