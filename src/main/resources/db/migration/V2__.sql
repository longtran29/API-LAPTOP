ALTER TABLE accounts
DROP
COLUMN role_id;

ALTER TABLE import_details
    ALTER COLUMN quantity SET NOT NULL;