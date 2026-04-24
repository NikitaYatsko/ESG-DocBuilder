ALTER TABLE esgschema.invoice
    ADD COLUMN IF NOT EXISTS discount_percent NUMERIC(5, 2) DEFAULT 0 NOT NULL
    CHECK (discount_percent >= 0 AND discount_percent <= 100);