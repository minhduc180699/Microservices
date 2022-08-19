ALTER TABLE signal_keywords
    ADD COLUMN IF NOT EXISTS status int(12) NOT NULL DEFAULT 0 COMMENT '0: Normal, -1: Deleted';
