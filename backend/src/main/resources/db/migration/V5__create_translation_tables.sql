-- Migration: Create translation tables for refactoring i18n from JSON to Relational
-- Description: Creates specific translation tables for Categories and Products

-- 1. Create category_translations table
CREATE TABLE category_translations (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    language_code VARCHAR(5) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (category_id, language_code),
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- 2. Create product_translations table
CREATE TABLE product_translations (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    language_code VARCHAR(5) NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    meta_title VARCHAR(200),
    meta_description TEXT,
    meta_keywords VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (product_id, language_code),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- 3. Data migration from JSON to new tables
-- Attempt to migrate existing JSON data if columns exist and have content
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='categories' AND column_name='translations') THEN
        INSERT INTO category_translations (category_id, language_code, name, description)
        SELECT 
            id, 
            lang.key, 
            lang.value->>'name', 
            lang.value->>'description'
        FROM categories, 
        jsonb_each((translations::jsonb)) as lang
        WHERE translations IS NOT NULL AND translations != '';
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='products' AND column_name='translations') THEN
        INSERT INTO product_translations (product_id, language_code, name, description, meta_title, meta_description, meta_keywords)
        SELECT 
            id, 
            lang.key, 
            lang.value->>'name', 
            lang.value->>'description',
            lang.value->>'metaTitle',
            lang.value->>'metaDescription',
            lang.value->>'metaKeywords'
        FROM products, 
        jsonb_each((translations::jsonb)) as lang
        WHERE translations IS NOT NULL AND translations != '';
    END IF;
END $$;

-- 4. Drop the old translations column
ALTER TABLE categories DROP COLUMN IF EXISTS translations;
ALTER TABLE products DROP COLUMN IF EXISTS translations;
