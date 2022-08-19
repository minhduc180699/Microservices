DROP TABLE IF EXISTS `comment`;

CREATE TABLE IF NOT EXISTS `comment` (
  `id` bigint(20) NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `user_id` varchar(40) NOT NULL,
  `feed_id` varchar(50) NOT NULL COMMENT 'This field map to feed from MongoDB',
  PRIMARY KEY (`id`),
  KEY `comment_user_FK` (`user_id`),
  CONSTRAINT `comment_user_FK_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `interaction_user` ADD COLUMN IF NOT EXISTS `type` int(2) DEFAULT NULL;
