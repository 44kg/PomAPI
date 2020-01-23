-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: localhost    Database: pom_api
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
-- Table structure for table `gavs`
--

DROP TABLE IF EXISTS `gavs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `gavs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `artifactId` varchar(255) DEFAULT NULL,
  `groupId` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `pom_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_dqpw055jefs9gxswnwrj9si31` (`pom_id`),
  CONSTRAINT `FK_dqpw055jefs9gxswnwrj9si31` FOREIGN KEY (`pom_id`) REFERENCES `poms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gavs`
--

LOCK TABLES `gavs` WRITE;
/*!40000 ALTER TABLE `gavs` DISABLE KEYS */;
INSERT INTO `gavs` VALUES (1,'PomAPI','org.44kg','1.0',1),(2,'maven-shade-plugin','org.apache.maven.plugins','3.2.1',NULL),(3,'jetty-server','org.eclipse.jetty','9.3.0.M0',NULL),(4,'log4j','log4j','1.2.17',NULL),(5,'mysql-connector-java','mysql','8.0.18',NULL),(6,'hibernate-core','org.hibernate','4.3.10.Final',NULL),(7,'maven-compiler-plugin','org.apache.maven.plugins','3.8.1',NULL),(8,'gson','com.google.code.gson','2.8.6',NULL),(9,'jetty-webapp','org.eclipse.jetty','9.3.0.M0',NULL),(10,'junit','junit','4.12',NULL),(11,'L2.1','L2.1','1.0',2),(12,'maven-shade-plugin','org.apache.maven.plugins','3.2.1',NULL),(13,'jetty-server','org.eclipse.jetty','9.3.0.M0',NULL),(14,'mysql-connector-java','mysql','8.0.18',NULL),(15,'maven-compiler-plugin','org.apache.maven.plugins','3.8.1',NULL),(16,'jetty-webapp','org.eclipse.jetty','9.3.0.M0',NULL),(17,'h2','com.h2database','${com.h2database.h2.version}',NULL),(18,'hibernate-core','org.hibernate','${org.hibernate.hibernate-core.version}',NULL),(19,'gson','com.google.code.gson','2.3.1',NULL),(20,'artifactId','groupId','version',3),(21,'test1','test1','test1',NULL),(22,'test2','test2','test2',NULL);
/*!40000 ALTER TABLE `gavs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-23  5:08:32
