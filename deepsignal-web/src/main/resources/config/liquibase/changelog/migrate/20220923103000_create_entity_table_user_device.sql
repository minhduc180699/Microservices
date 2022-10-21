DROP TABLE IF EXISTS `user_device`;

CREATE TABLE IF NOT EXISTS `user_device` (
    `id` bigint(20),
    `device_id` VARCHAR(64) NOT NULL,
    `user_id` VARCHAR(64) NOT NULL,
    `secret_key` varchar(64) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT fk_user_device FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
