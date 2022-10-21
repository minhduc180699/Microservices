ALTER TABLE `external_url_tracking`
    DROP FOREIGN KEY IF EXISTS `external_url_tracking_ibfk_1`,
    DROP FOREIGN KEY IF EXISTS `external_url_tracking_ibfk_2`,
    DROP COLUMN IF EXISTS `user_id`,
    DROP COLUMN IF EXISTS `connectome_id`,
    ADD `short_url` VARCHAR(255) DEFAULT NULL;
