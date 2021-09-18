/*
 Navicat MySQL Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : qqmsg

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 18/09/2021 14:16:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message_log
-- ----------------------------
DROP TABLE IF EXISTS `message_log`;
CREATE TABLE `message_log`
(
    `id`      int                                                            NOT NULL AUTO_INCREMENT,
    `content` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `meta`    json                                                           NOT NULL,
    `uid`     int                                                            NOT NULL,
    `time`    datetime(0)                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FK_uid` (`uid`) USING BTREE,
    CONSTRAINT `FK_uid` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 60
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for note
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note`
(
    `id`    int                                                           NOT NULL AUTO_INCREMENT,
    `main`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qq_info
-- ----------------------------
DROP TABLE IF EXISTS `qq_info`;
CREATE TABLE `qq_info`
(
    `number`  bigint                                                        NOT NULL,
    `pwd`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `name`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `state`   int                                                           NULL DEFAULT 0,
    `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`number`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `uid`                int                                                          NOT NULL AUTO_INCREMENT,
    `name`               varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `password`           varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `admin`              int                                                          NOT NULL DEFAULT 0,
    `config`             json                                                         NOT NULL,
    `cipher`             varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `day_max_send_count` int                                                          NOT NULL,
    PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
