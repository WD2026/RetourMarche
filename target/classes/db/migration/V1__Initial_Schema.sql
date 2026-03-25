-- V1__Initial_Schema.sql

-- Drop tables if they exist (to be safe during baseline or re-runs)
-- Sequence IDs are handled by Hibernate GenerationType.IDENTITY in most entities

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telephone VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(255) NOT NULL DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    price DOUBLE PRECISION,
    image_url VARCHAR(2048),
    description VARCHAR(2048),
    stock INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS smartphone (
    id BIGINT PRIMARY KEY REFERENCES product(id),
    brand VARCHAR(255),
    color VARCHAR(255),
    storage_capacity INTEGER,
    condition VARCHAR(255),
    new_price DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS accessoire (
    id BIGINT PRIMARY KEY REFERENCES product(id),
    type VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS accessoire_smartphones (
    accessoire_id BIGINT REFERENCES accessoire(id),
    smartphones_id BIGINT REFERENCES smartphone(id),
    PRIMARY KEY (accessoire_id, smartphones_id)
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    order_date TIMESTAMP,
    total_price DOUBLE PRECISION,
    status VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    address VARCHAR(255),
    city VARCHAR(255),
    zip VARCHAR(255),
    country VARCHAR(255),
    payment_method VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    product_id BIGINT NOT NULL REFERENCES product(id),
    quantity INTEGER,
    price DOUBLE PRECISION,
    insurance_type VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS cart (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    product_id BIGINT NOT NULL REFERENCES product(id),
    quantity INTEGER NOT NULL,
    insurance_type VARCHAR(255) DEFAULT 'NONE'
);
