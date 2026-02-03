-- Add status column
ALTER TABLE categories ADD COLUMN status VARCHAR(20) DEFAULT 'DRAFT';

-- Migrate existing data
UPDATE categories SET status = 'ACTIVE' WHERE active = true;
UPDATE categories SET status = 'DRAFT' WHERE active = false;

-- Make status not null
ALTER TABLE categories ALTER COLUMN status SET NOT NULL;

-- Drop active column
ALTER TABLE categories DROP COLUMN active;
