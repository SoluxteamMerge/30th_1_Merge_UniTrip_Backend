-- MySQL dump 10.13  Distrib 8.0.42, for Linux (x86_64)
--
-- Host: database-1.c7ma6i8ic7eq.us-east-2.rds.amazonaws.com    Database: unitrip
-- ------------------------------------------------------
-- Server version	8.0.41

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '';

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `category_id` int NOT NULL,
  `likes` int NOT NULL,
  `views` int NOT NULL,
  `created_at` datetime NOT NULL,
  `post_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `board_type` enum('MT_LT','국내학점교류','동행모집','모임구인','졸업_휴학여행','해외교환') NOT NULL,
  `comment_count` int NOT NULL,
  `scraps` int NOT NULL,
  `place_id` bigint DEFAULT NULL,
  `thumbnail_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  KEY `FKfyf1fchnby6hndhlfaidier1r` (`user_id`),
  KEY `FKrsv9enkb91opgextyte5up0kt` (`category_id`),
  KEY `FKd1vst92ujsp42m7ocm6970qi9` (`place_id`),
  CONSTRAINT `FKd1vst92ujsp42m7ocm6970qi9` FOREIGN KEY (`place_id`) REFERENCES `place` (`place_id`),
  CONSTRAINT `FKfyf1fchnby6hndhlfaidier1r` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKrsv9enkb91opgextyte5up0kt` FOREIGN KEY (`category_id`) REFERENCES `post_category` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board`
--

LOCK TABLES `board` WRITE;
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
INSERT INTO `board` VALUES (1,10,100,'2025-07-22 13:32:57',23,7,'강릉 여행 후기','강릉 다녀온 후기입니다!','국내학점교류',3,5,1,'https://example.com/image.jpg'),(10,0,2,'2025-07-22 23:55:02',24,5,'정말 맛있었어요!','<p>여기 정말 맛있어요!</p><img src=\"https://cdn.example.com/reviews/2025/06/27/abc123.jpg\" alt=\"맛집 사진\">','모임구인',0,0,26,NULL),(18,0,0,'2025-07-23 00:34:52',27,5,'엠티 대성리 숙소 추천','가성비 좋은 MT 숙소 추천해드릴게요.','MT_LT',0,0,29,NULL),(10,0,0,'2025-07-23 00:36:16',28,8,'정말 맛있었어요!','<p>여기 정말 맛있어요!</p><img src=\"https://cdn.example.com/reviews/2025/06/27/abc123.jpg\" alt=\"맛집 사진\">','모임구인',0,0,30,NULL),(18,0,0,'2025-07-23 00:38:52',29,8,'엠티 대성리 숙소 추천','가성비 좋은 MT 숙소 추천해드릴게요.','MT_LT',0,0,31,NULL),(19,0,0,'2025-07-23 02:44:39',30,8,'휴학하고 제주도 한달 살이','제주도 한달살이 도전해봤습니다.','졸업_휴학여행',0,0,32,NULL);
/*!40000 ALTER TABLE `board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_place_mapping`
--

DROP TABLE IF EXISTS `board_place_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_place_mapping` (
  `mapping_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `post_id` bigint NOT NULL,
  `place_id` bigint NOT NULL,
  PRIMARY KEY (`mapping_id`),
  KEY `FK5dvfhdg15mpkx596lxv6hs77o` (`post_id`),
  KEY `FKo3ccoqaaghp0q3ysflr3moern` (`place_id`),
  CONSTRAINT `FK5dvfhdg15mpkx596lxv6hs77o` FOREIGN KEY (`post_id`) REFERENCES `board` (`post_id`),
  CONSTRAINT `FKo3ccoqaaghp0q3ysflr3moern` FOREIGN KEY (`place_id`) REFERENCES `place` (`place_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_place_mapping`
--

LOCK TABLES `board_place_mapping` WRITE;
/*!40000 ALTER TABLE `board_place_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_place_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_likes`
--

DROP TABLE IF EXISTS `comment_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_likes` (
  `like_id` bigint NOT NULL AUTO_INCREMENT,
  `comment_id` bigint DEFAULT NULL,
  `liked_at` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`like_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_likes`
--

LOCK TABLES `comment_likes` WRITE;
/*!40000 ALTER TABLE `comment_likes` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `comment_id` bigint NOT NULL AUTO_INCREMENT,
  `content` tinytext,
  `created_at` datetime(6) DEFAULT NULL,
  `like_count` int DEFAULT NULL,
  `post_id` bigint DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1,'이것은 테스트 댓글입니다.','2025-07-07 15:23:13.514259',0,1,NULL,1),(3,'이것은 테스트 댓글입니다22.','2025-07-08 10:31:32.430882',0,1,NULL,3),(4,'이것은 테스트 댓글입니다.','2025-07-11 01:04:20.673005',0,1,NULL,1);
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_recruit_board`
--

DROP TABLE IF EXISTS `group_recruit_board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_recruit_board` (
  `post_id` bigint NOT NULL,
  `overnight_flag` bit(1) DEFAULT NULL,
  `recruitment_cnt` int DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  CONSTRAINT `FKsx40n23l5j52jkhp4wyrt9a4i` FOREIGN KEY (`post_id`) REFERENCES `board` (`post_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_recruit_board`
--

LOCK TABLES `group_recruit_board` WRITE;
/*!40000 ALTER TABLE `group_recruit_board` DISABLE KEYS */;
INSERT INTO `group_recruit_board` VALUES (24,_binary '\0',4),(28,_binary '\0',4);
/*!40000 ALTER TABLE `group_recruit_board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image` (
  `created_at` datetime(6) NOT NULL,
  `image_id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`image_id`),
  KEY `FKlxnnh0ir05khn8iu9tgwh1yyk` (`user_id`),
  KEY `fk_image_post_id` (`post_id`),
  CONSTRAINT `fk_image_post_id` FOREIGN KEY (`post_id`) REFERENCES `board` (`post_id`) ON DELETE CASCADE,
  CONSTRAINT `FKlxnnh0ir05khn8iu9tgwh1yyk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES ('2025-07-23 00:36:16.545571',5,28,NULL,'https://unitripbucket.s3.ap-northeast-2.amazonaws.com/board/b5ab4d10-986a-4d86-b31e-386ccf413f67_KakaoTalk_20250717_171047777.png'),('2025-07-23 00:38:51.805016',6,29,NULL,'https://unitripbucket.s3.ap-northeast-2.amazonaws.com/board/d3721a11-8145-40ea-af79-81996717e4b3_KakaoTalk_20250717_171047777.png'),('2025-07-23 02:44:38.865300',7,30,8,'https://unitripbucket.s3.ap-northeast-2.amazonaws.com/board/9c7edd0f-4398-4322-9b7d-497c719114f5_KakaoTalk_20250717_171047777.png'),('2025-07-23 02:44:39.047857',8,30,8,'https://unitripbucket.s3.ap-northeast-2.amazonaws.com/board/e951bf5b-6f6b-42fe-a388-c69b40cfd845_KakaoTalk_20250717_171047777.png');
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `keyword_ranking`
--

DROP TABLE IF EXISTS `keyword_ranking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `keyword_ranking` (
  `rank` int NOT NULL,
  `calculated_at` datetime(6) NOT NULL,
  `keyword_id` bigint NOT NULL,
  `keyword` varchar(100) NOT NULL,
  PRIMARY KEY (`keyword_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `keyword_ranking`
--

LOCK TABLES `keyword_ranking` WRITE;
/*!40000 ALTER TABLE `keyword_ranking` DISABLE KEYS */;
INSERT INTO `keyword_ranking` VALUES (1,'2025-07-22 21:53:34.655581',1,'여수'),(2,'2025-07-22 21:53:36.343616',2,'제주도'),(3,'2025-07-22 21:53:36.513974',3,'부산 여행'),(4,'2025-07-22 21:53:36.683236',4,'캠핑'),(5,'2025-07-22 21:53:36.852101',5,'서울 맛집'),(6,'2025-07-22 21:53:37.021832',6,'강릉 카페'),(7,'2025-07-22 21:53:37.191608',7,'여수 밤바다'),(8,'2025-07-22 21:53:37.362050',8,'속초 여행'),(9,'2025-07-22 21:53:37.530875',9,'대전 관광'),(10,'2025-07-22 21:53:37.701617',10,'전주 한옥마을');
/*!40000 ALTER TABLE `keyword_ranking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization` (
  `org_id` bigint NOT NULL AUTO_INCREMENT,
  `org_registration_number` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `representative_name` varchar(50) NOT NULL,
  `org_email` varchar(100) NOT NULL,
  `org_name` varchar(100) NOT NULL,
  `orgEmail` varchar(100) NOT NULL,
  `orgName` varchar(100) NOT NULL,
  `orgRegistrationNumber` bigint NOT NULL,
  `representativeName` varchar(50) NOT NULL,
  PRIMARY KEY (`org_id`),
  KEY `FKq0435723w14233u7xu6r92xev` (`user_id`),
  CONSTRAINT `FKq0435723w14233u7xu6r92xev` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `place`
--

DROP TABLE IF EXISTS `place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `place` (
  `place_id` bigint NOT NULL AUTO_INCREMENT,
  `kakao_id` varchar(50) NOT NULL,
  `address` varchar(255) NOT NULL,
  `place_name` varchar(255) NOT NULL,
  `region` enum('BUSAN','CHUNGBUK','CHUNGNAM','DAEGU','DAEJEON','GANGWON','GWANGJU','GYEONGBUK','GYEONGGI','GYEONGNAM','INCHEON','JEJU','JEONBUK','JEONNAM','SEJONG','SEOUL','ULSAN') DEFAULT NULL,
  `category_group_name` varchar(50) NOT NULL,
  PRIMARY KEY (`place_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `place`
--

LOCK TABLES `place` WRITE;
/*!40000 ALTER TABLE `place` DISABLE KEYS */;
INSERT INTO `place` VALUES (1,'629289362','서울 용산구 청파동3가 115','스타벅스 숙명여대정문점','SEOUL','카페'),(2,'2007370415','강원특별자치도 춘천시 남산면 방하리 285','빠지에빠지다','GANGWON',''),(3,'1233','서울 용산구 청파로 47길 99 프라임관 지하 1층','스타벅스 숙명여대정문점','SEOUL','학교'),(26,'1233','서울 용산구 청파로 47길 99 프라임관 지하 1층','스타벅스 숙명여대정문점','SEOUL','학교'),(29,'18773756','경기 가평군 청평면 대성리 402-1','실로암펜션','GYEONGGI','숙박'),(30,'1233','서울 용산구 청파로 47길 99 프라임관 지하 1층','스타벅스 숙명여대정문점','SEOUL','학교'),(31,'18773756','경기 가평군 청평면 대성리 402-1','실로암펜션','GYEONGGI','숙박'),(32,'19293737','제주특별자치도 제주시 애월읍 애월리','한담해변','JEJU','관광명소');
/*!40000 ALTER TABLE `place` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_category`
--

DROP TABLE IF EXISTS `post_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_category` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) NOT NULL,
  `board_type` enum('MT_LT','국내학점교류','동행모집','모임구인','졸업_휴학여행','해외교환') NOT NULL,
  `post_categorycol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_category`
--

LOCK TABLES `post_category` WRITE;
/*!40000 ALTER TABLE `post_category` DISABLE KEYS */;
INSERT INTO `post_category` VALUES (1,'동행모집','동행모집',NULL),(2,'모임구인','모임구인',NULL),(3,'졸업_휴학여행','졸업_휴학여행',NULL),(4,'국내학점교류','국내학점교류',NULL),(5,'해외교환','해외교환',NULL),(6,'MT_LT','MT_LT',NULL),(7,'워크숍','MT_LT',NULL),(8,'대학교 LT','MT_LT',NULL),(9,'대학교 LT','모임구인',NULL),(10,'식도락여행','모임구인',NULL),(16,'학점교류','국내학점교류',NULL),(17,'번개','모임구인',NULL),(18,'MT','MT_LT',NULL),(19,'휴학','졸업_휴학여행',NULL);
/*!40000 ALTER TABLE `post_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_likes`
--

DROP TABLE IF EXISTS `post_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_likes` (
  `status` bit(1) NOT NULL,
  `liked_at` datetime(6) NOT NULL,
  `post_id` bigint NOT NULL,
  `post_like_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`post_like_id`),
  KEY `FKc85he3c94qv5vmyutmf0plp69` (`user_id`),
  KEY `fk_postlikes_post_id` (`post_id`),
  CONSTRAINT `fk_postlikes_post_id` FOREIGN KEY (`post_id`) REFERENCES `board` (`post_id`) ON DELETE CASCADE,
  CONSTRAINT `FKc85he3c94qv5vmyutmf0plp69` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_likes`
--

LOCK TABLES `post_likes` WRITE;
/*!40000 ALTER TABLE `post_likes` DISABLE KEYS */;
INSERT INTO `post_likes` VALUES (_binary '','2025-07-23 00:54:18.726722',24,6,5),(_binary '','2025-07-23 10:33:01.898656',28,7,5);
/*!40000 ALTER TABLE `post_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ratings`
--

DROP TABLE IF EXISTS `ratings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ratings` (
  `rating_id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `rating` double NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rating_id`),
  UNIQUE KEY `post_id` (`post_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `ratings_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `board` (`post_id`) ON DELETE CASCADE,
  CONSTRAINT `ratings_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `ratings_chk_1` CHECK (((`rating` >= 0.5) and (`rating` <= 5.0)))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ratings`
--

LOCK TABLES `ratings` WRITE;
/*!40000 ALTER TABLE `ratings` DISABLE KEYS */;
INSERT INTO `ratings` VALUES (4,29,8,4.5,NULL,NULL),(5,29,5,5,NULL,NULL);
/*!40000 ALTER TABLE `ratings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roulette_item`
--

DROP TABLE IF EXISTS `roulette_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roulette_item` (
  `r_item_id` int NOT NULL,
  `r_topic_id` int NOT NULL,
  `r_item_content` text NOT NULL,
  `rItemId` int NOT NULL,
  `rItemContent` text NOT NULL,
  PRIMARY KEY (`r_item_id`),
  KEY `FKnmomhydbr2khx4e93re3qei3u` (`r_topic_id`),
  CONSTRAINT `FKnmomhydbr2khx4e93re3qei3u` FOREIGN KEY (`r_topic_id`) REFERENCES `roulette_topic` (`r_topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roulette_item`
--

LOCK TABLES `roulette_item` WRITE;
/*!40000 ALTER TABLE `roulette_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `roulette_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roulette_topic`
--

DROP TABLE IF EXISTS `roulette_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roulette_topic` (
  `r_topic_id` int NOT NULL,
  `r_topic_name` varchar(50) NOT NULL,
  `rTopicId` int NOT NULL,
  `rTopicName` varchar(50) NOT NULL,
  PRIMARY KEY (`r_topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roulette_topic`
--

LOCK TABLES `roulette_topic` WRITE;
/*!40000 ALTER TABLE `roulette_topic` DISABLE KEYS */;
/*!40000 ALTER TABLE `roulette_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scrap`
--

DROP TABLE IF EXISTS `scrap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scrap` (
  `scrap_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `post_id` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`scrap_id`),
  UNIQUE KEY `unique_scrap` (`user_id`,`post_id`),
  UNIQUE KEY `UK2tomh6dsfjtpyww6kykkr9wce` (`user_id`,`post_id`),
  KEY `fk_scrap_post_id` (`post_id`),
  CONSTRAINT `fk_scrap_post_id` FOREIGN KEY (`post_id`) REFERENCES `board` (`post_id`) ON DELETE CASCADE,
  CONSTRAINT `scrap_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scrap`
--

LOCK TABLES `scrap` WRITE;
/*!40000 ALTER TABLE `scrap` DISABLE KEYS */;
INSERT INTO `scrap` VALUES (5,5,24,'2025-07-23 00:37:28'),(6,5,28,'2025-07-23 10:33:09');
/*!40000 ALTER TABLE `scrap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `search_keywords`
--

DROP TABLE IF EXISTS `search_keywords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `search_keywords` (
  `keyword` varchar(100) NOT NULL,
  `search_count` int NOT NULL DEFAULT '1',
  `last_searched_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`keyword`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `search_keywords`
--

LOCK TABLES `search_keywords` WRITE;
/*!40000 ALTER TABLE `search_keywords` DISABLE KEYS */;
INSERT INTO `search_keywords` VALUES ('',1,'2025-07-22 19:58:52'),('MT',1,'2025-07-23 00:41:02'),('가평',5,'2025-07-23 02:39:28'),('강릉',3,'2025-07-23 02:39:56'),('강릉 카페',540,'2025-07-14 04:57:56'),('강릉여행',1,'2025-07-22 19:43:40'),('대전 관광',370,'2025-07-14 04:57:56'),('부산 여행',980,'2025-07-14 04:57:56'),('서울 맛집',650,'2025-07-14 04:57:56'),('속초 여행',410,'2025-07-14 04:57:56'),('숙명',3,'2025-07-23 00:52:39'),('식도락',1,'2025-07-23 00:41:22'),('여수',1600,'2025-07-22 11:25:31'),('여수 밤바다',430,'2025-07-14 04:57:56'),('전주 한옥마을',330,'2025-07-14 04:57:56'),('제주도',1501,'2025-07-23 02:46:14'),('캠핑',760,'2025-07-14 04:57:56'),('포항 해수욕장',290,'2025-07-14 04:57:56');
/*!40000 ALTER TABLE `search_keywords` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `travel_schedule`
--

DROP TABLE IF EXISTS `travel_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `travel_schedule` (
  `end_date` date DEFAULT NULL,
  `is_public` tinyint(1) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `schedule_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `companions` text,
  `description` text,
  `travel_type` enum('국내','기타','해외') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`schedule_id`),
  KEY `FKqrbp4flie2ro3jbkypdx292pt` (`user_id`),
  CONSTRAINT `FKqrbp4flie2ro3jbkypdx292pt` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `travel_schedule`
--

LOCK TABLES `travel_schedule` WRITE;
/*!40000 ALTER TABLE `travel_schedule` DISABLE KEYS */;
INSERT INTO `travel_schedule` VALUES ('2025-07-25',1,'2025-07-20','2025-07-08 11:14:53.064937',2,3,'제주도 여름 여행','가족','휴양과 관광을 함께 즐기는 제주도 여행입니다.','국내',NULL),('2025-07-25',1,'2025-07-20','2025-07-08 11:16:37.018314',3,3,'제주도 여름 여행','가족','휴양과 관광을 함께 즐기는 제주도 여행입니다.','국내',NULL),('2025-07-25',1,'2025-07-20','2025-07-08 13:28:58.404877',5,3,'테스트','가족','휴양과 관광을 함께 즐기는 제주도 여행입니다.','국내',NULL),('2025-07-25',1,'2025-07-20','2025-07-08 13:29:27.597580',6,3,'테스트2','가족','휴양과 관광을 함께 즐기는 제주도 여행입니다.','국내',NULL),('2025-07-25',1,'2025-07-20','2025-07-17 22:58:35.368219',15,7,'테스트 중','가족','휴양과 관광을 함께 즐기는 제주도 여행입니다.','국내',NULL);
/*!40000 ALTER TABLE `travel_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `nickname` varchar(50) NOT NULL,
  `email_verified` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `profile_image_url` varchar(255) DEFAULT NULL,
  `user_type` enum('ORGANIZATION','PERSONAL') NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2,'정은서','jeongeunseo57@gmail.com','은서',_binary '','2025-07-05 14:38:57.515805','0101111115',NULL,'PERSONAL'),(3,'hyeon da','sally1023p@gmail.com','defaultNickname',_binary '\0','2025-07-07 13:36:16.854917',NULL,NULL,'PERSONAL'),(4,'장유빈','ahdtlf2002@sookmyung.ac.kr','defaultNickname',_binary '','2025-07-09 16:06:12.938490',NULL,NULL,'PERSONAL'),(5,'장유빈','ahdtlf2002@naver.com','defaultNickname',_binary '\0','2025-07-09 16:47:23.106073',NULL,NULL,'PERSONAL'),(7,'백다현','2014865@sookmyung.ac.kr','defaultNickname',_binary '','2025-07-17 22:57:34.646335',NULL,NULL,'PERSONAL'),(8,'정은서','jeunseo0903@sookmyung.ac.kr','대나무행주',_binary '','2025-07-23 00:16:16.112407','0105446762','https://unitripbucket.s3.ap-northeast-2.amazonaws.com/profile/1813239a-5184-45ab-b0fd-4b0d9aea71aa_KakaoTalk_20250717_171047777.png','PERSONAL');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-23  5:38:03
