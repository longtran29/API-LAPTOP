ALTER TABLE orders
    ADD payment_status VARCHAR(255);

ALTER TABLE accounts
DROP
COLUMN role_id;