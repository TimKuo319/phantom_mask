ALTER TABLE masks
ADD COLUMN quantity INT NOT NULL DEFAULT 0;

ALTER TABLE masks
ADD CONSTRAINT uq_store_mask UNIQUE (store_id, name);

ALTER TABLE stores
ADD CONSTRAINT uq_store_name UNIQUE (name);

ALTER TABLE users
ADD CONSTRAINT uq_user_name UNIQUE (name);

ALTER TABLE opening_hours
ADD CONSTRAINT uq_opening_hour UNIQUE (store_id, day_of_week, start_time, end_time);

ALTER TABLE transactions
ADD CONSTRAINT uq_transaction UNIQUE (user_id, store_id, mask_id, transaction_date);
