/*
 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : questionnaire_admin

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 30/04/2021 14:19:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha`
(
    `uuid`        char(36)    NOT NULL COMMENT '主键uuid',
    `code`        varchar(6)  NOT NULL COMMENT '验证码',
    `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
    PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统验证码临时存储表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
    `id`          bigint(0)     NOT NULL AUTO_INCREMENT,
    `param_key`   varchar(50)   NULL DEFAULT NULL COMMENT 'key',
    `param_value` varchar(2000) NULL DEFAULT NULL COMMENT 'value',
    `status`      tinyint(0)    NULL DEFAULT 1 COMMENT '状态 0：隐藏  1：显示',
    `remark`      varchar(500)  NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `param_key` (`param_key`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统配置表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config`
VALUES (1, 'CLOUD_STORAGE_CONFIG_KEY',
        '{\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"aliyunDomain\":\"\",\"aliyunEndPoint\":\"\",\"aliyunPrefix\":\"\",\"qcloudBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuBucketName\":\"ios-app\",\"qiniuDomain\":\"http://7xqbwh.dl1.z0.glb.clouddn.com\",\"qiniuPrefix\":\"upload\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"type\":1}',
        0, '云存储配置信息');
INSERT INTO `sys_config`
VALUES (2, 'system_name', '问卷Online后台管理系统', 1, '系统名称配置信息');
INSERT INTO `sys_config`
VALUES (3, 'system_version', 'v1.0.0', 1, '系统当前版本');
INSERT INTO `sys_config`
VALUES (4, 'system_copyright', '&copy cloudgyb.版权所有.', 1, '系统版权信息');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`
(
    `id`          bigint        NOT NULL AUTO_INCREMENT,
    `username`    varchar(50)   NULL DEFAULT NULL COMMENT '操作用户名',
    `operation`   varchar(50)   NULL DEFAULT NULL COMMENT '操作',
    `method`      varchar(200)  NULL DEFAULT NULL COMMENT '生成日志的方法签名',
    `params`      varchar(5000) NULL DEFAULT NULL COMMENT '生成日志的方法入参',
    `time`        bigint        NOT NULL COMMENT '方法执行时长（ms）',
    `ip`          varchar(64)   NULL DEFAULT NULL COMMENT '用户ip地址',
    `create_date` datetime      NULL DEFAULT NULL COMMENT '日志生成时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统日志存储表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `menu_id`   bigint       NOT NULL AUTO_INCREMENT,
    `parent_id` bigint       NULL DEFAULT NULL COMMENT '菜单的父ID，一级菜单父ID为0',
    `name`      varchar(50)  NULL DEFAULT NULL COMMENT '菜单名称',
    `url`       varchar(200) NULL DEFAULT NULL COMMENT '菜单URL',
    `perms`     varchar(500) NULL DEFAULT NULL COMMENT '菜单绑定的权限，多个权限英文逗号分割，例如(user:list,user:create)',
    `type`      int          NULL DEFAULT 2 COMMENT '菜单类型，0一级菜单，1二级菜单，2按钮或功能',
    `icon`      varchar(50)  NULL DEFAULT NULL COMMENT '菜单图标',
    `order_num` int          NULL DEFAULT NULL COMMENT '菜单同级的排序号',
    `open`      tinyint      NULL DEFAULT 0 COMMENT '是否展开菜单下的子菜单，0否1是',
    PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单存储表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`
VALUES (1, 0, '系统管理', '/sys', NULL, 0, 'el-icon-setting', 19999, 0);
INSERT INTO `sys_menu`
VALUES (2, 1, '管理员管理', '/sys/user', NULL, 1, 'admin', 1, 0);
INSERT INTO `sys_menu`
VALUES (3, 1, '角色管理', '/sys/role', NULL, 1, 'role', 2, 0);
INSERT INTO `sys_menu`
VALUES (4, 1, '菜单管理', '/sys/menu', NULL, 1, 'menu', 3, 0);
INSERT INTO `sys_menu`
VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0, 0);
INSERT INTO `sys_menu`
VALUES (16, 2, '新增', NULL, 'sys:user:save', 2, NULL, 1, 0);
INSERT INTO `sys_menu`
VALUES (17, 2, '修改', NULL, 'sys:user:update', 2, NULL, 2, 0);
INSERT INTO `sys_menu`
VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 4, 0);
INSERT INTO `sys_menu`
VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0, 0);
INSERT INTO `sys_menu`
VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0, 0);
INSERT INTO `sys_menu`
VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0, 0);
INSERT INTO `sys_menu`
VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0, 0);
INSERT INTO `sys_menu`
VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0, 0);
INSERT INTO `sys_menu`
VALUES (24, 4, '新增', NULL, 'sys:menu:add,sys:menu:select', 2, NULL, 0, 0);
INSERT INTO `sys_menu`
VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0, 0);
INSERT INTO `sys_menu`
VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0, 0);
INSERT INTO `sys_menu`
VALUES (27, 1, '配置管理', '/sys/config',
        'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', 1, 'config', 6, 0);
INSERT INTO `sys_menu`
VALUES (29, 1, '系统日志', '/sys/log', 'sys:log:list', 1, 'log', 7, 0);
INSERT INTO `sys_menu`
VALUES (31, 0, '调查问卷模板管理', '/template', 'sys:template:*', 0, 'el-icon-document', 1, 1);
INSERT INTO `sys_menu`
VALUES (32, 31, '调查问卷类别管理', '/template/type', 'sys:template:type:*', 1, NULL, 0, 0);
INSERT INTO `sys_menu`
VALUES (33, 31, '调查问卷模板管理', '/template', 'sys:template:*', 1, NULL, 1, 0);
INSERT INTO `sys_menu`
VALUES (34, 0, '用户管理', '/user', 'user:*', 0, 'el-icon-user', 2, 1);
INSERT INTO `sys_menu`
VALUES (35, 34, '用户列表', '/user/list', 'user:list', 1, NULL, 1, 0);
INSERT INTO `sys_menu`
VALUES (37, 0, '首页', '/', NULL, 0, 'el-icon-switch-button', 0, 1);
INSERT INTO `sys_menu`
VALUES (38, 29, '日志删除', NULL, 'sys:log:delete', 2, NULL, 0, 0);
INSERT INTO `sys_menu`
VALUES (55, 2, '锁定', NULL, 'sys:user:lock', 2, NULL, 3, 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `role_id`        bigint       NOT NULL AUTO_INCREMENT,
    `role_name`      varchar(100) NULL DEFAULT '' COMMENT '角色名称',
    `remark`         varchar(100) NULL DEFAULT '' COMMENT '角色描述',
    `create_user_id` bigint       NULL DEFAULT NULL COMMENT '创建者ID',
    `create_time`    datetime     NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES (1, '超级管理员', '超级管理员具有所有权限', 1, '2021-03-16 14:01:26');
INSERT INTO `sys_role`
VALUES (2, '一般管理员', '一般管理员', 1, '2021-03-16 14:02:13');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `id`      bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id` bigint NULL DEFAULT NULL COMMENT '角色ID',
    `menu_id` bigint NULL DEFAULT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色与菜单关系对应表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu`
VALUES (1, 2, 37);
INSERT INTO `sys_role_menu`
VALUES (2, 2, 31);
INSERT INTO `sys_role_menu`
VALUES (3, 2, 32);
INSERT INTO `sys_role_menu`
VALUES (4, 2, 33);
INSERT INTO `sys_role_menu`
VALUES (5, 2, 34);
INSERT INTO `sys_role_menu`
VALUES (6, 2, 35);
INSERT INTO `sys_role_menu`
VALUES (7, 2, 29);
INSERT INTO `sys_role_menu`
VALUES (8, 2, 38);
INSERT INTO `sys_role_menu`
VALUES (9, 2, 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `user_id`        bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`       varchar(50)  NOT NULL COMMENT '用户名',
    `password`       varchar(100) NULL     DEFAULT NULL COMMENT '密码',
    `salt`           varchar(20)  NULL     DEFAULT NULL COMMENT '密码盐',
    `email`          varchar(100) NULL     DEFAULT '' COMMENT '邮箱',
    `mobile`         varchar(100) NULL     DEFAULT '' COMMENT '手机号',
    `status`         tinyint      NOT NULL DEFAULT 1 COMMENT '用户状态，0禁用，1正常',
    `create_user_id` bigint       NULL     DEFAULT NULL COMMENT '该用户的创建者',
    `create_time`    datetime     NULL     DEFAULT NULL COMMENT '用户创建时间',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE INDEX `username` (`username`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES (1, 'admin', '9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d', 'YzcmCZNvbXocrsz9dm8e',
        '277478869@qq.com', '13500000001', 1, 1, '2020-12-03 11:11:14');
INSERT INTO `sys_user`
VALUES (2, 'zhangsan', 'ca737ebdfe313f7eaef88a2cbffee72a55028b68191e1ecc6452d4e9ab196d89', 'uFLFx7RNGdvt6f2hO8Kv',
        'sdfsfdsf@163.com', '13500000002', 1, 1, '2021-04-26 17:39:56');
INSERT INTO `sys_user`
VALUES (3, '李四', '02d6d3b2e5baf54d406cd76282896650362befe5f4762863aec96292e99c4985', 'ByLsA4EYBVtppyiTOAZf',
        'fsdfsdfsd@qq.com', '13500000003', 1, 1, '2021-04-26 17:41:57');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`      bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
    `role_id` bigint NULL DEFAULT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户与角色关系对应表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role`
VALUES (1, 1, 1);
INSERT INTO `sys_user_role`
VALUES (2, 2, 2);
INSERT INTO `sys_user_role`
VALUES (3, 3, 2);

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`
(
    `user_id`     bigint       NOT NULL,
    `token`       varchar(100) NOT NULL COMMENT 'token',
    `expire_time` datetime     NULL DEFAULT NULL COMMENT '过期时间',
    `update_time` datetime     NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE INDEX `token` (`token`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户TOKEN存储表'
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
