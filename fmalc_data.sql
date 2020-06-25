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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,_binary '','$2a$10$724MDLiAX2cZK/dpTrKqsOxIVyO4UQZUAW4O5VYwq5tlFSxvQvdqa','admin',1),(2,_binary '','$2a$10$8uLk3P2mVYZO5V7G4V6YaetVF/iacaUSDPpboWAx9Y061ARRiHoVO','manager1',2),(3,_binary '','$2a$10$8uLk3P2mVYZO5V7G4V6YaetVF/iacaUSDPpboWAx9Y061ARRiHoVO','driver',3);
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consignment`
--

LOCK TABLES `consignment` WRITE;
/*!40000 ALTER TABLE `consignment` DISABLE KEYS */;
INSERT INTO `consignment` VALUES (1,1000,'CTy TNHH FMALC','Lấy hàng đúng giờ',0,1000),(2,2000,'FPT Software ','Lấy hàng sai giờ là trừ tiền',0,1000);
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
  `time` datetime(6) NOT NULL,
  `consignment_id` int(11) NOT NULL,
  `fleet_manager_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7un2dbefb101im4cxfl4q3jbr` (`consignment_id`),
  KEY `FK93viguflthjfaqmhxisxyviab` (`fleet_manager_id`),
  CONSTRAINT `FK7un2dbefb101im4cxfl4q3jbr` FOREIGN KEY (`consignment_id`) REFERENCES `consignment` (`id`),
  CONSTRAINT `FK93viguflthjfaqmhxisxyviab` FOREIGN KEY (`fleet_manager_id`) REFERENCES `fleet_manager` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consignment_history`
--

LOCK TABLES `consignment_history` WRITE;
/*!40000 ALTER TABLE `consignment_history` DISABLE KEYS */;
INSERT INTO `consignment_history` VALUES (1,'create by admin','2020-06-25 00:00:00.000000',1,1),(2,'create by admin','2020-06-25 00:00:00.000000',2,1);
/*!40000 ALTER TABLE `consignment_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_detail`
--

DROP TABLE IF EXISTS `delivery_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `delivery_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `priority` int(11) NOT NULL,
  `consignment_id` int(11) NOT NULL,
  `place_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1omaipqw78wifjeuf5p9cod3y` (`consignment_id`),
  KEY `FKqwg9ilph9vwlrwin4lk601316` (`place_id`),
  CONSTRAINT `FK1omaipqw78wifjeuf5p9cod3y` FOREIGN KEY (`consignment_id`) REFERENCES `consignment` (`id`),
  CONSTRAINT `FKqwg9ilph9vwlrwin4lk601316` FOREIGN KEY (`place_id`) REFERENCES `place` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_detail`
--

LOCK TABLES `delivery_detail` WRITE;
/*!40000 ALTER TABLE `delivery_detail` DISABLE KEYS */;
INSERT INTO `delivery_detail` VALUES (1,2,1,1),(2,1,1,2),(3,3,1,3),(4,4,2,4),(5,2,2,2),(6,3,2,3),(7,1,2,1);
/*!40000 ALTER TABLE `delivery_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `driver`
--

DROP TABLE IF EXISTS `driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `driver` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date_of_birth` datetime(6) NOT NULL,
  `driver_license` int(11) NOT NULL,
  `identity_no` varchar(255) NOT NULL,
  `license_expires` datetime(6) NOT NULL,
  `name` varchar(255) NOT NULL,
  `no` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `fleet_manager_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjadmq2tv9b60y5p0wfy16w9it` (`account_id`),
  KEY `FKkp2fbic2m55upbj3lnmui0li8` (`fleet_manager_id`),
  CONSTRAINT `FKjadmq2tv9b60y5p0wfy16w9it` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKkp2fbic2m55upbj3lnmui0li8` FOREIGN KEY (`fleet_manager_id`) REFERENCES `fleet_manager` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` VALUES (1,'2020-06-25 00:00:00.000000',1,'025917287','2020-06-25 00:00:00.000000','Nguyễn Văn Lương','01111111','0372813268',0,3,1);
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
  `identity_no` varchar(255) NOT NULL,
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
INSERT INTO `fleet_manager` VALUES (1,'123456789','Fleet Manager','0909090909',2);
/*!40000 ALTER TABLE `fleet_manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspection`
--

DROP TABLE IF EXISTS `inspection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `inspection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inspection_level` int(11) NOT NULL,
  `inspection_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspection`
--

LOCK TABLES `inspection` WRITE;
/*!40000 ALTER TABLE `inspection` DISABLE KEYS */;
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
  `vehicle_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh2ol71lr9c17y0bu97ui8nf01` (`vehicle_id`),
  CONSTRAINT `FKh2ol71lr9c17y0bu97ui8nf01` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`)
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
-- Table structure for table `maintain`
--

DROP TABLE IF EXISTS `maintain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `maintain` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image_maintain` varchar(255) NOT NULL,
  `km_old` int(11) NOT NULL,
  `maintain_date` date NOT NULL,
  `maintain_type_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbmtw9rro4ih3v925y762qfl0b` (`maintain_type_id`),
  KEY `FKlko4mk7oirvydvfhg2o4baaim` (`vehicle_id`),
  CONSTRAINT `FKbmtw9rro4ih3v925y762qfl0b` FOREIGN KEY (`maintain_type_id`) REFERENCES `maintain_type` (`id`),
  CONSTRAINT `FKlko4mk7oirvydvfhg2o4baaim` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintain`
--

LOCK TABLES `maintain` WRITE;
/*!40000 ALTER TABLE `maintain` DISABLE KEYS */;
/*!40000 ALTER TABLE `maintain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintain_type`
--

DROP TABLE IF EXISTS `maintain_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `maintain_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
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
  `image_maintain` varchar(255) NOT NULL,
  `km_old` int(11) NOT NULL,
  `maintain_date` date NOT NULL,
  `maintain_type_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4plhblq97ud7dinmio5lvlbs` (`maintain_type_id`),
  KEY `FK1uctvvn1p1y3rp57tq34mao9h` (`vehicle_id`),
  CONSTRAINT `FK1uctvvn1p1y3rp57tq34mao9h` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FK4plhblq97ud7dinmio5lvlbs` FOREIGN KEY (`maintain_type_id`) REFERENCES `maintain_type` (`id`)
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
-- Table structure for table `notify`
--

DROP TABLE IF EXISTS `notify`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `notify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `status` bit(1) NOT NULL,
  `time` datetime(6) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhed5antp0odowv48g6akkertj` (`driver_id`),
  KEY `FKogm4hu41r0w8qt3h6ot0s4fdi` (`vehicle_id`),
  CONSTRAINT `FKhed5antp0odowv48g6akkertj` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`),
  CONSTRAINT `FKogm4hu41r0w8qt3h6ot0s4fdi` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notify`
--

LOCK TABLES `notify` WRITE;
/*!40000 ALTER TABLE `notify` DISABLE KEYS */;
/*!40000 ALTER TABLE `notify` ENABLE KEYS */;
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
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `planned_time` datetime(6) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `place`
--

LOCK TABLES `place` WRITE;
/*!40000 ALTER TABLE `place` DISABLE KEYS */;
INSERT INTO `place` VALUES (1,NULL,'26/2 đường 40',1,1,'FPT University','2020-06-25 00:00:00.000000',0),(2,NULL,'Nguyễn Thái Sơn. Gò Vấp',2,2,'Thức coffee','2020-06-26 00:00:00.000000',0),(3,NULL,'Nguyễn Oanh. Gò Vấp',3,3,'Chợ Gò Vấp','2020-06-27 00:00:00.000000',1),(4,NULL,'D2 Lô 4',4,4,'FPT Software ','2020-06-28 00:00:00.000000',0),(5,NULL,'D3 Lô 10 Bình Dương',5,5,'Chợ đầu mối','2020-06-28 00:00:00.000000',0),(6,NULL,'123 đường 1',6,6,'Đại Nam','2020-06-28 00:00:00.000000',1),(7,NULL,'321 đường 421',7,7,'Đầm Sen','2020-06-28 00:00:00.000000',1);
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
  `time` datetime(6) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `inspection_id` int(11) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqxax5dvrukemgvsbd230ald9q` (`driver_id`),
  KEY `FK4kahp60iq5kenmd1qhb0ry7qs` (`inspection_id`),
  KEY `FK5qaqj68iv6peccwu8j1atfg16` (`vehicle_id`),
  CONSTRAINT `FK4kahp60iq5kenmd1qhb0ry7qs` FOREIGN KEY (`inspection_id`) REFERENCES `inspection` (`id`),
  CONSTRAINT `FK5qaqj68iv6peccwu8j1atfg16` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`),
  CONSTRAINT `FKqxax5dvrukemgvsbd230ald9q` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_issue`
--

LOCK TABLES `report_issue` WRITE;
/*!40000 ALTER TABLE `report_issue` DISABLE KEYS */;
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
  `image_consignment` varchar(255) NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (1,'image nè','Lấy hàng sai giờ là trừ tiền',1,1,1),(2,'image nè','Lấy hàng sai giờ trừ tiền',2,1,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES (1,100,'2020-06-25',1,1200,'59-X3 101.91',1000,0,'Honda',3.5);
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

-- Dump completed on 2020-06-26  0:10:23
