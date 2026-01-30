-- Create product_options table (e.g., Color, Size)
CREATE TABLE product_options (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    position INTEGER DEFAULT 0,
    CONSTRAINT fk_product_options_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Create product_option_values table (e.g., Red, Blue, S, M)
CREATE TABLE product_option_values (
    id BIGSERIAL PRIMARY KEY,
    option_id BIGINT NOT NULL,
    value VARCHAR(100) NOT NULL,
    position INTEGER DEFAULT 0,
    CONSTRAINT fk_option_values_option FOREIGN KEY (option_id) REFERENCES product_options(id) ON DELETE CASCADE
);

-- Create product_variants table (Specific combination with price/stock)
CREATE TABLE product_variants (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    sku VARCHAR(100) UNIQUE,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    image VARCHAR(500),
    CONSTRAINT fk_product_variants_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Create variant_values_map table (Link variant to specific option values)
CREATE TABLE variant_values_map (
    variant_id BIGINT NOT NULL,
    option_value_id BIGINT NOT NULL,
    PRIMARY KEY (variant_id, option_value_id),
    CONSTRAINT fk_variant_values_variant FOREIGN KEY (variant_id) REFERENCES product_variants(id) ON DELETE CASCADE,
    CONSTRAINT fk_variant_values_value FOREIGN KEY (option_value_id) REFERENCES product_option_values(id) ON DELETE CASCADE
);

-- Add simple/variable type indicator to products table
ALTER TABLE products ADD COLUMN has_variants BOOLEAN DEFAULT FALSE;
