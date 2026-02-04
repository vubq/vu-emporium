-- Migration: Create translation tables for Product Options and Values
-- Description: Creates specific translation tables for internationalizing variants

-- 1. Create product_option_translations table
CREATE TABLE product_option_translations (
    id BIGSERIAL PRIMARY KEY,
    product_option_id BIGINT NOT NULL,
    language_code VARCHAR(5) NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (product_option_id, language_code),
    FOREIGN KEY (product_option_id) REFERENCES product_options(id) ON DELETE CASCADE
);

-- 2. Create product_option_value_translations table
CREATE TABLE product_option_value_translations (
    id BIGSERIAL PRIMARY KEY,
    product_option_value_id BIGINT NOT NULL,
    language_code VARCHAR(5) NOT NULL,
    value VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (product_option_value_id, language_code),
    FOREIGN KEY (product_option_value_id) REFERENCES product_option_values(id) ON DELETE CASCADE
);
