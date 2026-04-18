ALTER TABLE esgschema.operation
    ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE;
