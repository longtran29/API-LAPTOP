ALTER TABLE order_detail DROP COLUMN created_timestamp;

ALTER TABLE order_detail DROP COLUMN modified_timestamp;

ALTER TABLE orders DROP COLUMN modified_timestamp;

ALTER TABLE reset_token DROP COLUMN modified_timestamp;