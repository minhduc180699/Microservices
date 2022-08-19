DROP TABLE IF EXISTS `user_activity_log`;

CREATE TABLE IF NOT EXISTS `user_activity_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50),
  `user_info` text,
  `feed_id` varchar(50) COMMENT 'This field map to feed from MongoDB',
  `connectome_id` varchar(40),
  `activity_name` varchar(100),
  `user_agent` text,
  `user_ip` varchar(50),
  `URL` varchar(255),
  `referer_page` varchar(255),
  `page` varchar(255),
  `query_params` varchar(255),
  `request_method` varchar(20),
  `related_keyword_search` varchar(255),
  `logged_time` timestamp NULL DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

