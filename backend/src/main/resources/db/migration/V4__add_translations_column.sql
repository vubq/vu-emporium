-- Migration: Add translations column for i18n support
-- Description: Adds a TEXT column to store JSON translations for categories and products

ALTER TABLE categories ADD COLUMN translations TEXT;
ALTER TABLE products ADD COLUMN translations TEXT;

-- Indexing for performance (optional for TEXT/JSON)
-- If we were using PostgreSQL JSONB, we could add GIN indexes here.
-- For now, TEXT is sufficient for Map structure handled by JPA converter.
