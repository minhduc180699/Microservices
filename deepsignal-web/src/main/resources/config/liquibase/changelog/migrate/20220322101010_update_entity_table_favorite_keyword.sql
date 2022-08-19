ALTER TABLE favorite_keyword
    ADD COLUMN IF NOT EXISTS connectome_id varchar(40) NOT NULL;
