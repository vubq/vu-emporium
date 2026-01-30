-- Add status column to product_options
ALTER TABLE product_options 
ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';

-- Add status column to product_option_values
ALTER TABLE product_option_values 
ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';

-- Update existing records to ACTIVE
UPDATE product_options SET status = 'ACTIVE' WHERE status IS NULL;
UPDATE product_option_values SET status = 'ACTIVE' WHERE status IS NULL;
