ALTER TABLE products
DROP COLUMN price,
DROP COLUMN compare_at_price;

ALTER TABLE product_variants
DROP COLUMN price,
ADD COLUMN base_price DECIMAL(10, 2),
ADD COLUMN sale_price DECIMAL(10, 2),
ADD COLUMN cost_price DECIMAL(10, 2);
