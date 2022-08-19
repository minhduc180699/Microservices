DROP TABLE IF EXISTS `recent_search`;

CREATE TABLE `recent_search` (
  `id` bigint(20) NOT NULL,
  `content` varchar(1000) NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `recent_search_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
