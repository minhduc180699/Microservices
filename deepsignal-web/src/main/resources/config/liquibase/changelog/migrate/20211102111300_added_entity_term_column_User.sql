ALTER TABLE `user`
    ADD COLUMN IF NOT EXISTS `term_of_service` tinyint(1) DEFAULT 0,
    ADD COLUMN IF NOT EXISTS `receive_news` tinyint(1) DEFAULT 0;

