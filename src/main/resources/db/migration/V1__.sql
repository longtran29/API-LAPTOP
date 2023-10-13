ALTER TABLE accounts
DROP
COLUMN role_id;

ALTER TABLE invoices
    ALTER COLUMN due_date DROP NOT NULL;

ALTER TABLE invoices
    ALTER COLUMN invoice_date DROP NOT NULL;