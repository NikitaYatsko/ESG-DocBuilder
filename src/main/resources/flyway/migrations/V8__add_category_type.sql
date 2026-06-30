ALTER TABLE esgschema.banking_categories
    ADD COLUMN type VARCHAR(20) NOT NULL DEFAULT 'EXPENSE';

ALTER TABLE esgschema.banking_categories
    ADD CONSTRAINT chk_category_type CHECK (type IN ('INCOME', 'EXPENSE'));

UPDATE esgschema.banking_categories bc
SET type = (
    SELECT op.type
    FROM esgschema.operation op
    WHERE op.id_categories = bc.id
    LIMIT 1
    )
WHERE EXISTS (
    SELECT 1 FROM esgschema.operation op WHERE op.id_categories = bc.id
    );