CREATE DATABASE  IF NOT EXISTS `fmalc` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `fmalc`;
-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: localhost    Database: fmalc
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `is_active` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_gex1lmaqpg0ir5g1f5eftyaa1` (`username`),
  KEY `FKd4vb66o896tay3yy52oqxr9w0` (`role_id`),
  CONSTRAINT `FKd4vb66o896tay3yy52oqxr9w0` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,_binary '','$2a$10$eN43av6JxtASAkx00lU18.rFfpk0WO9Fx/tnpeaxmkKGacCw.0wke','admin',1),(2,_binary '','$2a$10$aa6qRXVoP1qyMI/McpA8G.BHUO9DO9ESpFHW/z0wVZk5.9yxnosMa','manager1',2),(3,_binary '','$2a$10$eN43av6JxtASAkx00lU18.rFfpk0WO9Fx/tnpeaxmkKGacCw.0wke','driver',3),(4,_binary '','$2a$10$aa6qRXVoP1qyMI/McpA8G.BHUO9DO9ESpFHW/z0wVZk5.9yxnosMa','driver2',3);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alert`
--

DROP TABLE IF EXISTS `alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `alert` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `level` int(11) NOT NULL,
  `status` bit(1) NOT NULL,
  `time` datetime(6) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqk387ajf1m7tm5kcutlh6fxkl` (`driver_id`),
  KEY `FKm7c2d4cxe1uopwr6njxp5k1f8` (`vehicle_id`),
  CONSTRAINT `FKm7c2d4cxe1uopwr6njxp5k1f8` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FKqk387ajf1m7tm5kcutlh6fxkl` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert`
--

LOCK TABLES `alert` WRITE;
/*!40000 ALTER TABLE `alert` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consignment`
--

DROP TABLE IF EXISTS `consignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `consignment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` int(11) NOT NULL,
  `owner_name` varchar(255) NOT NULL,
  `owner_note` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `weight` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consignment`
--

LOCK TABLES `consignment` WRITE;
/*!40000 ALTER TABLE `consignment` DISABLE KEYS */;
INSERT INTO `consignment` VALUES (1,1000,'Công ty TNHH Song Long','Trễ là trừ tiền',0,1000),(2,500,'Công ty TNHH Harima','Trễ là trừ tiền',1,500),(3,250,'Công ty TNHH Việt Tiệp','Trễ là trừ tiền',3,250);
/*!40000 ALTER TABLE `consignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consignment_history`
--

DROP TABLE IF EXISTS `consignment_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `consignment_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `note` varchar(255) DEFAULT NULL,
  `time` date NOT NULL,
  `consignment_id` int(11) NOT NULL,
  `fleet_manager_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7un2dbefb101im4cxfl4q3jbr` (`consignment_id`),
  KEY `FK93viguflthjfaqmhxisxyviab` (`fleet_manager_id`),
  CONSTRAINT `FK7un2dbefb101im4cxfl4q3jbr` FOREIGN KEY (`consignment_id`) REFERENCES `consignment` (`id`),
  CONSTRAINT `FK93viguflthjfaqmhxisxyviab` FOREIGN KEY (`fleet_manager_id`) REFERENCES `fleet_manager` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consignment_history`
--

LOCK TABLES `consignment_history` WRITE;
/*!40000 ALTER TABLE `consignment_history` DISABLE KEYS */;
INSERT INTO `consignment_history` VALUES (1,'created by admin','2020-07-13',1,1),(2,'created by admin','2020-07-13',2,1),(3,'created by admin','2020-07-13',3,1);
/*!40000 ALTER TABLE `consignment_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `day_off`
--

DROP TABLE IF EXISTS `day_off`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `day_off` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `end_date` date NOT NULL,
  `start_date` date NOT NULL,
  `driver_id` int(11) NOT NULL,
  `fleet_manager_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjhcsd8ujun2885gu81adpsdhf` (`driver_id`),
  KEY `FK78a9iphdyv6h5ni46a7tlfxnw` (`fleet_manager_id`),
  CONSTRAINT `FK78a9iphdyv6h5ni46a7tlfxnw` FOREIGN KEY (`fleet_manager_id`) REFERENCES `fleet_manager` (`id`),
  CONSTRAINT `FKjhcsd8ujun2885gu81adpsdhf` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `day_off`
--

LOCK TABLES `day_off` WRITE;
/*!40000 ALTER TABLE `day_off` DISABLE KEYS */;
/*!40000 ALTER TABLE `day_off` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `driver`
--

DROP TABLE IF EXISTS `driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `driver` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date_of_birth` date NOT NULL,
  `driver_license` int(11) NOT NULL,
  `identity_no` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `license_expires` date NOT NULL,
  `name` varchar(255) NOT NULL,
  `no` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `working_hour` float DEFAULT NULL,
  `account_id` int(11) NOT NULL,
  `fleet_manager_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjadmq2tv9b60y5p0wfy16w9it` (`account_id`),
  KEY `FKkp2fbic2m55upbj3lnmui0li8` (`fleet_manager_id`),
  CONSTRAINT `FKjadmq2tv9b60y5p0wfy16w9it` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKkp2fbic2m55upbj3lnmui0li8` FOREIGN KEY (`fleet_manager_id`) REFERENCES `fleet_manager` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` VALUES (1,'1998-01-27',1,'0123456789',NULL,'2025-01-01','driver','0123456789','0123456789',0,50,3,1),(2,'1998-10-02',1,'0123456789',NULL,'2025-01-01','driver2','0123456789','0123456789',0,50,4,1);
/*!40000 ALTER TABLE `driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fleet_manager`
--

DROP TABLE IF EXISTS `fleet_manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `fleet_manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date_of_birth` date DEFAULT NULL,
  `identity_no` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `account_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK54bxyg99j7xwen5dq5fcldnem` (`account_id`),
  CONSTRAINT `FK54bxyg99j7xwen5dq5fcldnem` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fleet_manager`
--

LOCK TABLES `fleet_manager` WRITE;
/*!40000 ALTER TABLE `fleet_manager` DISABLE KEYS */;
INSERT INTO `fleet_manager` VALUES (1,NULL,'123456789','https://fmalc-img.s3-ap-southeast-1.amazonaws.com/abc.jpg','Fleet Manager Default','0909090909',2);
/*!40000 ALTER TABLE `fleet_manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fuel`
--

DROP TABLE IF EXISTS `fuel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `fuel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filling_date` date NOT NULL,
  `km_old` int(11) NOT NULL,
  `unit_price_at_filling_time` float NOT NULL,
  `volume` double NOT NULL,
  `fuel_type_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh5kakrsd8cwabdn8asusx2p8n` (`fuel_type_id`),
  KEY `FKcsj8bx4ew5bbufbsh3ad5hpx6` (`vehicle_id`),
  CONSTRAINT `FKcsj8bx4ew5bbufbsh3ad5hpx6` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FKh5kakrsd8cwabdn8asusx2p8n` FOREIGN KEY (`fuel_type_id`) REFERENCES `fuel_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fuel`
--

LOCK TABLES `fuel` WRITE;
/*!40000 ALTER TABLE `fuel` DISABLE KEYS */;
/*!40000 ALTER TABLE `fuel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fuel_type`
--

DROP TABLE IF EXISTS `fuel_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `fuel_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `current_price` double DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fuel_type`
--

LOCK TABLES `fuel_type` WRITE;
/*!40000 ALTER TABLE `fuel_type` DISABLE KEYS */;
INSERT INTO `fuel_type` VALUES (1,15070,'Xăng RON 95-IV'),(2,14970,'Xăng RON 95-III'),(3,14250,'E5 RON 92-II'),(4,12310,'DO 0,001S-V'),(5,12110,'DO 0,05S-II');
/*!40000 ALTER TABLE `fuel_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspection`
--

DROP TABLE IF EXISTS `inspection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `inspection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inspection_name` varchar(255) NOT NULL,
  `is_active` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hbn4iokseogcve4w41vdtj3bv` (`inspection_name`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspection`
--

LOCK TABLES `inspection` WRITE;
/*!40000 ALTER TABLE `inspection` DISABLE KEYS */;
INSERT INTO `inspection` VALUES (5,'Xi nhan trái trước',_binary ''),(6,'Xi nhan phải trước',_binary ''),(7,'Xi nhan trái sau',_binary ''),(8,'Xi nhan phải sau',_binary ''),(9,'Gương chiếu hậu',_binary ''),(10,'Gương trái',_binary ''),(11,'Gương phải',_binary ''),(12,'Cửa số trái',_binary ''),(13,'Cửa sổ phải',_binary ''),(14,'Thắng xe',_binary ''),(15,'Còi',_binary ''),(16,'Cần gạt nước',_binary ''),(17,'Bánh xe',_binary ''),(18,'Điều hòa',_binary '');
/*!40000 ALTER TABLE `inspection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `time` datetime(6) NOT NULL,
  `schedule_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj02fdh4ircwsixd1pfamdp1ky` (`schedule_id`),
  CONSTRAINT `FKj02fdh4ircwsixd1pfamdp1ky` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintain_type`
--

DROP TABLE IF EXISTS `maintain_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `maintain_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `kilometers_number` int(11) NOT NULL,
  `maintain_type_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintain_type`
--

LOCK TABLES `maintain_type` WRITE;
/*!40000 ALTER TABLE `maintain_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `maintain_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance`
--

DROP TABLE IF EXISTS `maintenance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `maintenance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actual_maintain_date` date DEFAULT NULL,
  `image_maintain` varchar(255) NOT NULL,
  `km_old` int(11) NOT NULL,
  `planned_maintain_date` date NOT NULL,
  `driver_id` int(11) NOT NULL,
  `maintain_type_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdovlmvrq34jei6sq558dgqd9h` (`driver_id`),
  KEY `FK4plhblq97ud7dinmio5lvlbs` (`maintain_type_id`),
  KEY `FK1uctvvn1p1y3rp57tq34mao9h` (`vehicle_id`),
  CONSTRAINT `FK1uctvvn1p1y3rp57tq34mao9h` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FK4plhblq97ud7dinmio5lvlbs` FOREIGN KEY (`maintain_type_id`) REFERENCES `maintain_type` (`id`),
  CONSTRAINT `FKdovlmvrq34jei6sq558dgqd9h` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance`
--

LOCK TABLES `maintenance` WRITE;
/*!40000 ALTER TABLE `maintenance` DISABLE KEYS */;
/*!40000 ALTER TABLE `maintenance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `status` bit(1) NOT NULL,
  `time` datetime(6) NOT NULL,
  `type` int(11) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn2xfgfgrnl177v2p9ca2odk2o` (`driver_id`),
  KEY `FKfv99ej3rl460ak0erv34yk7vd` (`vehicle_id`),
  CONSTRAINT `FKfv99ej3rl460ak0erv34yk7vd` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FKn2xfgfgrnl177v2p9ca2odk2o` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `place`
--

DROP TABLE IF EXISTS `place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `place` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actual_time` datetime(6) DEFAULT NULL,
  `address` varchar(255) NOT NULL,
  `contact_name` varchar(255) DEFAULT NULL,
  `contact_phone` varchar(255) DEFAULT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `planned_time` datetime(6) NOT NULL,
  `priority` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `consignment_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK202l4yhf2qrxcptqcdy8pc6rx` (`consignment_id`),
  CONSTRAINT `FK202l4yhf2qrxcptqcdy8pc6rx` FOREIGN KEY (`consignment_id`) REFERENCES `consignment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `place`
--

LOCK TABLES `place` WRITE;
/*!40000 ALTER TABLE `place` DISABLE KEYS */;
INSERT INTO `place` VALUES (1,'2020-07-14 08:00:00.000000','Lô D, Quận 9',NULL,NULL,1,1,'Đại học FPT','2020-07-16 00:00:00.000000',1,0,1),(2,'2020-07-14 08:00:00.000000','Xô Viết Nghệ Tĩnh, Bình Thạnh',NULL,NULL,1,1,'Cheese coffee','2020-07-16 00:00:00.000000',2,0,1),(3,'2020-07-14 08:00:00.000000','Nguyễn Thái Sơn, Gò Vấp',NULL,NULL,1,1,'Thức coffee','2020-07-16 00:00:00.000000',3,1,1),(4,'2020-07-14 08:00:00.000000','Đặng Văn Bi, Thủ Đức',NULL,NULL,1,1,'The coffee house ','2020-07-16 00:00:00.000000',4,1,1),(5,'2020-07-14 08:00:00.000000','Lô D, Quận 9',NULL,NULL,1,1,'Đại học FPT','2020-07-16 15:00:00.000000',1,0,2),(6,'2020-07-14 08:00:00.000000','Xô Viết Nghệ Tĩnh, Bình Thạnh',NULL,NULL,1,1,'Cheese coffee','2020-07-16 15:00:00.000000',2,0,2),(7,'2020-07-14 08:00:00.000000','Nguyễn Thái Sơn, Gò Vấp',NULL,NULL,1,1,'Thức coffee','2020-07-16 15:00:00.000000',3,1,2),(8,'2020-07-14 08:00:00.000000','Đặng Văn Bi, Thủ Đức',NULL,NULL,1,1,'The coffee house ','2020-07-16 15:00:00.000000',4,1,2),(9,'2020-07-14 08:00:00.000000','Lô D, Quận 9',NULL,NULL,1,1,'Đại học FPT','2020-07-17 05:00:00.000000',1,0,3),(10,'2020-07-14 08:00:00.000000','Xô Viết Nghệ Tĩnh, Bình Thạnh',NULL,NULL,1,1,'Cheese coffee','2020-07-17 05:00:00.000000',2,0,3),(11,'2020-07-14 08:00:00.000000','Nguyễn Thái Sơn, Gò Vấp',NULL,NULL,1,1,'Thức coffee','2020-07-17 05:00:00.000000',3,1,3),(12,'2020-07-14 08:00:00.000000','Đặng Văn Bi, Thủ Đức',NULL,NULL,1,1,'The coffee house ','2020-07-17 05:00:00.000000',4,1,3);
/*!40000 ALTER TABLE `place` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_issue`
--

DROP TABLE IF EXISTS `report_issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `report_issue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `create_time` datetime(6) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `status` bit(1) NOT NULL DEFAULT b'1',
  `type` int(11) NOT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `created_by` int(11) NOT NULL,
  `inspection_id` int(11) NOT NULL,
  `updated_by` int(11) DEFAULT NULL,
  `vehicle_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKde004w754bpd7k47b5df34k6l` (`created_by`),
  KEY `FK4kahp60iq5kenmd1qhb0ry7qs` (`inspection_id`),
  KEY `FKbo6rnbmocei2uqua8qx6jdtdc` (`updated_by`),
  KEY `FK5qaqj68iv6peccwu8j1atfg16` (`vehicle_id`),
  CONSTRAINT `FK4kahp60iq5kenmd1qhb0ry7qs` FOREIGN KEY (`inspection_id`) REFERENCES `inspection` (`id`),
  CONSTRAINT `FK5qaqj68iv6peccwu8j1atfg16` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FKbo6rnbmocei2uqua8qx6jdtdc` FOREIGN KEY (`updated_by`) REFERENCES `driver` (`id`),
  CONSTRAINT `FKde004w754bpd7k47b5df34k6l` FOREIGN KEY (`created_by`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_issue`
--

LOCK TABLES `report_issue` WRITE;
/*!40000 ALTER TABLE `report_issue` DISABLE KEYS */;
INSERT INTO `report_issue` VALUES (1,'recheck','2020-07-14 22:40:58.278000',NULL,_binary '',0,NULL,1,6,NULL,1),(2,'recheck','2020-07-14 22:40:58.288000',NULL,_binary '',0,NULL,1,5,NULL,1),(3,'recheck','2020-07-14 22:40:58.240000',NULL,_binary '',0,NULL,1,7,NULL,1),(4,'checked','2020-07-15 22:44:38.274000',NULL,_binary '',0,NULL,1,8,NULL,1),(5,'checked','2020-07-15 22:44:38.289000',NULL,_binary '',0,NULL,1,7,NULL,1),(6,'checked','2020-07-15 22:44:38.316000',NULL,_binary '',0,NULL,1,5,NULL,1),(7,'checked','2020-07-15 22:44:38.327000',NULL,_binary '',0,NULL,1,6,NULL,1),(8,'new','2020-07-15 22:44:38.301000',NULL,_binary '',0,NULL,1,9,NULL,1),(9,'ahihi','2020-07-16 01:49:43.935000','https://fmalc-img.s3.ap-southeast-1.amazonaws.com/1594839108695-FMALC_ACTIVITY_DIAGRAM-Route_Optimization_and_Scheduling.png',_binary '',0,NULL,1,18,NULL,1);
/*!40000 ALTER TABLE `report_issue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_FLEET_MANAGER'),(3,'ROLE_DRIVER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image_consignment` varchar(255) DEFAULT NULL,
  `is_approve` bit(1) NOT NULL DEFAULT b'1',
  `note` varchar(255) DEFAULT NULL,
  `consignment_id` int(11) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKihr8fcsx7b2mygrjsh0bapdhj` (`consignment_id`),
  KEY `FKmjfuvpp6n6ce63ppi9j3xnntr` (`driver_id`),
  KEY `FK5s6sphay5edjq73mnw5gxwwaa` (`vehicle_id`),
  CONSTRAINT `FK5s6sphay5edjq73mnw5gxwwaa` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FKihr8fcsx7b2mygrjsh0bapdhj` FOREIGN KEY (`consignment_id`) REFERENCES `consignment` (`id`),
  CONSTRAINT `FKmjfuvpp6n6ce63ppi9j3xnntr` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (1,NULL,_binary '',NULL,1,1,1),(2,NULL,_binary '',NULL,1,2,2),(3,NULL,_binary '',NULL,2,1,3),(4,NULL,_binary '',NULL,3,2,3);
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `vehicle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `average_fuel` double NOT NULL,
  `date_of_manufacture` date NOT NULL,
  `driver_license` int(11) NOT NULL,
  `kilometer_running` int(11) NOT NULL,
  `license_plates` varchar(255) NOT NULL,
  `maximum_capacity` double NOT NULL,
  `status` int(11) NOT NULL,
  `vehicle_name` varchar(255) NOT NULL,
  `weight` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bft8ds3fdkk56rghb77fnyg1v` (`license_plates`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES (1,100,'2020-01-01',1,2500,'59-X3 101.91',1000,0,'honda',1000),(2,100,'2020-01-01',1,2500,'69-A3 000.01',1000,0,'huyndai',1000),(3,100,'2020-01-01',1,2500,'46-N3 999.90',1000,0,'suziki',1000);
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'fmalc'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-16  2:21:45
