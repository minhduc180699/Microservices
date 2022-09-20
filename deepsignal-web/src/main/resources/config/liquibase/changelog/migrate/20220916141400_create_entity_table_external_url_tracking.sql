DROP TABLE IF EXISTS `external_url_tracking`;

CREATE TABLE `external_url_tracking` (
    `id` bigint(20) AUTO_INCREMENT,
    `user_id` varchar(40) NOT NULL ,
    `connectome_id` varchar(40) NOT NULL,
    `url` varchar(255),
    `original_url` text NOT NULL,
    `title` varchar(255),
    `created_date` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`connectome_id`) REFERENCES `connectome`(`connectome_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
