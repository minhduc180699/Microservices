ALTER TABLE `user` ADD COLUMN IF NOT EXISTS
    `time_zone` varchar(20) DEFAULT NULL COMMENT 'This field description for the user"s timezone';
