DROP TABLE IF EXISTS `interaction_user`;
DROP TABLE IF EXISTS `interaction`;

CREATE TABLE `interaction` (
  `id` bigint(20) NOT NULL,
  `type` int(2) NOT NULL,
  `name` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `interaction_user` (
  `interaction_id` bigint(20) NOT NULL,
  `user_id` varchar(40) NOT NULL,
  `feed_id` varchar(50) NOT NULL COMMENT 'This field map to feed from MongoDB',
  `created_date` timestamp NOT NULL,
  `type` int(2) DEFAULT NULL,
  PRIMARY KEY (`interaction_id`, `user_id`, `feed_id`, `created_date`),
  KEY `interaction_user_FK` (`user_id`),
  CONSTRAINT `interaction_user_FK_1` FOREIGN KEY (`interaction_id`) REFERENCES `interaction` (`id`),
  CONSTRAINT `interaction_user_FK_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
