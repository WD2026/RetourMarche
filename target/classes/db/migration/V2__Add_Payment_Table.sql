-- V2__Add_Payment_Table.sql

CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    stripe_session_id VARCHAR(255),
    stripe_payment_intent_id VARCHAR(255),
    amount DOUBLE PRECISION,
    currency VARCHAR(10),
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    order_id BIGINT NOT NULL UNIQUE REFERENCES orders(id)
);

-- Note: The orders table already has a status and payment_method. 
-- We'll keep them but might use the payments table for more detailed logs.
