-- MySQL dump 10.13  Distrib 8.0.37, for Win64 (x86_64)
--
-- Host: localhost    Database: payload
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `board_id` bigint NOT NULL AUTO_INCREMENT,
  `create_date` varchar(255) DEFAULT NULL,
  `modified_date` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `title` varchar(100) NOT NULL,
  `view_count` int DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `userid` int DEFAULT NULL,
  PRIMARY KEY (`board_id`),
  KEY `FK60jn05fpinj6cjhqcmtkacu2g` (`category_id`),
  KEY `FK5vetjtrvc03etabyyl6su0s2e` (`userid`),
  CONSTRAINT `FK5vetjtrvc03etabyyl6su0s2e` FOREIGN KEY (`userid`) REFERENCES `member` (`userid`),
  CONSTRAINT `FK60jn05fpinj6cjhqcmtkacu2g` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board`
--

LOCK TABLES `board` WRITE;
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
INSERT INTO `board` VALUES (1,'2024/08/17 02:17:21','2024/08/17 02:18:45','hello','test',1,1,1),(2,'2024/08/17 02:17:31','2024/08/17 02:17:31','hello','test',0,2,1),(3,'2024/08/17 02:17:42','2024/08/25 23:57:28','hello','test2',1,1,1),(4,'2024/08/17 02:40:28','2024/08/17 02:40:28','hahahahahhahah','tip@!!',0,4,1),(5,'2024/08/25 23:57:06','2024/08/31 20:28:24','hellow?낅땲??,'?덈뀞?섏꽭??,4,3,2),(6,'2024/08/30 10:51:23','2024/08/30 18:58:22','Pay1oad ?좎엯?앹쓣 紐⑥쭛?⑸땲??','2024???좎엯??紐⑥쭛',3,4,2);
/*!40000 ALTER TABLE `board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_46ccwnsi9409t36lurvtyljak` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (3,'怨쇱젣 ?쒖텧 寃뚯떆??),(2,'諛쒗몴 ?먮즺 寃뚯떆??),(1,'蹂댁븞 ?뚯떇'),(4,'?뺣낫 寃뚯떆??);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_id` bigint NOT NULL,
  `create_date` varchar(255) DEFAULT NULL,
  `modified_date` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `board_id` bigint DEFAULT NULL,
  `member_id` int DEFAULT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `FKlij9oor1nav89jeat35s6kbp1` (`board_id`),
  KEY `FKmrrrpi513ssu63i2783jyiv9m` (`member_id`),
  CONSTRAINT `FKlij9oor1nav89jeat35s6kbp1` FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`),
  CONSTRAINT `FKmrrrpi513ssu63i2783jyiv9m` FOREIGN KEY (`member_id`) REFERENCES `member` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'2024/08/31 00:43:46','2024/08/31 00:43:46','留덈땲留덈땲 吏?먰빐二쇱꽭??,6,2);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_seq`
--

DROP TABLE IF EXISTS `comment_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_seq`
--

LOCK TABLES `comment_seq` WRITE;
/*!40000 ALTER TABLE `comment_seq` DISABLE KEYS */;
INSERT INTO `comment_seq` VALUES (101);
/*!40000 ALTER TABLE `comment_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_verification`
--

DROP TABLE IF EXISTS `email_verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_verification` (
  `verificationid` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`verificationid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_verification`
--

LOCK TABLES `email_verification` WRITE;
/*!40000 ALTER TABLE `email_verification` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_verification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file` (
  `file_id` bigint NOT NULL,
  `create_date` varchar(255) DEFAULT NULL,
  `modified_date` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `file_type` varchar(255) NOT NULL,
  `origin_file_name` varchar(255) DEFAULT NULL,
  `board_id` bigint DEFAULT NULL,
  PRIMARY KEY (`file_id`),
  KEY `FKhxmhyint41lp428u8yxcy4alx` (`board_id`),
  CONSTRAINT `FKhxmhyint41lp428u8yxcy4alx` FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
INSERT INTO `file` VALUES (1,'2024/08/31 11:38:48','2024/08/31 11:38:48','POST_5_6553c664-f7a3-4d64-b842-0e48f7ff5ea3.png','image/png','吏깃뎄-而댄벂??諛곌꼍?붾㈃1.png',5),(2,'2024/08/31 20:28:01','2024/08/31 20:28:01','POST_5_613fc562-b5df-4ebc-ba6e-ed029da10bdf.png','image/png','吏깃뎄-而댄벂??諛곌꼍?붾㈃1.png',5),(3,'2024/08/31 20:28:53','2024/08/31 20:28:53','POST_5_8ce41dc3-511e-487d-bd9b-e1fcc43515d5.png','image/png','吏깃뎄-而댄벂??諛곌꼍?붾㈃1.png',5),(52,'2024/09/03 15:55:49','2024/09/03 15:55:49','POST_5_70c2c02a-912b-449d-8ad4-6dc917c5a8f1.png','image/png','吏깃뎄-而댄벂??諛곌꼍?붾㈃1.png',5),(53,'2024/09/03 15:55:53','2024/09/03 15:55:53','POST_5_9d492299-69a1-47e7-896b-3961b4ae3c3b.png','image/png','吏깃뎄-而댄벂??諛곌꼍?붾㈃1.png',5);
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_seq`
--

DROP TABLE IF EXISTS `file_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_seq`
--

LOCK TABLES `file_seq` WRITE;
/*!40000 ALTER TABLE `file_seq` DISABLE KEYS */;
INSERT INTO `file_seq` VALUES (151);
/*!40000 ALTER TABLE `file_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likes`
--

DROP TABLE IF EXISTS `likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `board_id` bigint DEFAULT NULL,
  `userid` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5cq36196j3ww17d7r95qdm4td` (`board_id`),
  KEY `FKgq7pw3mjjhg0a0t910a8aisgr` (`userid`),
  CONSTRAINT `FK5cq36196j3ww17d7r95qdm4td` FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`),
  CONSTRAINT `FKgq7pw3mjjhg0a0t910a8aisgr` FOREIGN KEY (`userid`) REFERENCES `member` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes`
--

LOCK TABLES `likes` WRITE;
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
INSERT INTO `likes` VALUES (1,3,1),(2,3,1),(3,3,1),(4,4,1),(6,6,2);
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `userid` int NOT NULL AUTO_INCREMENT,
  `auth_provider` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `member_auth` tinyint NOT NULL,
  `passwd` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `verified` bit(1) DEFAULT NULL,
  PRIMARY KEY (`userid`),
  CONSTRAINT `member_chk_1` CHECK ((`member_auth` between 0 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,NULL,'abc@naver.com',5,'12345678Aa','test24',NULL),(2,NULL,'test@naver.com',5,'12345678Aa','apple',NULL);
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-03 23:15:40
