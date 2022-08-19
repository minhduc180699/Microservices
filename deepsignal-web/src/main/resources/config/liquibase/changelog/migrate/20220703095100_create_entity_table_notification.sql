DROP TABLE IF EXISTS `notification`;

CREATE TABLE IF NOT EXISTS `notification` (
  `id` bigint(20) NOT NULL,
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `user_id` varchar(40) NOT NULL,
  `is_marked_read` tinyint(1) NOT NULL DEFAULT 0,
  `type` varchar(10) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `notification_user_FK` (`user_id`),
  CONSTRAINT `notification_user_FK_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
