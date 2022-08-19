ALTER TABLE `user`
    ADD COLUMN IF NOT EXISTS `login_failed_count` tinyint(1) DEFAULT 0,
    ADD COLUMN IF NOT EXISTS `locked_on` timestamp NULL DEFAULT NULL,
    ADD COLUMN IF NOT EXISTS `last_login` timestamp NULL DEFAULT NULL,
    ADD COLUMN IF NOT EXISTS `ip` varchar(256) DEFAULT NULL;
