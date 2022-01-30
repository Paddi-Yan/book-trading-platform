/*
 Navicat Premium Data Transfer

 Source Server         : Taylor Swift
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : wx_mini_server

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 30/01/2022 18:26:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 0 COMMENT '0：审核中 1：有效  -1：已失效',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图片',
  `start_time` datetime NULL DEFAULT NULL COMMENT '活动开始时间',
  `deadline` datetime NULL DEFAULT NULL COMMENT '活动截止时间',
  `user_id` int NULL DEFAULT NULL COMMENT '活动发起人ID',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动标签',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1487336533856968706 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity
-- ----------------------------
INSERT INTO `activity` VALUES (1487334536093229058, '活动名称 ', '活动内容', '2022-01-29 15:59:21', -1, NULL, '1988-12-13 00:00:00', '1989-12-13 00:00:00', 1, '活动标签1,活动标签2');
INSERT INTO `activity` VALUES (1487335763040288769, '更新后的活动标题', '更新后的活动内容', '2022-01-29 16:04:13', -1, '/2022-01-29/6147ca2b-a071-403e-8e9a-d1972ae451ce-ByteDance.jpg', '1989-12-01 00:00:00', '1989-12-13 00:00:00', 1, '更新后的活动标签1,更新后的活动标签2');
INSERT INTO `activity` VALUES (1487336533856968705, '活动名称 ', '活动内容', '2022-01-29 16:07:17', 1, '/2022-01-29/08a8a63c-5661-4dea-8151-b7241742f0d8-ByteDance.jpg', '2021-11-11 00:00:00', '2021-12-13 00:00:00', 3, '活动标签1,活动标签2,活动标签3');

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '所属用户ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地区',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES (1, 1, '懒羊羊', '15015984462', '广东省深圳市福田区', '华强北街道');
INSERT INTO `address` VALUES (2, 1, '懒羊羊快递员', '15015988888', '广东省湛江市麻章区', '海大路一号厂东母羊大学');
INSERT INTO `address` VALUES (3, 1, '蕉太狼', '15089128888', '广东省汕头市潮南区', '湖光路海大路饭店');

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `photo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '书籍描述',
  `tag_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类标签id',
  `type` smallint NULL DEFAULT NULL COMMENT '0表示出书 1表示求书',
  `user_id` int NULL DEFAULT NULL COMMENT '所属用户id',
  `created_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `status` smallint NULL DEFAULT 1 COMMENT '书籍当前状态 0-已失效 1-有效',
  `price` decimal(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, 'Java核心技术卷', 'path1,path2', '描述', '1,2', 0, 1, '2022-01-21 20:07:59', 0, 59.00);
INSERT INTO `book` VALUES (2, 'RocketMQ技术内幕', 'path3,path4', '描述', '1,2', 0, 1, '2022-01-21 20:22:03', 1, 59.00);
INSERT INTO `book` VALUES (5, '新修改的书名1', '/2022-01-22/5647b667-47ee-4c60-b341-4bb5a39af09a-ByteDance.jpg', '新修改的描述信息1', '1,2,3', 0, 1, '2022-01-22 15:11:57', 1, 59.00);
INSERT INTO `book` VALUES (6, 'SpringCloud从入门到实战', '/2022-01-23/3ccd099b-5c13-415c-9152-e5f3d710c9fb-ByteDance.jpg,/2022-01-23/c5614a7d-fd8a-47eb-93bb-1939655b673b-Python.jpg', '简介', '1,2', 0, 1, '2022-01-23 13:53:24', 1, 59.00);
INSERT INTO `book` VALUES (7, 'RocketMQ技术内幕', 'path5,path6', '描述', '1,2,3', 0, 1, '2022-01-28 16:34:43', 0, 59.00);
INSERT INTO `book` VALUES (8, 'Netty实战', 'path5,path6', '9成新，大大的好阿！', '1,2,3', 0, 1, '2022-01-28 16:42:39', 1, 59.00);
INSERT INTO `book` VALUES (9, 'Netty0', 'path5,path6', '8成心 想要的来', '1,2,3', 0, 1, '2022-01-28 17:41:39', 1, 59.00);
INSERT INTO `book` VALUES (10, 'Netty1', 'path5,path6', '8成心 想要的来', '1,2,3', 0, 1, '2022-01-28 17:41:39', 1, 60.00);
INSERT INTO `book` VALUES (11, 'Netty2', 'path5,path6', '8成心 想要的来', '1,2,3', 0, 1, '2022-01-28 17:41:39', 1, 61.00);
INSERT INTO `book` VALUES (12, 'Netty3', 'path5,path6', '8成心 想要的来', '1,2,3', 0, 1, '2022-01-28 17:41:39', 1, 62.00);
INSERT INTO `book` VALUES (13, 'Netty4', 'path5,path6', '8成心 想要的来', '1,2,3', 0, 1, '2022-01-28 17:41:39', 1, 63.00);
INSERT INTO `book` VALUES (14, '新视野英语读写第一册', 'path5,path6', '8成新 想要的来', '1,2,3', 1, 1, '2022-01-28 17:47:02', 1, 59.00);
INSERT INTO `book` VALUES (15, '新视野英语读写第二册', 'path5,path6', '5成新 想要的来', '1,2,3', 1, 1, '2022-01-28 17:47:03', 1, 59.00);
INSERT INTO `book` VALUES (16, '新视野英语读写第三册', 'path5,path6', '9.9成心 想要的来', '1,2,3', 1, 1, '2022-01-28 17:47:03', 1, 59.00);
INSERT INTO `book` VALUES (17, '新视野英语读写第一册', 'path5,path6', '8成新 想要的来', '1,2,3', 1, 1, '2022-01-28 17:58:08', 1, 20.00);
INSERT INTO `book` VALUES (18, '新视野英语读写第三册', 'path5,path6', '9.9成新 想要的来', '1,2,3', 1, 1, '2022-01-28 17:58:08', 1, 23.00);
INSERT INTO `book` VALUES (19, '新视野英语读写第二册', 'path5,path6', '5成新 想要的来', '1,2,3', 1, 1, '2022-01-28 17:58:08', 1, 50.00);
INSERT INTO `book` VALUES (21, 'Netty实战', 'path5,path6', '9.9成新 想要的来', '1,2,3', 0, 1, '2022-01-28 17:58:08', 1, 59.00);
INSERT INTO `book` VALUES (22, 'Rocket技术内幕', 'images1,images2', '99成新', '1,2', 0, 1, '2022-01-28 17:58:08', 1, 99.00);

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint NOT NULL,
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
  `book_id` int NULL DEFAULT NULL COMMENT '书籍ID',
  `book_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '书籍名称',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '书籍价格',
  `book_photo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '书籍图片',
  `add_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (1487729616389971970, 3, 8, 'Netty实战', 59.00, 'path5,path6', '2022-01-30 18:09:15', 0);
INSERT INTO `cart` VALUES (1487730140346621954, 1, 8, 'Netty实战', 59.00, 'path5,path6', '2022-01-30 18:11:20', 0);
INSERT INTO `cart` VALUES (1487730317954424833, 1, 22, 'Rocket技术内幕', 99.00, 'images1,images2', '2022-01-30 18:12:03', 0);
INSERT INTO `cart` VALUES (1487730355736715266, 1, 21, 'Netty实战', 59.00, 'path5,path6', '2022-01-30 18:12:12', 0);
INSERT INTO `cart` VALUES (1487732144229912578, 3, 21, 'Netty实战', 59.00, 'path5,path6', '2022-01-30 18:19:18', 0);

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `book_id` int NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorite
-- ----------------------------

-- ----------------------------
-- Table structure for qa
-- ----------------------------
DROP TABLE IF EXISTS `qa`;
CREATE TABLE `qa`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `question` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '问题',
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '答案',
  `activity_id` bigint NULL DEFAULT NULL COMMENT '对应活动编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1487625458987040770 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qa
-- ----------------------------
INSERT INTO `qa` VALUES (1487334536118394881, '答案1', '问题1', 1487334536093229058);
INSERT INTO `qa` VALUES (1487334536118394882, '答案2', '问题2', 1487334536093229058);
INSERT INTO `qa` VALUES (1487336533856968706, '答案1', '问题1', 1487336533856968705);
INSERT INTO `qa` VALUES (1487336533898911746, '答案2', '问题2', 1487336533856968705);
INSERT INTO `qa` VALUES (1487625458987040769, '更新后的活动答案', '更新后的活动问题', 1487335763040288769);

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签名',
  `user_id` int NULL DEFAULT NULL COMMENT '自定义标签用户标识-存在该标识仅供用户自行使用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES (1, '编程', 1);
INSERT INTO `tag` VALUES (2, '教材', NULL);
INSERT INTO `tag` VALUES (3, '武侠', 1);
INSERT INTO `tag` VALUES (4, 'Java', 1);
INSERT INTO `tag` VALUES (5, 'Python', 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gender` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `favorites` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收藏书籍ID',
  `enabled` tinyint NULL DEFAULT 1 COMMENT '是否有效 1-有效 0-被禁用',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `register_time` datetime NULL DEFAULT NULL,
  `last_login_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上次登录IP地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', '123456', '男', NULL, NULL, NULL, '2,1,5', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (3, 'Sabrina', '小明', '123456', '男', 'Sabrina\'s avatar', '15848134876', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (4, 'Taylor Swift', '小明', '123456', '男', 'Sabrina\'s avatar', '15848134876', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (5, 'Taylor Swift', '小明', '123456', '男', 'Sabrina\'s avatar', '15848134876', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (6, 'Taylor Smith', '小明', '123456', '男', 'Sabrina\'s avatar', '15848134876', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (7, '小明', '小明', '123456', '男', 'Sabrina\'s avatar', '15848134876', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (8, '五天小明', '小明', '123456', '男', 'Sabrina\'s avatar', '15848134876', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (9, '小懒羊羊', '小明', '123456', '男', 'Sabrina\'s avatar', '15848134876', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (10, '秃头懒狗', '小明', '123456', '男', 'Sabrina\'s avatar', '15848134876', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
