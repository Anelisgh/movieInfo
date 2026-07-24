CREATE DATABASE  IF NOT EXISTS `movie_info` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `movie_info`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: movie_info
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `actors`
--

DROP TABLE IF EXISTS `actors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `birth_date` date DEFAULT NULL,
  `debut_year` int DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1gagqkxrjo8341on94djf9y01` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actors`
--

LOCK TABLES `actors` WRITE;
/*!40000 ALTER TABLE `actors` DISABLE KEYS */;
INSERT INTO `actors` VALUES (1,'1974-11-11',1989,_binary '','Leonardo DiCaprio'),(2,'1990-04-15',2001,_binary '','Emma Watson'),(3,'1981-12-13',1999,_binary '','Martin Freeman'),(4,'1961-07-03',1978,_binary '','Tom Cruise'),(5,NULL,NULL,NULL,'Sigourney Weaver'),(6,NULL,NULL,NULL,'Zoe Saldana'),(7,NULL,NULL,NULL,'Sam Worthington'),(8,NULL,NULL,NULL,'Johnny Depp'),(9,NULL,NULL,NULL,'Keira Knightley'),(10,NULL,NULL,NULL,'Orlando Bloom'),(11,NULL,NULL,NULL,'Eddie Murphy'),(12,NULL,NULL,NULL,'Mike Myers'),(13,NULL,NULL,NULL,'Cameron Diaz'),(14,NULL,NULL,NULL,'Robert Downey Jr.'),(15,NULL,NULL,NULL,'Chris Evans'),(16,NULL,NULL,NULL,'Scarlett Johansson'),(17,NULL,NULL,NULL,'Carrie-Anne Moss'),(18,NULL,NULL,NULL,'Keanu Reeves'),(19,NULL,NULL,NULL,'Laurence Fishburne'),(20,NULL,NULL,NULL,'Billy Zane'),(21,NULL,NULL,NULL,'Kate Winslet'),(22,NULL,NULL,NULL,'Elijah Wood'),(23,NULL,NULL,NULL,'Viggo Mortensen'),(24,NULL,NULL,NULL,'Ian McKellen'),(25,NULL,NULL,NULL,'Donna Reed'),(26,NULL,NULL,NULL,'Lionel Barrymore'),(27,NULL,NULL,NULL,'James Stewart'),(28,NULL,NULL,NULL,'Michael Keaton'),(29,NULL,NULL,NULL,'Tom Holland'),(30,NULL,NULL,NULL,'Zendaya'),(31,NULL,NULL,NULL,'Guy Pearce'),(32,NULL,NULL,NULL,'Joe Pantoliano'),(33,NULL,NULL,NULL,'Elissa Knight'),(34,NULL,NULL,NULL,'Jeff Garlin'),(35,NULL,NULL,NULL,'Ben Burtt'),(36,NULL,NULL,NULL,'Anthony Gonzalez'),(37,NULL,NULL,NULL,'Benjamin Bratt'),(38,NULL,NULL,NULL,'Gael García Bernal'),(39,NULL,NULL,NULL,'Matthew McConaughey'),(40,NULL,NULL,NULL,'Kate Hudson'),(41,NULL,NULL,NULL,'Kathryn Hahn'),(42,NULL,NULL,NULL,'Sandra Bullock'),(43,NULL,NULL,NULL,'Samuel L. Jackson'),(44,NULL,NULL,NULL,'Jennifer Aniston'),(45,NULL,NULL,NULL,'Vin Diesel'),(46,NULL,NULL,NULL,'Eli Marienthal'),(47,NULL,NULL,NULL,'America Ferrera'),(48,NULL,NULL,NULL,'Jay Baruchel'),(49,NULL,NULL,NULL,'Gerard Butler'),(50,NULL,NULL,NULL,'Christopher Walken'),(51,NULL,NULL,NULL,'Tom Hanks'),(52,NULL,NULL,NULL,'Ethan Hawke'),(53,NULL,NULL,NULL,'Robert Sean Leonard'),(54,NULL,NULL,NULL,'Robin Williams'),(55,NULL,NULL,NULL,'Kristen Stewart'),(56,NULL,NULL,NULL,'Taylor Lautner'),(57,NULL,NULL,NULL,'Robert Pattinson'),(59,NULL,NULL,NULL,'Skandar Keynes'),(60,NULL,NULL,NULL,'Georgie Henley'),(61,NULL,NULL,NULL,'Tilda Swinton');
/*!40000 ALTER TABLE `actors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `directors`
--

DROP TABLE IF EXISTS `directors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `directors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `birth_date` date DEFAULT NULL,
  `debut_year` int DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt6u48cmktdmyieuc2b6y4umn` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `directors`
--

LOCK TABLES `directors` WRITE;
/*!40000 ALTER TABLE `directors` DISABLE KEYS */;
INSERT INTO `directors` VALUES (1,'1970-07-30',2001,_binary '','Christopher Nolan'),(2,'1964-08-15',2001,_binary '','Peter Jackson'),(3,'1951-10-25',1978,_binary '','Robert Zemeckis'),(4,'1946-11-17',1969,_binary '','Steven Spielberg'),(5,NULL,NULL,_binary '','James Cameron'),(6,NULL,NULL,_binary '','Gore Verbinski'),(7,NULL,NULL,_binary '','Andrew Adamson'),(8,NULL,NULL,_binary '','Joss Whedon'),(9,NULL,NULL,_binary '','Lana Wachowski'),(10,NULL,NULL,_binary '','Frank Capra'),(11,NULL,NULL,_binary '','Jon Watts'),(12,NULL,NULL,_binary '','Andrew Stanton'),(13,NULL,NULL,_binary '','Lee Unkrich'),(14,NULL,NULL,_binary '','Donald Petrie'),(15,NULL,NULL,_binary '','Joel Schumacher'),(16,NULL,NULL,_binary '','Brad Bird'),(17,NULL,NULL,_binary '','Dean DeBlois'),(18,NULL,NULL,_binary '','Peter Weir'),(19,NULL,NULL,_binary '','Catherine Hardwicke'),(20,NULL,NULL,_binary '','a');
/*!40000 ALTER TABLE `directors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_actor`
--

DROP TABLE IF EXISTS `movie_actor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_actor` (
  `movie_id` bigint NOT NULL,
  `actor_id` bigint NOT NULL,
  PRIMARY KEY (`movie_id`,`actor_id`),
  KEY `FK_actor_movie_cascade` (`actor_id`),
  CONSTRAINT `FK90bbe6vpr6eoahw20ta95nkta` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  CONSTRAINT `FK_actor_movie_cascade` FOREIGN KEY (`actor_id`) REFERENCES `actors` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_actor`
--

LOCK TABLES `movie_actor` WRITE;
/*!40000 ALTER TABLE `movie_actor` DISABLE KEYS */;
INSERT INTO `movie_actor` VALUES (1,1),(10,1),(21,1),(2,2),(3,3),(4,4),(5,5),(5,6),(5,7),(6,8),(6,9),(6,10),(7,11),(7,12),(7,13),(8,14),(8,15),(8,16),(9,17),(14,17),(9,18),(9,19),(10,20),(10,21),(11,22),(11,23),(11,24),(12,25),(12,26),(12,27),(13,28),(13,29),(13,30),(14,31),(14,32),(15,33),(15,34),(15,35),(16,36),(16,37),(16,38),(17,39),(18,39),(17,40),(17,41),(18,42),(18,43),(19,44),(19,45),(19,46),(20,47),(20,48),(20,49),(21,50),(21,51),(22,52),(22,53),(22,54),(23,55),(23,56),(23,57),(25,59),(25,60),(25,61);
/*!40000 ALTER TABLE `movie_actor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `genre` enum('ACTION','ADVENTURE','ANIMATION','COMEDY','DOCUMENTARY','DRAMA','FANTASY','HORROR','ROMANCE','SCI_FI','THRILLER') DEFAULT NULL,
  `release_year` int DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `director_id` bigint DEFAULT NULL,
  `photo_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgovm2dombrdnujupo3o7hvtep` (`title`),
  KEY `FK5ft3u8k962bmjd8rn2mr77j8d` (`director_id`),
  CONSTRAINT `FK5ft3u8k962bmjd8rn2mr77j8d` FOREIGN KEY (`director_id`) REFERENCES `directors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies`
--

LOCK TABLES `movies` WRITE;
/*!40000 ALTER TABLE `movies` DISABLE KEYS */;
INSERT INTO `movies` VALUES (1,'A mind-bending thriller.','SCI_FI',2010,'Inception',1,'https://m.media-amazon.com/images/M/MV5BMjExMjkwNTQ0Nl5BMl5BanBnXkFtZTcwNTY0OTk1Mw@@._V1_.jpg'),(2,'A young wizard\'s journey.','FANTASY',2001,'Harry Potter and the Philosopher\'s Stone',2,'https://m.media-amazon.com/images/M/MV5BNTU1MzgyMDMtMzBlZS00YzczLThmYWEtMjU3YmFlOWEyMjE1XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),(3,'A hobbit\'s unexpected adventure.','FANTASY',2012,'The Hobbit: An Unexpected Journey',3,'https://m.media-amazon.com/images/M/MV5BMTcwNTE4MTUxMl5BMl5BanBnXkFtZTcwMDIyODM4OA@@._V1_FMjpg_UX1000_.jpg'),(4,'A journey through time.','SCI_FI',1985,'Back to the Future',4,'https://m.media-amazon.com/images/M/MV5BZmM3ZjE0NzctNjBiOC00MDZmLTgzMTUtNGVlOWFlOTNiZDJiXkEyXkFqcGc@._V1_.jpg'),(5,'A paraplegic marine dispatched to the moon Pandora becomes torn between following his orders and protecting the world he feels is his home.','SCI_FI',2009,'Avatar',5,'https://m.media-amazon.com/images/M/MV5BMTc3MDcwMTc1MV5BMl5BanBnXkFtZTcwMzk4NTU3Mg@@._V1_.jpg'),(6,'Blacksmith Will Turner teams up with eccentric pirate Captain Jack Sparrow to save his love from the undead crew of the Black Pearl.','ADVENTURE',2003,'Pirates of the Caribbean: The Curse of the Black Pearl',6,'https://m.media-amazon.com/images/M/MV5BNDhlMzEyNzItMTA5Mi00YWRhLThlNTktYTQyMTA0MDIyNDEyXkEyXkFqcGc@._V1_.jpg'),(7,'An ogre\'s peace is disrupted when fairy tale creatures are exiled to his swamp, forcing him on a quest to reclaim his home.','ANIMATION',2001,'Shrek',7,'https://m.media-amazon.com/images/M/MV5BN2FkMTRkNTUtYTI0NC00ZjI4LWI5MzUtMDFmOGY0NmU2OGY1XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),(8,'Earth\'s mightiest heroes must come together to stop the mischievous Loki and his alien army from enslaving humanity.','ACTION',2012,'The Avengers',8,'https://m.media-amazon.com/images/M/MV5BNGE0YTVjNzUtNzJjOS00NGNlLTgxMzctZTY4YTE1Y2Y1ZTU4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),(9,'A hacker learns the shocking truth about reality and his role in the war against its controllers.','SCI_FI',1999,'The Matrix',9,'https://m.media-amazon.com/images/M/MV5BN2NmN2VhMTQtMDNiOS00NDlhLTliMjgtODE2ZTY0ODQyNDRhXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),(10,'A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, ill-fated R.M.S. Titanic.','DRAMA',1997,'Titanic',5,'https://m.media-amazon.com/images/M/MV5BYzYyN2FiZmUtYWYzMy00MzViLWJkZTMtOGY1ZjgzNWMwN2YxXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),(11,'A young hobbit, Frodo Baggins, embarks on a perilous journey to destroy an ancient and powerful ring.','FANTASY',2001,'The Lord of the Rings: The Fellowship of the Ring',2,'https://m.media-amazon.com/images/M/MV5BNzIxMDQ2YTctNDY4MC00ZTRhLTk4ODQtMTVlOWY4NTdiYmMwXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),(12,'An angel helps a frustrated businessman by showing him what life would be like if he had never existed.','DRAMA',1946,'It\'s a Wonderful Life',10,'https://m.media-amazon.com/images/M/MV5BMDM4OWFhYjEtNTE5Yy00NjcyLTg5N2UtZDQwNDZlYjlmNDU5XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),(13,'Peter Parker balances his life as a high school student with his superhero alter-ego Spider-Man and faces the villainous Vulture.','ACTION',2017,'Spider-Man: Homecoming',11,'https://m.media-amazon.com/images/M/MV5BODY2MTAzOTQ4M15BMl5BanBnXkFtZTgwNzg5MTE0MjI@._V1_.jpg'),(14,'A man suffering from short-term memory loss uses notes and tattoos to hunt for the man he thinks killed his wife.','THRILLER',2000,'Memento',1,'https://m.media-amazon.com/images/M/MV5BYmQ3MjliNjAtNWFiZS00YWI1LTlmZTktMzBiNDE1NjRhZjU0XkEyXkFqcGc@._V1_.jpg'),(15,'In the distant future, a small waste-collecting robot embarks on a journey that will ultimately decide the fate of mankind.','ANIMATION',2008,'WALL-E',12,'https://m.media-amazon.com/images/M/MV5BMjExMTg5OTU0NF5BMl5BanBnXkFtZTcwMjMxMzMzMw@@._V1_FMjpg_UX1000_.jpg'),(16,'Aspiring musician Miguel embarks on an extraordinary journey to the magical land of his ancestors.','ANIMATION',2017,'Coco',13,'https://m.media-amazon.com/images/M/MV5BMDIyM2E2NTAtMzlhNy00ZGUxLWI1NjgtZDY5MzhiMDc5NGU3XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),(17,'A magazine writer and an advertising executive enter a relationship for very different reasons, leading to unexpected complications.','ROMANCE',2003,'How to Lose a Guy in 10 Days',14,'https://m.media-amazon.com/images/M/MV5BMjE4NTA1NzExN15BMl5BanBnXkFtZTYwNjc3MjM3._V1_FMjpg_UX1000_.jpg'),(18,'A young lawyer defends a black man accused of murdering two white men who attacked his ten-year-old daughter.','DRAMA',1996,'A Time to Kill',15,'https://m.media-amazon.com/images/M/MV5BYmNiYzY1N2ItZDZiNC00ZGMyLWJlZjktZDE1MDI3NDBlYjE5XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),(19,'A young boy befriends a giant robot from outer space that a paranoid government agent wants to destroy.','ANIMATION',1999,'The Iron Giant',16,'https://m.media-amazon.com/images/M/MV5BODM4ZjZjMGEtYmFiMy00MThjLWIzZjUtMDU0ODg1NTI2MzIwXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg'),(20,'A young Viking who aspires to hunt dragons becomes the unlikely friend of a young dragon and learns there may be more to the creatures than he assumed.','ANIMATION',2010,'How to Train Your Dragon',17,'https://m.media-amazon.com/images/M/MV5BMjA5NDQyMjc2NF5BMl5BanBnXkFtZTcwMjg5ODcyMw@@._V1_.jpg'),(21,'A real-life con artist successfully forges millions of dollars\' worth of checks while being pursued by the FBI.','COMEDY',2002,'Catch Me If You Can',4,'https://m.media-amazon.com/images/M/MV5BMTY5MzYzNjc5NV5BMl5BanBnXkFtZTYwNTUyNTc2._V1_FMjpg_UX1000_.jpg'),(22,'An English teacher inspires his students to embrace poetry and seize the day.','DRAMA',1989,'Dead Poets Society',18,'https://resizing.flixster.com/-XZAfHZM39UwaGJIFWKAE8fS0ak=/v3/t/assets/p11671_p_v8_ad.jpg'),(23,'A teenage girl risks everything when she falls in love with a vampire.','ROMANCE',2008,'Twilight',19,'https://m.media-amazon.com/images/M/MV5BMTQ2NzUxMTAxN15BMl5BanBnXkFtZTcwMzEyMTIwMg@@._V1_.jpg'),(25,'Four siblings discover a magical world called Narnia, where they must help a powerful lion named Aslan defeat the White Witch and restore peace to the kingdom.','FANTASY',2005,'The Chronicles of Narnia: The Lion, the Witch and the Wardrobe ',7,'https://m.media-amazon.com/images/M/MV5BMTc0NTUwMTU5OV5BMl5BanBnXkFtZTcwNjAwNzQzMw@@._V1_FMjpg_UX1000_.jpg');
/*!40000 ALTER TABLE `movies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persistent_logins`
--

DROP TABLE IF EXISTS `persistent_logins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persistent_logins`
--

LOCK TABLES `persistent_logins` WRITE;
/*!40000 ALTER TABLE `persistent_logins` DISABLE KEYS */;
/*!40000 ALTER TABLE `persistent_logins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `movie_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `type` enum('PRIVATE','PUBLIC') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK87tlqya0rq8ijfjscldpvvdyq` (`movie_id`),
  KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
  CONSTRAINT `FK87tlqya0rq8ijfjscldpvvdyq` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,'Fantastic movie!',9,1,1,'PRIVATE'),(2,'Magical experience.',8,2,1,'PRIVATE'),(3,'Amazing visuals and story.',8,3,1,'PRIVATE'),(4,'Timeless classic.',10,4,1,'PRIVATE'),(6,'A heartwarming tale about family and tradition. The music and visuals were outstanding!!!!!',9,16,8,'PUBLIC'),(7,'A funny and unique twist on classic fairy tales. It was entertaining, but some jokes were a bit too childish for my taste.',7,7,8,'PUBLIC'),(8,'A powerful story about friendship and self-discovery. The animation and emotional depth are remarkable <3',9,19,8,'PUBLIC'),(16,'Incredible movie!!!\n',9,4,8,'PUBLIC'),(18,'Stunning visuals',8,16,4,'PUBLIC'),(20,'',9,20,8,'PUBLIC'),(21,'',7,6,8,'PUBLIC'),(24,'',8,21,8,'PUBLIC');
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'emma@gmail.com','Emma','123'),(4,'alinam@gmail.com','alinam','$2a$10$Mn4KIZ0SCp3OFhH5lDqlvO7t1DPejcikBacendY4/DnS4NeoshLTi'),(8,'anelis@gmail.com','Anelis<3','$2a$10$OBYfRP/jZBInhpa/Iucb1exXwIi03yHvdS2oNRkPboWnT/EGmbyEy'),(9,'claudia@gmail.com','Claudia','$2a$10$KUwNH.OmEqpGkG.S5froE.jxBPmwbZxbiNhR/ofix/zQNOUWMini.');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `watched_movies`
--

DROP TABLE IF EXISTS `watched_movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `watched_movies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_watched` bit(1) DEFAULT NULL,
  `watch_date` date DEFAULT NULL,
  `movie_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd3n78n4xdmbmj95uwknioqut3` (`movie_id`),
  KEY `FKiq1etortlkgvt122ousi3hksv` (`user_id`),
  CONSTRAINT `FKd3n78n4xdmbmj95uwknioqut3` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  CONSTRAINT `FKiq1etortlkgvt122ousi3hksv` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `watched_movies`
--

LOCK TABLES `watched_movies` WRITE;
/*!40000 ALTER TABLE `watched_movies` DISABLE KEYS */;
INSERT INTO `watched_movies` VALUES (1,_binary '','2023-01-05',1,1),(2,_binary '','2023-01-10',2,1),(3,_binary '','2023-01-15',3,1),(4,_binary '','2023-01-20',4,1),(6,_binary '\0','2024-03-01',1,4),(7,_binary '\0','2024-06-15',2,4),(8,_binary '','2025-03-02',20,8),(9,_binary '','2025-03-02',16,8),(10,_binary '','2025-03-02',7,8),(11,_binary '','2025-03-02',19,8),(14,_binary '','2025-03-12',14,8),(15,_binary '','2025-03-12',3,8),(16,_binary '','2025-03-15',4,8),(17,_binary '','2025-03-16',19,4),(18,_binary '','2025-03-18',16,4),(19,_binary '','2025-03-18',22,8),(20,_binary '','2025-03-29',21,8),(21,_binary '','2025-03-29',6,8),(22,_binary '','2025-03-29',15,8);
/*!40000 ALTER TABLE `watched_movies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `watchlist_movie`
--

DROP TABLE IF EXISTS `watchlist_movie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `watchlist_movie` (
  `watchlist_id` bigint NOT NULL,
  `movie_id` bigint NOT NULL,
  PRIMARY KEY (`watchlist_id`,`movie_id`),
  KEY `FKc26dlxrxso17ix0we5avn2msb` (`movie_id`),
  CONSTRAINT `FKc26dlxrxso17ix0we5avn2msb` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  CONSTRAINT `FKtksh092j5eon7fcfb3p5j8ug8` FOREIGN KEY (`watchlist_id`) REFERENCES `watchlists` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `watchlist_movie`
--

LOCK TABLES `watchlist_movie` WRITE;
/*!40000 ALTER TABLE `watchlist_movie` DISABLE KEYS */;
INSERT INTO `watchlist_movie` VALUES (1,1),(4,1),(8,1),(1,2),(4,2),(2,3),(4,3),(7,3),(2,4),(4,4),(10,4),(7,5),(8,5),(9,5),(7,6),(5,7),(8,7),(10,7),(9,8),(11,9),(7,11),(6,12),(8,13),(10,13),(8,14),(5,16),(9,16),(10,17),(11,17),(11,18),(4,19),(5,19),(5,20),(8,20),(8,21),(9,21),(8,22);
/*!40000 ALTER TABLE `watchlist_movie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `watchlists`
--

DROP TABLE IF EXISTS `watchlists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `watchlists` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsjis83bxhryaemq9m6gv5r0pb` (`user_id`),
  CONSTRAINT `FKsjis83bxhryaemq9m6gv5r0pb` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `watchlists`
--

LOCK TABLES `watchlists` WRITE;
/*!40000 ALTER TABLE `watchlists` DISABLE KEYS */;
INSERT INTO `watchlists` VALUES (1,'Favorite Movies',1),(2,'Must Watch',1),(3,'Classics',1),(4,'My Favorites',4),(5,'Watched',8),(6,'Sad',8),(7,'Plan to Watch',8),(8,'Incredible!',8),(9,'Breathtaking',8),(10,'Wow!',8),(11,'Mindblowing',8);
/*!40000 ALTER TABLE `watchlists` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-24 20:19:52
