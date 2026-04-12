ALTER TABLE esgschema.invoice ADD COLUMN IF NOT EXISTS power_kwt NUMERIC(10, 2);
ALTER TABLE esgschema.invoice ADD COLUMN IF NOT EXISTS invoice_name VARCHAR(255);
UPDATE esgschema.invoice SET invoice_name = invoice_number WHERE invoice_name IS NULL;
ALTER TABLE esgschema.invoice ALTER COLUMN invoice_name SET NOT NULL;
ALTER TABLE esgschema.invoice DROP COLUMN IF EXISTS invoice_number;
ALTER TABLE esgschema.invoice ADD COLUMN IF NOT EXISTS vat_amount NUMERIC(12, 2) DEFAULT 0 NOT NULL;
ALTER TABLE esgschema.invoice ADD COLUMN IF NOT EXISTS sum_amount NUMERIC(12, 2) DEFAULT 0 NOT NULL;
ALTER TABLE esgschema.invoice_item ADD COLUMN IF NOT EXISTS marginality NUMERIC(12, 2) DEFAULT 0 NOT NULL;
ALTER TABLE esgschema.invoice ADD COLUMN IF NOT EXISTS sum_marginality NUMERIC(12, 2) DEFAULT 0 NOT NULL;