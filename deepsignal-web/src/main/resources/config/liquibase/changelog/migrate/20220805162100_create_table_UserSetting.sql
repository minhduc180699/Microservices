DROP TABLE IF EXISTS `user_setting`;

CREATE TABLE `user_setting` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` varchar(40) NOT NULL ,
    `weather` longtext DEFAULT NULL COMMENT 'setting for weather',
    `stock` longtext DEFAULT NULL COMMENT 'setting for stok',
    `loc_weather` varchar(35) DEFAULT NULL COMMENT 'This field description for the location weather',
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `user_setting_fk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
)
ENGINE = INNODB DEFAULT CHARSET=utf8;
