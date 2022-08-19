DROP TABLE IF EXISTS `user_purpose_detail`;
DROP TABLE IF EXISTS `purpose`;
CREATE TABLE `purpose`
(
    `id`           varchar(40) CHARACTER SET utf8 NOT NULL COMMENT 'purpose id, PID_UUID',
    `purpose_name` varchar(50)                    NOT NULL COMMENT 'purpose name',
    `description`  varchar(512) DEFAULT NULL COMMENT 'description',
    `created_date` timestamp NULL DEFAULT NULL,
    `last_modified_date` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
);

CREATE TABLE `user_purpose_detail`
(
    `user_id`    varchar(40) CHARACTER SET utf8 NOT NULL,
    `purpose_id` varchar(40) CHARACTER SET utf8 NOT NULL,
    PRIMARY KEY (`user_id`, `purpose_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `user_purpose_detail_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `user_purpose_detail_ibfk_2` FOREIGN KEY (`purpose_id`) REFERENCES `purpose` (`id`)
);

BEGIN;
INSERT INTO `purpose` VALUES ('PID_494deb76-0e6a-4fb1-b7d1-442e2ba073e2', 'Investment Information', NULL, '2021-07-23 13:43:43', '2021-07-23 13:43:47');
INSERT INTO `purpose` VALUES ('PID_91ddd542-7ce8-479e-a659-677008d2cf46', 'Research material', NULL, '2021-07-23 13:44:27', '2021-07-23 13:44:30');
INSERT INTO `purpose` VALUES ('PID_bd144440-13c0-457e-aec7-dd24d797c5c6', 'Marketing materials', NULL, '2021-07-23 13:45:14', '2021-07-23 13:45:16');
INSERT INTO `purpose` VALUES ('PID_c95168a7-da5a-45f6-b27e-ee62d23c34c3', 'IR Data', NULL, '2021-07-23 13:45:44', '2021-07-23 13:45:46');
COMMIT;
