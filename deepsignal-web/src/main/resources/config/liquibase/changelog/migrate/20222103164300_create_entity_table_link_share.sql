DROP TABLE IF EXISTS `link_share`;

CREATE TABLE IF NOT EXISTS `link_share` (
    `id` bigint(20) NOT NULL,
    `connectome_id` varchar(128) NOT NULL,
    `card_id` varchar(128) NOT NULL,
    `state` varchar(32) NOT NULL COMMENT 'PUBLIC/RESTRICTED',
    `email` varchar(128) DEFAULT NULL,
    `phone` varchar(128) DEFAULT NULL,
    `created_date` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `link_share_FK_1` FOREIGN KEY (`connectome_id`) REFERENCES `connectome` (`connectome_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
