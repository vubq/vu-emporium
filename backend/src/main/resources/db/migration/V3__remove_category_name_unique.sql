-- Remove unique constraint from categories name
ALTER TABLE categories DROP CONSTRAINT IF EXISTS categories_name_key;
