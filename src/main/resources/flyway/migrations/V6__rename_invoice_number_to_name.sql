ALTER TABLE esgschema.invoice ADD COLUMN invoice_name VARCHAR(255);


UPDATE esgschema.invoice SET invoice_name = invoice_number;


ALTER TABLE esgschema.invoice ALTER COLUMN invoice_name SET NOT NULL;


ALTER TABLE esgschema.invoice DROP COLUMN invoice_number;