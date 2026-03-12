ALTER TABLE esgschema.users
    ADD COLUMN IF NOT EXISTS user_photo VARCHAR(255);
