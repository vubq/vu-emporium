-- Fix translation_key type to TEXT to resolve lower(bytea) error
ALTER TABLE i18n_translations ALTER COLUMN translation_key TYPE TEXT;
