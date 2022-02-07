/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.32 : Database - sys
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`sys` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `sys`;

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `post_id` bigint(20) NOT NULL COMMENT '帖子id',
  `user_id` bigint(20) NOT NULL,
  `content` text,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`post_id`,`user_id`),
  KEY `post_id` (`post_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`),
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Data for the table `comment` */

insert  into `comment`(`id`,`post_id`,`user_id`,`content`,`create_time`) values (2,2,1,'哈哈哈哈','2022-01-27 22:44:09'),(3,3,1,'寄！','2022-01-27 22:44:09'),(5,2,1,'啊宝贵的发不发','2022-02-06 15:38:41');

/*Table structure for table `community` */

DROP TABLE IF EXISTS `community`;

CREATE TABLE `community` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `community_id` bigint(20) NOT NULL COMMENT '社区id',
  PRIMARY KEY (`user_id`,`community_id`),
  KEY `community_id` (`community_id`),
  CONSTRAINT `community_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `community_ibfk_2` FOREIGN KEY (`community_id`) REFERENCES `community_infor` (`com_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `community` */

insert  into `community`(`user_id`,`community_id`) values (1,1),(2,1),(1,2);

/*Table structure for table `community_infor` */

DROP TABLE IF EXISTS `community_infor`;

CREATE TABLE `community_infor` (
  `com_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社区id',
  `com_name` varchar(255) NOT NULL COMMENT '社区名称',
  `com_photo` varchar(255) DEFAULT NULL COMMENT '社区头像',
  `com_infor` varchar(255) DEFAULT NULL COMMENT '社区信息介绍',
  `user_id` bigint(20) NOT NULL COMMENT '创建人id',
  `kind` int(2) NOT NULL COMMENT '类别',
  PRIMARY KEY (`com_id`,`com_name`,`kind`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `community_infor_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `community_infor` */

insert  into `community_infor`(`com_id`,`com_name`,`com_photo`,`com_infor`,`user_id`,`kind`) values (1,'第一社区',NULL,'hhhhhhhh',1,1),(2,'第二社区',NULL,'hhhhh',2,2),(3,'第三社区',NULL,'hhhhhhhhhhhh',1,1),(4,'wrew',NULL,'tqteq',2,3);

/*Table structure for table `post` */

DROP TABLE IF EXISTS `post`;

CREATE TABLE `post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '帖子id',
  `community_id` bigint(20) NOT NULL COMMENT '社区id',
  `user_id` bigint(20) NOT NULL COMMENT '发表人id',
  `book_id` int(11) DEFAULT NULL COMMENT '相关书籍id',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` text COMMENT '帖子内容',
  `photo` varchar(100) DEFAULT NULL COMMENT '图片',
  `type` int(1) DEFAULT '0' COMMENT '0-普通，1-置顶',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`post_id`,`community_id`),
  KEY `community_id` (`community_id`),
  KEY `user_id` (`user_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `post_ibfk_1` FOREIGN KEY (`community_id`) REFERENCES `community_infor` (`com_id`),
  CONSTRAINT `post_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `post_ibfk_3` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `post` */

insert  into `post`(`post_id`,`community_id`,`user_id`,`book_id`,`title`,`content`,`photo`,`type`,`create_time`) values (2,1,1,NULL,'第一',NULL,NULL,0,'2022-01-27 22:23:10'),(3,1,1,1,'第一',NULL,NULL,0,'2022-02-07 22:24:26'),(4,1,1,NULL,'afdas','dgasg',NULL,0,'2022-02-06 15:31:54'),(5,2,1,NULL,'三个生日歌','我哥我哥','',0,'2022-02-06 16:29:22');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
