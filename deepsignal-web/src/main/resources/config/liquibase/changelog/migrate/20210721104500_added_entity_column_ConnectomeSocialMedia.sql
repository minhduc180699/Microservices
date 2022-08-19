ALTER TABLE `connectome_social_media`
    ADD COLUMN IF NOT EXISTS `cookies_state` INT(11) NULL DEFAULT '1' AFTER `uuid`,
    ADD COLUMN IF NOT EXISTS `cookies_state_description` VARCHAR (150) NULL;
