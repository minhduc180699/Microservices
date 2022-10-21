CREATE TABLE IF NOT EXISTS `user_url_tracking` (
    `user_id` VARCHAR(40) NOT NULL,
    `external_url_id` bigint(20) NOT NULL,
    `click` bigint(20),
    PRIMARY KEY (`user_id`, `external_url_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`external_url_id`) REFERENCES `external_url`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
  COMMENT 'This table is to tracking user click in external url';
