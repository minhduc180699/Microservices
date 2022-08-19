DROP TABLE IF EXISTS `memo`;

CREATE TABLE IF NOT EXISTS `memo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `user_id` varchar(40) NOT NULL,
  `feed_id` varchar(50) NOT NULL COMMENT 'This field map to feed from MongoDB',
  PRIMARY KEY (`id`),
  CONSTRAINT `memo_user_FK_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

