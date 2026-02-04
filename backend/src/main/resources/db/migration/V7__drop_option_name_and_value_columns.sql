-- Migration: Drop name and value columns from Product Options and Values
-- Description: Decouples variants from main tables, relying solely on translations

-- 1. Ensure existing Vietnamese translations exist for all options/values (Optional but safe)
INSERT INTO product_option_translations (product_option_id, language_code, name)
SELECT id, 'vi', name FROM product_options 
ON CONFLICT (product_option_id, language_code) DO NOTHING;

INSERT INTO product_option_value_translations (product_option_value_id, language_code, value)
SELECT id, 'vi', value FROM product_option_values 
ON CONFLICT (product_option_value_id, language_code) DO NOTHING;

-- 2. Drop columns
ALTER TABLE product_options DROP COLUMN name;
ALTER TABLE product_option_values DROP COLUMN value;
