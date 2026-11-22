-- Drop tables
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE inventory (
    id                 BIGINT      PRIMARY KEY,
    sku                VARCHAR(64) NOT NULL,
    available_quantity INTEGER     NOT NULL,
    version            INTEGER     NOT NULL DEFAULT 0,

    CONSTRAINT uk_inventory_sku UNIQUE (sku),
    CONSTRAINT chk_inventory_non_negative CHECK (available_quantity >= 0)
);

CREATE TABLE book_order (
    id             BIGINT                   PRIMARY KEY,
    customer_email VARCHAR(255)             NOT NULL,
    sku            VARCHAR(64)              NOT NULL,
    quantity       INTEGER                  NOT NULL,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT chk_book_orders_positive_qty CHECK (quantity > 0)
);

CREATE INDEX idx_book_orders_customer_email ON book_order (customer_email);

CREATE TABLE idempotency_record (
    idempotency_key VARCHAR(64)              PRIMARY KEY,
    request_hash    VARCHAR(64)              NOT NULL,
    response_status INTEGER                  NOT NULL,
    response_body   TEXT                     NOT NULL,
    expires_at      TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_idempotency_expires_at ON idempotency_record (expires_at);