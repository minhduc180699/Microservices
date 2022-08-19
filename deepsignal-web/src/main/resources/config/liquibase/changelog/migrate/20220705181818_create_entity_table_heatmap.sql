DROP TABLE IF EXISTS `heatmap`;

CREATE TABLE `heatmap` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `x` int NOT NULL,
  `y` int NOT NULL,
  `value` int NOT NULL,
  `user_id` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `heatmap_fk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
