ALTER TABLE import_details
    ADD import BIGINT;

ALTER TABLE import_details
    ADD CONSTRAINT FK_IMPORT_DETAILS_ON_IMPORT FOREIGN KEY (import) REFERENCES imports (id);

ALTER TABLE import_details
DROP
CONSTRAINT fk2x6upv1e0rx9t3fu8vodwdtin;

ALTER TABLE import_details
DROP
COLUMN import_id;

ALTER TABLE accounts
DROP
COLUMN role_id;