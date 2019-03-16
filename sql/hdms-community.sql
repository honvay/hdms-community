-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: hdms-community
-- ------------------------------------------------------
-- Server version	5.7.17-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hdms_activity`
--

DROP TABLE IF EXISTS `hdms_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `directory_id` int(11) DEFAULT NULL,
  `document_id` int(11) DEFAULT NULL,
  `original_name` varchar(255) DEFAULT NULL,
  `current_name` varchar(255) DEFAULT NULL,
  `document_name` varchar(255) DEFAULT NULL,
  `directory_name` varchar(255) DEFAULT NULL,
  `document_type` varchar(20) DEFAULT NULL,
  `operate_date` datetime DEFAULT NULL,
  `operation` varchar(255) DEFAULT NULL,
  `operator` int(11) DEFAULT NULL,
  `content` varchar(500) DEFAULT NULL,
  `outcome_mount` int(11) DEFAULT NULL,
  `outcome_name` varchar(255) DEFAULT NULL,
  `outcome` int(11) DEFAULT NULL,
  `income_mount` int(11) DEFAULT NULL,
  `income_name` varchar(255) DEFAULT NULL,
  `income` int(11) DEFAULT NULL,
  `scope` int(11) DEFAULT NULL,
  `owner` int(11) DEFAULT NULL,
  `owner_type` int(11) DEFAULT NULL,
  `owner_name` varchar(255) DEFAULT NULL,
  `current_permission` varchar(200) DEFAULT NULL,
  `original_permission` varchar(200) DEFAULT NULL,
  `original_description` varchar(500) DEFAULT NULL,
  `current_description` varchar(500) DEFAULT NULL,
  `original_tags` varchar(500) DEFAULT NULL,
  `current_tags` varchar(500) DEFAULT NULL,
  `path` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `hdms_activity_scope_document_id_index` (`scope`,`document_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_activity`
--

LOCK TABLES `hdms_activity` WRITE;
/*!40000 ALTER TABLE `hdms_activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `hdms_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_authorize`
--

DROP TABLE IF EXISTS `hdms_authorize`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_authorize` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authorizer` int(11) DEFAULT NULL,
  `permission_id` int(11) DEFAULT NULL,
  `document_id` int(11) DEFAULT NULL,
  `owner` int(11) DEFAULT NULL,
  `owner_type` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `hdms_authorize_owner_owner_type_index` (`owner`,`owner_type`),
  KEY `idx_document_owner` (`document_id`,`owner`,`owner_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_authorize`
--

LOCK TABLES `hdms_authorize` WRITE;
/*!40000 ALTER TABLE `hdms_authorize` DISABLE KEYS */;
/*!40000 ALTER TABLE `hdms_authorize` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_department`
--

DROP TABLE IF EXISTS `hdms_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(10) NOT NULL,
  `full_pinyin` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `order_no` int(11) DEFAULT NULL,
  `path` varchar(200) DEFAULT NULL,
  `short_pinyin` varchar(255) DEFAULT NULL,
  `parent` int(20) DEFAULT NULL,
  `mount_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcuk5uxt4r9c419c0j4881q9bo` (`parent`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_department`
--

LOCK TABLES `hdms_department` WRITE;
/*!40000 ALTER TABLE `hdms_department` DISABLE KEYS */;
INSERT INTO `hdms_department` VALUES (1,'00','hanweiyunkeji','瀚为云科技',1,'1,','hwykj',-1,2),(2,'12','yanfazhongxin','研发中心',12,'1,2,','yfzx',1,NULL),(3,'11','shichangbu','市场部',1,'1,11,','scb',1,NULL),(4,'003','huanandaqu','华南大区',2,'1,4,','hndq',1,NULL),(5,'0031','huadongdaqu','华东大区',NULL,'1,5,','hddq',1,NULL);
/*!40000 ALTER TABLE `hdms_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_document`
--

DROP TABLE IF EXISTS `hdms_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_document` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(300) DEFAULT NULL,
  `parent` int(11) DEFAULT NULL,
  `path` varchar(200) DEFAULT NULL,
  `full_name` varchar(1000) NOT NULL,
  `mount` int(11) NOT NULL,
  `type` varchar(20) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `permission` int(11) DEFAULT NULL,
  `root` int(10) DEFAULT NULL,
  `tags` varchar(1000) DEFAULT NULL,
  `code` varchar(32) DEFAULT NULL,
  `accessibility` varchar(20) DEFAULT NULL,
  `content_type` varchar(100) DEFAULT NULL,
  `ext` varchar(50) DEFAULT NULL,
  `icon` varchar(100) DEFAULT NULL,
  `md5` varchar(50) DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `locked` bit(1) NOT NULL,
  `locked_by` int(11) DEFAULT NULL,
  `lock_date` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `deleted_by` int(11) DEFAULT NULL,
  `delete_date` datetime DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_mount_parent_path` (`parent`,`mount`,`path`,`type`,`deleted`),
  KEY `hdms_document_mount_index` (`mount`),
  KEY `hdms_document_deleted_current_index` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_document`
--

LOCK TABLES `hdms_document` WRITE;
/*!40000 ALTER TABLE `hdms_document` DISABLE KEYS */;
/*!40000 ALTER TABLE `hdms_document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_favorite`
--

DROP TABLE IF EXISTS `hdms_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_favorite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `document_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_favorite`
--

LOCK TABLES `hdms_favorite` WRITE;
/*!40000 ALTER TABLE `hdms_favorite` DISABLE KEYS */;
/*!40000 ALTER TABLE `hdms_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_login_log`
--

DROP TABLE IF EXISTS `hdms_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client` varchar(50) DEFAULT NULL,
  `login_date` datetime DEFAULT NULL,
  `login_ip` varchar(50) DEFAULT NULL,
  `position` varchar(200) DEFAULT NULL,
  `user_agent` varchar(300) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_login_log`
--

LOCK TABLES `hdms_login_log` WRITE;
/*!40000 ALTER TABLE `hdms_login_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `hdms_login_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_message`
--

DROP TABLE IF EXISTS `hdms_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(500) DEFAULT NULL,
  `document_id` int(11) DEFAULT NULL,
  `read_date` datetime DEFAULT NULL,
  `readed` bit(1) NOT NULL,
  `receiver` int(11) DEFAULT NULL,
  `sender` int(11) DEFAULT NULL,
  `send_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXimtlhkhoe3e0p63swmsm0euoe` (`receiver`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_message`
--

LOCK TABLES `hdms_message` WRITE;
/*!40000 ALTER TABLE `hdms_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `hdms_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_mount`
--

DROP TABLE IF EXISTS `hdms_mount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_mount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner` int(11) DEFAULT NULL,
  `type` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `alias` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_mount`
--

LOCK TABLES `hdms_mount` WRITE;
/*!40000 ALTER TABLE `hdms_mount` DISABLE KEYS */;
INSERT INTO `hdms_mount` VALUES (1,1,1,'我的文档','my'),(2,1,2,'企业文档','enterprise'),(3,2,1,'我的文档','my'),(4,NULL,1,'个人文档','my');
/*!40000 ALTER TABLE `hdms_mount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_notice`
--

DROP TABLE IF EXISTS `hdms_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(1000) NOT NULL,
  `enable` bit(1) NOT NULL,
  `end_date` datetime DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `title` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_notice`
--

LOCK TABLES `hdms_notice` WRITE;
/*!40000 ALTER TABLE `hdms_notice` DISABLE KEYS */;
/*!40000 ALTER TABLE `hdms_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_permission`
--

DROP TABLE IF EXISTS `hdms_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `permissions` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_permission`
--

LOCK TABLES `hdms_permission` WRITE;
/*!40000 ALTER TABLE `hdms_permission` DISABLE KEYS */;
INSERT INTO `hdms_permission` VALUES (1,'编辑者','编辑者','upload,download,create,move,copy,lock,edit,remove,revert,view,share,review'),(2,'上传者','上传者','upload,download,view'),(3,'查看者','查看者','download,view'),(4,'无权限','无权限',NULL);
/*!40000 ALTER TABLE `hdms_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_recent`
--

DROP TABLE IF EXISTS `hdms_recent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_recent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `collect_date` datetime DEFAULT NULL,
  `document_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_document_user` (`document_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_recent`
--

LOCK TABLES `hdms_recent` WRITE;
/*!40000 ALTER TABLE `hdms_recent` DISABLE KEYS */;
/*!40000 ALTER TABLE `hdms_recent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_review`
--

DROP TABLE IF EXISTS `hdms_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_review` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` longtext NOT NULL,
  `number_of_hate` int(11) DEFAULT NULL,
  `number_of_like` int(11) DEFAULT NULL,
  `review_date` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `document_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_document` (`document_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_review`
--

LOCK TABLES `hdms_review` WRITE;
/*!40000 ALTER TABLE `hdms_review` DISABLE KEYS */;
/*!40000 ALTER TABLE `hdms_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_setting`
--

DROP TABLE IF EXISTS `hdms_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `default_password` varchar(100) DEFAULT NULL,
  `enable_watermark` bit(1) DEFAULT NULL,
  `exclude_extensions` varchar(255) DEFAULT NULL,
  `include_extensions` varchar(255) DEFAULT NULL,
  `login_fail_limit` int(11) DEFAULT NULL,
  `max_upload_file_size` bigint(20) DEFAULT NULL,
  `min_length_of_password` int(11) DEFAULT NULL,
  `password_strength` int(11) DEFAULT NULL,
  `show_captcha` bit(1) NOT NULL,
  `show_captcha_on_error` bit(1) NOT NULL,
  `captcha_length` int(11) DEFAULT NULL,
  `watermark_properties` varchar(255) DEFAULT NULL,
  `include_content_types` varchar(300) DEFAULT NULL,
  `exclude_content_types` varchar(300) DEFAULT NULL,
  `multi_part_size` bigint(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_setting`
--

LOCK TABLES `hdms_setting` WRITE;
/*!40000 ALTER TABLE `hdms_setting` DISABLE KEYS */;
INSERT INTO `hdms_setting` VALUES (1,'111111','\0','ext,bat,sh',NULL,5,500,6,2,'\0','\0',4,'',NULL,NULL,NULL);
/*!40000 ALTER TABLE `hdms_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdms_user`
--

DROP TABLE IF EXISTS `hdms_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdms_user` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `department_id` varchar(255) DEFAULT NULL,
  `status` varchar(2) NOT NULL,
  `full_pinyin` varchar(255) DEFAULT NULL,
  `short_pinyin` varchar(255) DEFAULT NULL,
  `change_password` char(1) NOT NULL,
  `quota` int(20) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `sort_desc` char(1) NOT NULL,
  `mode` varchar(255) DEFAULT NULL,
  `sort_field` varchar(255) DEFAULT NULL,
  `mount_id` int(11) DEFAULT NULL,
  `last_login_ip` varchar(255) DEFAULT NULL,
  `login_fail_count` int(11) DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_q376lnutbvsedgn4ijpyp6q4q` (`username`),
  UNIQUE KEY `UK_cwvfg8jnh65oo0elighnilpvl` (`email`),
  KEY `FKcaooquj82on5cbkbvea3r9457` (`department_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdms_user`
--

LOCK TABLES `hdms_user` WRITE;
/*!40000 ALTER TABLE `hdms_user` DISABLE KEYS */;
INSERT INTO `hdms_user` VALUES (1,'admin','$2a$10$Wvqb7e3d1V8kbtoFJX5N1OC6kVAlMKA4GazBsKIbBklCwEMJQZiEa','管理员账号','admin@honvay.com',NULL,'/resource/avatar/1','1','1','guanliyuanzhanghao','glyzh','0',20,'ROLE_SYS_ADMIN','0','list','name',1,'0:0:0:0:0:0:0:1',0,'2019-03-16 15:12:40',NULL),(2,'user1','$2a$10$Wvqb7e3d1V8kbtoFJX5N1OC6kVAlMKA4GazBsKIbBklCwEMJQZiEa','用户1','user1@honvay.com',NULL,'asset/img/avatar.png','2','1','yonghu1','yh1','0',20,'USE','0','list','name',3,NULL,0,NULL,NULL),(3,'user2','$2a$10$qIIbc4L/x8aVopdoQTmoiuuiSDUrTnQHQL9xGn4FI0Ijv1yHfOP3W','用户2','use2r@honvay.com',NULL,'/asset/img/avatar.png','5','1','yonghu2','yh2','1',20,'USER','0','list','name',NULL,NULL,NULL,NULL,NULL),(4,'user3','$2a$10$8kouzVWu.9P6quufe6RA.ucM/ke6cECq6fBiNvyXkK.Y7ft4caIj.','用户3','user3@honvay.com',NULL,'/asset/img/avatar.png','3','1','yonghu3','yh3','0',60,'USER','0','list','name',4,'0:0:0:0:0:0:0:1',0,'2019-03-16 11:23:49',NULL);
/*!40000 ALTER TABLE `hdms_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-16 23:55:53
