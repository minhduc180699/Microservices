ALTER TABLE `user_activity_log`
    ADD COLUMN IF NOT EXISTS `user_language` varchar(10) DEFAULT NULL;
