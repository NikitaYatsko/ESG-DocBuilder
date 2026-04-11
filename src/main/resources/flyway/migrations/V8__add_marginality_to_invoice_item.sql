ALTER TABLE esgschema.invoice_item ADD COLUMN marginality NUMERIC(12, 2) DEFAULT 0 NOT NULL;


ALTER TABLE esgschema.invoice ADD COLUMN sum_marginality NUMERIC(12, 2) DEFAULT 0 NOT NULL;