-- Migration: Drop root name, description and SEO columns from products and categories
-- Description: Ensures data is moved to translations table (language 'vi') before dropping columns

-- 1. Safety Data Migration for Products
INSERT INTO product_translations (product_id, language_code, name, description, meta_title, meta_description, meta_keywords)
SELECT 
    p.id, 
    'vi', 
    p.name, 
    p.description,
    p.meta_title,
    p.meta_description,
    p.meta_keywords
FROM products p
WHERE NOT EXISTS (
    SELECT 1 FROM product_translations pt 
    WHERE pt.product_id = p.id AND pt.language_code = 'vi'
);

-- 2. Safety Data Migration for Categories
INSERT INTO category_translations (category_id, language_code, name, description)
SELECT 
    c.id, 
    'vi', 
    c.name, 
    c.description
FROM categories c
WHERE NOT EXISTS (
    SELECT 1 FROM category_translations ct 
    WHERE ct.category_id = c.id AND ct.language_code = 'vi'
);

-- 3. Drop columns from products
ALTER TABLE products 
    DROP COLUMN name,
    DROP COLUMN description,
    DROP COLUMN meta_title,
    DROP COLUMN meta_description,
    DROP COLUMN meta_keywords;

-- 4. Drop columns from categories
ALTER TABLE categories 
    DROP COLUMN name,
    DROP COLUMN description;
