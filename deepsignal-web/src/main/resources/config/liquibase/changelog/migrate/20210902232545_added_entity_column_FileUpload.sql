ALTER TABLE `file_upload`
    ADD COLUMN IF NOT EXISTS `type` varchar(10) NULL DEFAULT 'FILE' AFTER `description`;
